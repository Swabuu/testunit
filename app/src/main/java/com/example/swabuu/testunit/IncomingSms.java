package com.example.swabuu.testunit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;


import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Swabuu on 2017-02-18.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    String smsc_addr = currentMessage.getServiceCenterAddress();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "THIS IS SENDER: " + senderNum + "\n THIS IS MESSAGE: " + message, duration);
                    toast.show();

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("sender_id", senderNum);
                    params.put("body", message);
                    params.put("smsc", smsc_addr);

                    client.get("http://requestb.in/1l2a5bm1", params, new TextHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String res) {
                                    // called when response HTTP status is "200 OK"
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                }
                            }
                    );



                }
            }
        } catch (Exception e) {return;}
    }
}
