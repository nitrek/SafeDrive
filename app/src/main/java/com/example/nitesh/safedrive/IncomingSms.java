package com.example.nitesh.safedrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
                    /*TextToSpeech t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                       @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                try {
                                    this.setLanguage(Locale.UK);
                                }
                                catch (Exception e)
                                {

                                }
                            }
                        }
                    });
                    t1.speak(message, TextToSpeech.QUEUE_FLUSH, null);*/
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
