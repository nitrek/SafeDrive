package com.example.nitesh.safedrive;

import android.Manifest;
import android.content.Intent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
    public static final String DRIVE_MODE = "drivemode";
    public static final String DRIVETIME = "drivetime";
    public static final String AUTO_SMS = "autosms";
    public static final String CALL_LOG = "calllog";
    public static final String AUTO_CALL = "autocall";
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


        checkPermissions();
        restoreCheckBoxState();
        showThought();

    }

    public void restoreCheckBoxState() {

        ToggleButton toggleButton;
        Switch s1, s2, s3;

        toggleButton = (ToggleButton) findViewById(R.id.driveModeToggle);
        toggleButton.setChecked(getFromSP(DRIVE_MODE));
        driveToggle(toggleButton);

        s1 = (Switch) findViewById(R.id.autoCall);
        s1.setChecked(getFromSP(AUTO_CALL));
        autoCall(s1);

        s2 = (Switch) findViewById(R.id.autoSms);
        s2.setChecked(getFromSP(AUTO_SMS));
        autoSms(s2);

    }

    public void checkPermissions() {
        checkCallPermissions();
        checkSmsPermissions();
        checkCallLogPermissions();
    }

    public void checkCallLogPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {

            requestCallLogPermission();
        } else {
            Log.i(TAG,
                    "CALL Log permission has already been granted.");
        }

    }

    public void checkCallPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            requestCallPermission();
        } else {
            Log.i(TAG,
                    "CALL permission has already been granted.");
        }
    }

    public void checkSmsPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            requestSendSmsPermission();
        } else {
            Log.i(TAG,
                    "Sms permission has already been granted.");
        }
    }

    private void requestCallPermission() {
        Log.i(TAG, "Call permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    0);
        }
    }

    private void requestCallLogPermission() {
        Log.i(TAG, "Call permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CALL_LOG)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG},
                    0);
        }
    }

    private void requestSendSmsPermission() {
        Log.i(TAG, "Send Sms permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.SEND_SMS)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    3);
        }
    }

    public void autoSms(View view) {
        Switch smsToggleButton = (Switch) view;
        saveInSp(AUTO_SMS, smsToggleButton.isChecked());
        if (smsToggleButton.isChecked()) {
            smsToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            smsToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void autoCall(View view) {
        Switch callToggleButton = (Switch) view;
        saveInSp(AUTO_CALL, callToggleButton.isChecked());
        if (callToggleButton.isChecked()) {
            callToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            callToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void callLog(View view) {
        Switch callLogToggleButton = (Switch) view;
        saveInSp(CALL_LOG, callLogToggleButton.isChecked());
        if (callLogToggleButton.isChecked()) {
            callLogToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            callLogToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void addSwitches() {
        Switch s1 = (Switch) findViewById(R.id.autoSms);
        Switch s2 = (Switch) findViewById(R.id.autoCall);
        ImageView im = (ImageView) findViewById(R.id.relax);
        im.setVisibility(View.GONE);
        s1.setVisibility(View.VISIBLE);
        s2.setVisibility(View.VISIBLE);


    }

    public void removeSwitches() {
        Switch s1 = (Switch) findViewById(R.id.autoSms);
        Switch s2 = (Switch) findViewById(R.id.autoCall);
        ImageView im = (ImageView) findViewById(R.id.relax);

        s1.setVisibility(View.GONE);
        s2.setVisibility(View.GONE);
        im.setVisibility(View.VISIBLE);
    }

    public void driveToggle(View view) {
        ToggleButton driveToggleButton = (ToggleButton) view;
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        saveInSp(DRIVE_MODE, driveToggleButton.isChecked());
        TextView textView = (TextView) findViewById(R.id.stats);
        if (driveToggleButton.isChecked()) {
            driveToggleButton.setText("Drive Mode:On");
            relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            showNotification();
            addSwitches();
            AudioManager am;
            am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int previousState = am.getRingerMode();
            Toast.makeText(this, "previous state" + previousState, Toast.LENGTH_LONG).show();
            saveInSp(IncomingCall.previousRingingState, previousState);
            saveInSp(DRIVETIME, System.currentTimeMillis());
            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            showThought();

            textView.setVisibility(View.GONE);

        } else {
            cancelNotification(0);
            driveToggleButton.setText("Drive Mode:Off");
            relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            Switch cb2, cb3;
            cb2 = (Switch) findViewById(R.id.autoCall);
            cb3 = (Switch) findViewById(R.id.autoSms);
            cb2.setChecked(false);
            cb3.setChecked(false);
            removeSwitches();
            String stats = "In Last Drive Session \nCalls:" + getIntFromSP(IncomingCall.CALLCOUNT) + " SMSSent :" + getIntFromSP(IncomingCall.SENTSMSCOUNT)
                    + " SmsRecivedCount: " + getIntFromSP(IncomingSms.SMSCOUNT);
            textView.setText(stats);
            saveInSp(IncomingCall.CALLCOUNT, 0);
            saveInSp(IncomingCall.SENTSMSCOUNT, 0);
            saveInSp(IncomingSms.SMSCOUNT, 0);
            textView.setVisibility(View.VISIBLE);
            showThought();
            AudioManager am2;
            am2 = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            Toast.makeText(this, "previous state now" + getIntFromSP(IncomingCall.previousRingingState), Toast.LENGTH_LONG).show();

            am2.setRingerMode(getIntFromSP(IncomingCall.previousRingingState));

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
        return preferences.getInt(key, AudioManager.RINGER_MODE_NORMAL);
    }

    private Long getLongFromSP(String key) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getLong(key, AudioManager.RINGER_MODE_NORMAL);
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

    public void showNotification() {

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Intent intent2 = new Intent(this, SettingsActivity.class);
        PendingIntent pIntent2 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification mNotification = new Notification.Builder(context)

                .setContentTitle("DriveSafe!")
                .setContentText("Safe mode is on!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_off, "Settings", pIntent2)
                .addAction(0, "View", pIntent)
                .setOngoing(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
}
