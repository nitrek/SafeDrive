package com.example.nitesh.safedrive;

import android.Manifest;
import android.content.Intent;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String DRIVE_MODE = "drivemode";
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
        restoreCheckBoxState();
    }

    public void restoreCheckBoxState() {

        CheckBox cb1, cb2, cb3, cb4;

        cb1 = (CheckBox) findViewById(R.id.driveModeToggle);
        cb1.setChecked(getFromSP(DRIVE_MODE));
        driveToggle(cb1);

        cb2 = (CheckBox) findViewById(R.id.autoCall);
        cb2.setChecked(getFromSP(AUTO_CALL));
        autoCall(cb2);

        cb3 = (CheckBox) findViewById(R.id.autoSms);
        cb3.setChecked(getFromSP(AUTO_SMS));
        autoSms(cb3);

        cb4 = (CheckBox) findViewById(R.id.saveLog);
        cb4.setChecked(getFromSP(CALL_LOG));
        callLog(cb4);

        checkCallPermissions();
        checkSmsPermissions();
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
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            requestReadSmsPermission();
        } else {
            Log.i(TAG,
                    "Sms permission has already been granted.");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            requestReciveSmsPermission();
        } else {
            Log.i(TAG,
                    "Sms permission has already been granted.");
        }*/
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
/*
    private void requestReadSmsPermission() {
        Log.i(TAG, "Read sms permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_SMS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    1);
        }
    }

    private void requestReciveSmsPermission() {
        Log.i(TAG, "Receive permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECEIVE_SMS)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    2);
        }
    }*/

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
        CheckBox smsToggleButton = (CheckBox) view;
        saveInSp(AUTO_SMS, smsToggleButton.isChecked());
        if (smsToggleButton.isChecked()) {
            smsToggleButton.setText("Auto Send Sms:On");
            smsToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            smsToggleButton.setText("Auto Send Sms:Off");
            smsToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void autoCall(View view) {
        CheckBox callToggleButton = (CheckBox) view;
        saveInSp(AUTO_CALL, callToggleButton.isChecked());
        if (callToggleButton.isChecked()) {
            callToggleButton.setText("Auto Silent Call:On");
            callToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            callToggleButton.setText("Auto Silent Call:Off");
            callToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void callLog(View view) {
        CheckBox callLogToggleButton = (CheckBox) view;
        saveInSp(CALL_LOG, callLogToggleButton.isChecked());
        if (callLogToggleButton.isChecked()) {
            callLogToggleButton.setText("Save Missed Call/SMS:On");
            callLogToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            callLogToggleButton.setText("Save Missed Call/SMS:Off");
            callLogToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void driveToggle(View view) {
        CheckBox driveToggleButton = (CheckBox) view;
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        saveInSp(DRIVE_MODE, driveToggleButton.isChecked());
        if (driveToggleButton.isChecked()) {
            driveToggleButton.setText("Drive Mode:On");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            showNotification();
        } else {
            cancelNotification(0);
            driveToggleButton.setText("Drive Mode:Off");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            CheckBox cb2, cb3, cb4;
            cb2 = (CheckBox) findViewById(R.id.autoCall);
            cb3 = (CheckBox) findViewById(R.id.autoSms);
            cb4 = (CheckBox) findViewById(R.id.saveLog);
            cb2.setActivated(false);
            cb3.setActivated(false);
            cb4.setActivated(false);
        }
    }

    private boolean getFromSP(String key) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    private void saveInSp(String key, boolean value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            // Handle the camera action
        } else if (id == R.id.nav_call) {

        } else if (id == R.id.nav_sms) {

        } else if (id == R.id.nav_manage) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    public void showNotification() {

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification mNotification = new Notification.Builder(context)

                .setContentTitle("DriveSafe!")
                .setContentText("Safe mode is on!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)

                .addAction(R.mipmap.ic_launcher, "Turn Off", pIntent)
                .addAction(0, "View", pIntent)

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
}
