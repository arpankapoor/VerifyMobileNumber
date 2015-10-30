package io.github.arpankapoor.verifymobilenumber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VerificationActivity extends AppCompatActivity {

    private BroadcastReceiver smsReceiver;

    private void setStatus(boolean isVerified) {
        String statusText = getResources().getString(R.string.verification_done);
        int checkBoxVisibility = View.VISIBLE;
        int progressBarVisibility = View.INVISIBLE;

        if (!isVerified) {
            statusText = getResources().getString(R.string.verifying);
            checkBoxVisibility = View.INVISIBLE;
            progressBarVisibility = View.VISIBLE;
        }

        TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        statusTextView.setText(statusText);
        checkBox.setVisibility(checkBoxVisibility);
        progressBar.setVisibility(progressBarVisibility);
    }

    /**
     * Listen for incoming SMS
     * https://developer.android.com/reference/android/provider/Telephony.Sms.Intents.html#SMS_RECEIVED_ACTION
     */
    private void registerSmsReceiver() {
        this.smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                for (SmsMessage smsMessage : smsMessages) {
                    String originatingAddress = smsMessage.getOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();
                    Log.v("origin", originatingAddress);
                    Log.v("msg", messageBody);
                }
            }
        };

        registerReceiver(smsReceiver,
                new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean isVerified = getIntent().getBooleanExtra("is_verified", false);
        String phoneNumber = getIntent().getStringExtra("phone_number");
        Log.v("ph", phoneNumber);

        setStatus(isVerified);
        if (!isVerified) {
            registerSmsReceiver();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
}
