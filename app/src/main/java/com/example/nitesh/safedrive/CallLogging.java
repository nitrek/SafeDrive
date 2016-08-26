package com.example.nitesh.safedrive;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by nitesh on 25-08-2016.
 */
public class CallLogging extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call_log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_call_log, container, false);
            ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.section_label);
            ListView lv = new ListView(getContext());
            scrollView.addView(lv);
            ArrayList<String> callLogs = new ArrayList<String>();
            Uri allCalls = Uri.parse("content://call_log/calls");
            SharedPreferences preferences = getContext().getSharedPreferences(this.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
            // new String[]{String.valueOf( preferences.getLong(HomeActivity.DRIVETIME, System.currentTimeMillis()-10000))}
            Cursor c = getContext().getContentResolver().query(allCalls, null, null, null, null, null);
            int number = c.getColumnIndex(CallLog.Calls.NUMBER);
            int type = c.getColumnIndex(CallLog.Calls.TYPE);
            int date = c.getColumnIndex(CallLog.Calls.DATE);
            int duration = c.getColumnIndex(CallLog.Calls.DURATION);
            while (c.moveToNext()) {
                String phNum = c.getString(number);
                int intCallType = c.getInt(type);
                String callDate = c.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                // Date saveDayTime = new Date(preferences.getLong(Constants.DRIVETIME, System.currentTimeMillis() - 10000));
                String callDuration = c.getString(duration);
                String callType = "Incoming";
                switch (intCallType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                }
                String callLogDetails = ("Number: " + phNum + " \n Type: " + callType + " \nDate: " + callDayTime + " \nDuration: " + callDuration);
                Log.i("time ret", Long.valueOf(callDate).toString());
                Log.i("time sav", Long.valueOf(preferences.getLong(Constants.DRIVETIME, System.currentTimeMillis() - 10000)).toString());


                if (Long.valueOf(callDate) > preferences.getLong(Constants.DRIVETIME, System.currentTimeMillis() - 10000))
                    callLogs.add(callLogDetails);
            }
            Collections.reverse(callLogs);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, callLogs);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String txt = (String) parent.getItemAtPosition(position);
                    String number = txt.substring(7, 22);
                    if (number.contains("+91"))
                        number = txt.substring(11, 22);
                    else
                        number = txt.substring(7, 18);
                    Toast.makeText(getContext(), number, Toast.LENGTH_LONG).show();
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(phoneIntent);
                        return;
                    }
                }
            });
            return rootView;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Calls";
               /* case 1:
                    return "Outgoing Calls";
                case 2:
                    return "Missed Calls";*/
            }
            return null;
        }
    }
}
