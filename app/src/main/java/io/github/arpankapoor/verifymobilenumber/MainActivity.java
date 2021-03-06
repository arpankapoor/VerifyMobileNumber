package io.github.arpankapoor.verifymobilenumber;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import io.github.arpankapoor.country.Country;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_COUNTRY_REQUEST = 1;
    private Button countryButton = null;
    private EditText phoneNumberEditText = null;

    private void setAppNameInCarrierWarningTextView() {
        String carrierChargesWarning = String.format(
                getString(R.string.carrier_charges_warning),
                getString(R.string.app_name)
        );

        TextView carrierChargesTextView = (TextView) findViewById(R.id.carrierChargesTextView);
        carrierChargesTextView.setText(carrierChargesWarning);
    }

    private String getSimCountryIso() {
        TelephonyManager telephonyManager = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimCountryIso().toUpperCase();
    }

    private Button getCountryButton() {
        if (this.countryButton == null) {
            this.countryButton = (Button) findViewById(R.id.countryButton);
        }
        return this.countryButton;
    }

    private void setCountryButton(Country country) {
        Button countryButton = getCountryButton();
        countryButton.setTag(country);
        countryButton.setText(country.toString());
    }

    private void setCountryButtonTextFromSimCountry() {
        Country country = new Country(getSimCountryIso());
        setCountryButton(country);
    }

    private EditText getPhoneNumberEditText() {
        if (this.phoneNumberEditText == null) {
            this.phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        }
        return this.phoneNumberEditText;
    }

    private void setCountryButtonOnClickListener() {
        Button countryButton = getCountryButton();
        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent countrySelectorIntent = new Intent(v.getContext(), CountrySelector.class);
                startActivityForResult(countrySelectorIntent, PICK_COUNTRY_REQUEST);
            }
        });
    }

    private String getPhoneNumberText() {
        EditText phoneNumberEditText = getPhoneNumberEditText();
        return phoneNumberEditText.getText().toString();
    }

    private Country getCountry() {
        Button countryButton = getCountryButton();
        return (Country) countryButton.getTag();
    }

    private String getFormattedPhoneNumber() {
        String phoneNumberText = getPhoneNumberText();
        Country country = getCountry();

        String formattedPhoneNumber = null;
        Phonenumber.PhoneNumber phoneNumber;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            phoneNumber = phoneNumberUtil.parse(phoneNumberText, country.getIsoCode());
            if (phoneNumberUtil.isValidNumber(phoneNumber)) {
                formattedPhoneNumber = phoneNumberUtil.format(phoneNumber,
                        PhoneNumberUtil.PhoneNumberFormat.E164);
            }
        } catch (NumberParseException npe) {
            formattedPhoneNumber = null;
        }

        return formattedPhoneNumber;
    }

    private void invalidPhoneNumberAlert(View v) {
        new AlertDialog.Builder(v.getContext())
                .setMessage(v.getResources().getString(R.string.enter_phone_number))
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .show();
    }

    private void setOkButtonOnClickListener() {
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Phone Number not entered
                if (getPhoneNumberText().equals("")) {
                    invalidPhoneNumberAlert(v);
                } else {
                    String phoneNumber = getFormattedPhoneNumber();

                    // Phone Number invalid
                    if (phoneNumber == null) {
                        invalidPhoneNumberAlert(v);
                    } else {
                        Intent verificationIntent = new Intent(v.getContext(),
                                VerificationStatusActivity.class);
                        verificationIntent.putExtra(getString(R.string.saved_phone_number_key),
                                phoneNumber);
                        startActivity(verificationIntent);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file), MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(
                getString(R.string.saved_phone_number_key), null);
        boolean isVerified = (phoneNumber != null);

        if (isVerified) {
            Intent verificationIntent = new Intent(this, VerificationStatusActivity.class);
            startActivity(verificationIntent);
            finish();
        } else {
            setAppNameInCarrierWarningTextView();
            setCountryButtonTextFromSimCountry();
            setCountryButtonOnClickListener();
            setOkButtonOnClickListener();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_COUNTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Country country = (Country) data.getSerializableExtra("country");
                setCountryButton(country);
            }
        }
    }
}
