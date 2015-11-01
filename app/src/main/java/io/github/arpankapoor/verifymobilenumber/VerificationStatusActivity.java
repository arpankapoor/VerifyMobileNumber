package io.github.arpankapoor.verifymobilenumber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class VerificationStatusActivity extends AppCompatActivity {

    private BroadcastReceiver smsReceiver = null;
    private String phoneNumber = null;
    private String verificationMessage = null;

    private void setStatus(boolean isVerified) {
        String statusText = getString(R.string.verification_done);
        int checkBoxVisibility = View.VISIBLE;
        int progressBarVisibility = View.INVISIBLE;

        if (!isVerified) {
            statusText = getString(R.string.verifying);
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

                    if (originatingAddress.equals(phoneNumber) &&
                            messageBody.equals(verificationMessage)) {

                        // Verified!!
                        setStatus(true);

                        // save the phone number
                        SharedPreferences sharedPreferences = getSharedPreferences(
                                getString(R.string.preference_file), MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.saved_phone_number_key), phoneNumber);
                        editor.commit();

                        // clear all the previous activities
                        Intent verifiedIntent = new Intent(context.getApplicationContext(),
                                VerificationStatusActivity.class);
                        verifiedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        verifiedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        verifiedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(verifiedIntent);
                    }
                }
            }
        };

        registerReceiver(smsReceiver,
                new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

    }

    private String generateRandomPin() {
        Random random = new Random();
        int randomInt = random.nextInt(10000);
        return String.format("%04d", randomInt);
    }

    private String generateVerificationMessage() {
        String pin = generateRandomPin();
        return String.format(getString(R.string.verification_msg),
                getString(R.string.app_name), pin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file), MODE_PRIVATE);
        this.phoneNumber = sharedPreferences.getString(
                getString(R.string.saved_phone_number_key), null);
        boolean isVerified = (phoneNumber != null);

        setStatus(isVerified);
        if (!isVerified) {
            this.phoneNumber = getIntent().getStringExtra(
                    getString(R.string.saved_phone_number_key));

            registerSmsReceiver();
            this.verificationMessage = generateVerificationMessage();
            new SmsSenderTask().execute(phoneNumber, this.verificationMessage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    private class SmsSenderTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String phoneNumber = params[0];
            String message = params[1];

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            return null;
        }
    }
}
