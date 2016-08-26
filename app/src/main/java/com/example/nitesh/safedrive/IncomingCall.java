package com.example.nitesh.safedrive;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;
/**
 * Created by nitesh on 24-08-2016.
 */
public class IncomingCall extends BroadcastReceiver {
    private String LOG_TAG = "SAFEDRIVE";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            AudioManager am;
            am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int previousState = am.getRingerMode();
            saveInSp(context,Constants.previousRingingStateOncall,previousState);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Toast.makeText(context, state, Toast.LENGTH_LONG).show();
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Toast.makeText(context, context.getString(R.string.ringing_state) + incomingNumber, Toast.LENGTH_SHORT).show();
                if (getBoolFromSP(context, Constants.AUTO_CALL))
                    if (getBoolFromSP(context, Constants.DRIVE_MODE)) {
/*
//For Silent mode
                        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
*/
//For Vibrate mode
                        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                int callCount = getIntFromSP(context,Constants.CALLCOUNT);
                callCount++;
                saveInSp(Constants.CALLCOUNT,callCount,context);
                int sentSmsCount = getIntFromSP(context,Constants.SENTSMSCOUNT);
                if (getBoolFromSP(context, Constants.AUTO_SMS)) {
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
                    SmsManager smsManager = SmsManager.getDefault();
                    String smsTextString = getStringFromSP(context,Constants.MESSAGE);
                    if(smsTextString.length() <5)
                        smsTextString = context.getString(R.string.sms_sent);
                    smsManager.sendTextMessage(incomingNumber, null,smsTextString, pi, null);
                    Toast.makeText(context, context.getString(R.string.sms_sent) + incomingNumber, Toast.LENGTH_SHORT).show();
                    sentSmsCount++;
                    saveInSp(Constants.SENTSMSCOUNT,sentSmsCount,context);
                }

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                Toast.makeText(context, context.getString(R.string.offhook_state), Toast.LENGTH_SHORT).show();
                AudioManager am1;
                am1 = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am1.setRingerMode(getIntFromSP(context,Constants.previousRingingStateOncall));
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Toast.makeText(context, context.getString(R.string.idle_state), Toast.LENGTH_SHORT).show();
                AudioManager am2;
                am2 = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am2.setRingerMode(getIntFromSP(context,Constants.previousRingingStateOncall));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean getBoolFromSP(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    private int getIntFromSP(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getInt(key, AudioManager.RINGER_MODE_NORMAL);
    }
    private String getStringFromSP(Context context,String key) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }


    private void saveInSp(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }
    private void saveInSp(String key, int value,Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}

