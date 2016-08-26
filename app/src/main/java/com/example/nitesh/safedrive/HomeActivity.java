package com.example.nitesh.safedrive;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String TAG = "HomeActivity";
    Intent intent;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.context = HomeActivity.this;
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // checkPermissions();
        AsyncTaskForPermissions runner = new AsyncTaskForPermissions();
        runner.execute();
        restoreCheckBoxState();
        showThought();

    }

    public void restoreCheckBoxState() {

        ToggleButton toggleButton;
        Switch s1, s2, s3;

        toggleButton = (ToggleButton) findViewById(R.id.driveModeToggle);
        toggleButton.setChecked(getFromSP(Constants.DRIVE_MODE));
        driveToggle(toggleButton);

        s1 = (Switch) findViewById(R.id.autoCall);
        s1.setChecked(getFromSP(Constants.AUTO_CALL));
        autoCall(s1);

        s2 = (Switch) findViewById(R.id.autoSms);
        s2.setChecked(getFromSP(Constants.AUTO_SMS));
        autoSms(s2);

    }


    public void autoSms(View view) {
        Switch smsToggleButton = (Switch) view;
        saveInSp(Constants.AUTO_SMS, smsToggleButton.isChecked());
        if (smsToggleButton.isChecked()) {
            smsToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            smsToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void autoCall(View view) {
        Switch callToggleButton = (Switch) view;
        saveInSp(Constants.AUTO_CALL, callToggleButton.isChecked());
        if (callToggleButton.isChecked()) {
            callToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            callToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void callLog(View view) {
        Switch callLogToggleButton = (Switch) view;
        saveInSp(Constants.CALL_LOG, callLogToggleButton.isChecked());
        if (callLogToggleButton.isChecked()) {
            callLogToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            callLogToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void addSwitches() {
        Switch s1 = (Switch) findViewById(R.id.autoSms);
        Switch s2 = (Switch) findViewById(R.id.autoCall);
        Button b1 = (Button) findViewById(R.id.sos);
        ImageView im = (ImageView) findViewById(R.id.relax);
        im.setVisibility(View.GONE);
        s1.setVisibility(View.VISIBLE);
        s2.setVisibility(View.VISIBLE);
        b1.setVisibility(View.VISIBLE);


    }

    public void removeSwitches() {
        Switch s1 = (Switch) findViewById(R.id.autoSms);
        Switch s2 = (Switch) findViewById(R.id.autoCall);
        ImageView im = (ImageView) findViewById(R.id.relax);
        Button b1 = (Button) findViewById(R.id.sos);

        s1.setVisibility(View.GONE);
        s2.setVisibility(View.GONE);
        im.setVisibility(View.VISIBLE);
        b1.setVisibility(View.GONE);
    }

    public void driveToggle(View view) {
        ToggleButton driveToggleButton = (ToggleButton) view;
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        saveInSp(Constants.DRIVE_MODE, driveToggleButton.isChecked());
        TextView textView = (TextView) findViewById(R.id.stats);
        if (driveToggleButton.isChecked()) {
            driveToggleButton.setText("Drive Mode:On");
            relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            showNotification();
            addSwitches();
            AudioManager am;
            am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int previousState = am.getRingerMode();
            Toast.makeText(this, "previous state" + previousState, Toast.LENGTH_LONG).show();
            if (!getFromSP(Constants.FIRST_CALL)) {
                saveInSp(Constants.FIRST_CALL, true);
                saveInSp(Constants.previousRingingState, previousState);
                saveInSp(Constants.DRIVETIME, System.currentTimeMillis());
            }
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            showThought();

            textView.setVisibility(View.GONE);

        } else {
            cancelNotification(0);
            saveInSp(Constants.FIRST_CALL, false);
            driveToggleButton.setText("Drive Mode:Off");
            relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            Switch cb2, cb3;
            cb2 = (Switch) findViewById(R.id.autoCall);
            cb3 = (Switch) findViewById(R.id.autoSms);
            cb2.setChecked(false);
            cb3.setChecked(false);
            removeSwitches();
            String stats = "In Last Drive Session \nCalls:" + getIntFromSP(Constants.CALLCOUNT) + " SMSSent :" + getIntFromSP(Constants.SENTSMSCOUNT)
                    + " SmsRecivedCount: " + getIntFromSP(Constants.SMSCOUNT);
            textView.setText(stats);
            saveInSp(Constants.CALLCOUNT, 0);
            saveInSp(Constants.SENTSMSCOUNT, 0);
            saveInSp(Constants.SMSCOUNT, 0);
            textView.setVisibility(View.VISIBLE);
            showThought();
            AudioManager am2;
            am2 = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            Toast.makeText(this, "previous state now" + getIntFromSP(Constants.previousRingingState), Toast.LENGTH_LONG).show();

            am2.setRingerMode(getIntFromSP(Constants.previousRingingState));

            cb2.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            cb3.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

        }
    }

    private boolean getFromSP(String key) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }


    private int getIntFromSP(String key) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    private String getStringFromSP(String key) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    private void saveInSp(String key, boolean value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void saveInSp(String key, int value) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void saveInSp(String key, Long value) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void showThought() {
        TextView tv = (TextView) findViewById(R.id.thought);
        ArrayList<String> thoughts = new ArrayList<String>();
        thoughts.add(getResources().getString(R.string.thought1));
        thoughts.add(getResources().getString(R.string.thought2));
        thoughts.add(getResources().getString(R.string.thought3));
        thoughts.add(getResources().getString(R.string.thought4));
        thoughts.add(getResources().getString(R.string.thought5));
        thoughts.add(getResources().getString(R.string.thought6));
        thoughts.add(getResources().getString(R.string.thought7));
        thoughts.add(getResources().getString(R.string.thought8));
        thoughts.add(getResources().getString(R.string.thought9));
        thoughts.add(getResources().getString(R.string.thought10));
        thoughts.add(getResources().getString(R.string.thought11));

        Random r = new Random();
        int randIndex = r.nextInt(10 - 0 + 1);

        tv.setText(thoughts.get(randIndex));

    }

    public void SOS(View view) {

        String sosNumber = getStringFromSP(Constants.SOS_NUMBER);
        if (sosNumber.length() < 10)
            sosNumber = getString(R.string.defaultSosNumber);
        String sosText = getStringFromSP(Constants.SOS_TEXT);
        if (sosText.length() < 10)
            sosText = getString(R.string.defaultSosText);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sosNumber, null, sosText, pi, null);
        int sentSmsCount = getIntFromSP(Constants.SENTSMSCOUNT);
        sentSmsCount++;
        saveInSp(Constants.SENTSMSCOUNT, sentSmsCount);

        Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sosNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            startActivity(phoneIntent);
            return;
        }


    }

    public void showNotification() {

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Intent intent2 = new Intent(this, SettingsActivity.class);
        PendingIntent pIntent2 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification mNotification = new Notification.Builder(context)

                .setContentTitle("DriveSafe!")
                .setContentText("Safe mode is on!")
                .setSmallIcon(R.drawable.car)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_off, "Settings", pIntent2)
                .addAction(0, "View", pIntent)
                .setOngoing(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (getFromSP(Constants.NOTIFICATION_ALLOW))
            notificationManager.notify(0, mNotification);
    }

    public void cancelNotification(int notificationId) {

        if (Context.NOTIFICATION_SERVICE != null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) context.getApplicationContext().getSystemService(ns);
            nMgr.cancel(notificationId);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_call) {
            Intent intent = new Intent(this, CallLogging.class);
            startActivity(intent);

        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, "koolestnitesh@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for SafeDrive");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi, We love your App. (send to:koolestnitesh@gmail.com)" + " \n Info :" + getPackageName() +
                    " " + getApplicationInfo());

            startActivity(Intent.createChooser(intent, "Send Email"));

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private class AsyncTaskForPermissions extends AsyncTask<String, Void, Void> {

        private String resp;

        @Override
        protected Void doInBackground(String... params) {
            publishProgress(); // Calls onProgressUpdate()
            try {
                // Do your long operations here and return the result
                int time = Integer.parseInt(params[0]);
                // Sleeping for given time period
                Thread.sleep(time);
                resp = "Slept for " + time + " milliseconds";
                checkPermissions();
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }

            return null;
        }

        public void checkPermissions() {
            checkCallPermissions();
            checkSmsPermissions();
            checkCallLogPermissions();
        }

        public void checkCallLogPermissions() {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CALL_LOG)
                    != PackageManager.PERMISSION_GRANTED) {

                requestCallLogPermission();
            } else {
                Log.i(TAG,
                        "CALL Log permission has already been granted.");
            }

        }

        public void checkCallPermissions() {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestCallPermission();
            } else {
                Log.i(TAG,
                        "CALL permission has already been granted.");
            }
        }

        public void checkSmsPermissions() {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestSendSmsPermission();
            } else {
                Log.i(TAG,
                        "Sms permission has already been granted.");
            }
        }

        private void requestCallPermission() {
            Log.i(TAG, "Call permission has NOT been granted. Requesting permission.");

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(),
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.READ_PHONE_STATE},
                        0);
            }
        }

        private void requestCallLogPermission() {
            Log.i(TAG, "Call permission has NOT been granted. Requesting permission.");

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(),
                    Manifest.permission.READ_CALL_LOG)) {

            } else {
                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.READ_CALL_LOG},
                        0);
            }
        }

        private void requestSendSmsPermission() {
            Log.i(TAG, "Send Sms permission has NOT been granted. Requesting permission.");

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(),
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.SEND_SMS},
                        3);
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */


        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */

    }
}

