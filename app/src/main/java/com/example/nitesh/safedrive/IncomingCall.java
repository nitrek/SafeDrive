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

public class IncomingCall extends BroadcastReceiver {
    private  String LOG_TAG ="SAFEDRIVE";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            Toast.makeText(context,state,Toast.LENGTH_LONG).show();
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Toast.makeText(context, "Ringing State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
              if(getFromSP(context,HomeActivity.DRIVE_MODE))
                AudioManager am;
                am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//For Silent mode
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);

//For Vibrate mode
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(incomingNumber,null,"msg",pi,null);
                Toast.makeText(context, "Sms sent Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();

            }
            else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {
                Toast.makeText(context, "OffHook State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
                AudioManager am;
                am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
            else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context, "Idle State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
                AudioManager am;
                am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean getFromSP(Context context,String key){
        SharedPreferences preferences =context.getSharedPreferences(context.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    private void saveInSp(Context context,String key,boolean value){
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}

