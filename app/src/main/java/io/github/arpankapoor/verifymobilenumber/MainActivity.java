package io.github.arpankapoor.verifymobilenumber;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.arpankapoor.country.Country;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_COUNTRY_REQUEST = 1;
    private Button countryButton = null;

    private void setAppNameInCarrierWarningTextView() {
        Resources resources = getResources();
        String carrierChargesWarning = String.format(
                resources.getString(R.string.carrier_charges_warning),
                resources.getString(R.string.app_name)
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
        if (countryButton == null) {
            countryButton = (Button) findViewById(R.id.countryButton);
        }
        return countryButton;
    }

    private void setCountryButtonText(String countryButtonText) {
        Button countryButton = getCountryButton();
        countryButton.setText(countryButtonText);
    }

    private void setCountryButtonTextFromSimCountry() {
        Country country = new Country(getSimCountryIso());
        setCountryButtonText(country.toString());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setAppNameInCarrierWarningTextView();
        setCountryButtonTextFromSimCountry();
        setCountryButtonOnClickListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_COUNTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                String country = data.getStringExtra("country");
                setCountryButtonText(country);
            }
        }
    }
}
