package io.github.arpankapoor.verifymobilenumber;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;

import io.github.arpankapoor.country.Country;

public class MainActivity extends AppCompatActivity {

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

    private void setCountryButtonText(String countryButtonText) {
        Button countryButton = (Button) findViewById(R.id.countryButton);
        countryButton.setText(countryButtonText);
    }

    private void setCountryButtonTextFromSimCountry() {
        Country country = new Country(getSimCountryIso());
        setCountryButtonText(country.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setAppNameInCarrierWarningTextView();
        setCountryButtonTextFromSimCountry();
    }
}
