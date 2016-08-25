package com.example.nitesh.safedrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by nitesh on 23-08-2016.
 */

public class IncomingSms extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i(" SmsReceiver ", " senderNum: " + senderNum + "; message: " + message);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            " senderNum: " + senderNum + ", message: " + message, duration);
                    toast.show();
                    int smsCount = getIntFromSP(context,Constants.SMSCOUNT);
                    smsCount++;
                    saveInSp(Constants.SMSCOUNT,smsCount,context);


                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
    private int getIntFromSP(Context context,String key) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), android.content.Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    private void saveInSp(String key, int value,Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

}
