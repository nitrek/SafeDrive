package com.example.nitesh.safedrive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String DRIVE_MODE ="drivemode";
    public static final String AUTO_SMS ="autosms";
    public static final String CALL_LOG ="calllog";
    public static final String AUTO_CALL ="autocall";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        CheckBox cb1,cb2,cb3,cb4;

        cb1 = (CheckBox)findViewById(R.id.driveModeToggle);
        cb1.setChecked(getFromSP(DRIVE_MODE));
        driveToggle(cb1);
        cb2 = (CheckBox)findViewById(R.id.autoCall);
        cb2.setChecked(getFromSP(AUTO_CALL));
        autoCall(cb2);
        cb3 = (CheckBox)findViewById(R.id.autoSms);
        cb3.setChecked(getFromSP(AUTO_SMS));
        autoSms(cb3);
        cb4 = (CheckBox)findViewById(R.id.saveLog);
        cb4.setChecked(getFromSP(CALL_LOG));
        callLog(cb4);
    }
    public void autoSms(View view)
    {
        CheckBox driveToggleButton = (CheckBox) view;
        saveInSp(AUTO_SMS,driveToggleButton.isChecked());
        if(driveToggleButton.isChecked())
        {
            driveToggleButton.setText("Auto Send Sms:On");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
        else
        {
            driveToggleButton.setText("Auto Send Sms:Off");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }
    public void autoCall(View view)
    {
        CheckBox driveToggleButton = (CheckBox) view;
        saveInSp(AUTO_CALL,driveToggleButton.isChecked());
        if(driveToggleButton.isChecked())
        {
            driveToggleButton.setText("Auto Silent Call:On");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
        else
        {
            driveToggleButton.setText("Auto Silent Call:Off");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }
    public void callLog(View view)
    {
        CheckBox driveToggleButton = (CheckBox) view;
        saveInSp(CALL_LOG,driveToggleButton.isChecked());
        if(driveToggleButton.isChecked())
        {
            driveToggleButton.setText("Save Missed Call/SMS:On");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
        else
        {
            driveToggleButton.setText("Save Missed Call/SMS:Off");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }
    public void driveToggle(View view)
    {
        CheckBox driveToggleButton = (CheckBox) view;
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.contentLayout);
        saveInSp(DRIVE_MODE,driveToggleButton.isChecked());
        if(driveToggleButton.isChecked())
        {
            driveToggleButton.setText("Drive Mode:On");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        else
        {
            driveToggleButton.setText("Drive Mode:Off");
            driveToggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }
    private boolean getFromSP(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    private void saveInSp(String key,boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
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
}
