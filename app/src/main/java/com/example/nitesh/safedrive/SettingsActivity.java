package com.example.nitesh.safedrive;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
/**
 * Created by nitesh on 25-08-2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Switch s1 = (Switch) findViewById(R.id.notify);
        s1.setChecked(getBooleanFromSP(Constants.NOTIFICATION_ALLOW));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void smsText(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter SMS String");
        final EditText input = new EditText(this);
        input.setText(getStringFromSP(Constants.MESSAGE));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String smsTextString = input.getText().toString();
                if(smsTextString.length()<5)
                    smsTextString = getString(R.string.sms_message);
                saveInSp(Constants.MESSAGE,smsTextString);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void sosNumber(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Sos Number");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setText(getStringFromSP(Constants.SOS_NUMBER));
       if(input.getText().length()<10)
           input.setText(getString(R.string.defaultSosNumber));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sosNumberString = input.getText().toString();
                if(sosNumberString.length()<10)
                    sosNumberString = getString(R.string.defaultSosNumber);
                saveInSp(Constants.SOS_NUMBER,sosNumberString);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void sosText(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter SOS String");
        final EditText input = new EditText(this);
        input.setText(getStringFromSP(Constants.SOS_TEXT));
        if(input.getText().length()<5)
            input.setText(getString(R.string.defaultSosText));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sosTextString = input.getText().toString();
                if(sosTextString.length()<5)
                    sosTextString = getString(R.string.defaultSosText);
                saveInSp(Constants.SOS_TEXT,sosTextString);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void autoStart(final View view)
    {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time =selectedHour + ":" + selectedMinute;
                TextView tv = (TextView) view;
                tv.setText("Auto Start Time : "+time);
                saveInSp(Constants.AUTO_STAR_TIME,time);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    public void TurnOffNotification(View view)
    {
        Switch s1 = (Switch) view;
        if(s1.isChecked())
        {
            saveInSp(Constants.NOTIFICATION_ALLOW,true);
        }
        else
        {
            saveInSp(Constants.NOTIFICATION_ALLOW,false);
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) this.getApplicationContext().getSystemService(ns);
            nMgr.cancel(0);
        }

    }
    private String getStringFromSP(String key) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }
    private Boolean getBooleanFromSP(String key) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key,true);
    }

    private void saveInSp(String key, String  value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    private int getIntFromSP(String key) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getInt(key,0);
    }

    private void saveIntInSp(String key, int  value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    private void saveInSp(String key, boolean  value) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
