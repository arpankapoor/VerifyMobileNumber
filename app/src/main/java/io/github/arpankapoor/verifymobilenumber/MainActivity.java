package io.github.arpankapoor.verifymobilenumber;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private void setAppNameInCarrierWarningText() {
        Resources resources = getResources();
        String carrierChargesWarning = String.format(
                resources.getString(R.string.carrier_charges_warning),
                resources.getString(R.string.app_name)
        );

        TextView carrierChargesTextView = (TextView) findViewById(R.id.carrierChargesTextView);
        carrierChargesTextView.setText(carrierChargesWarning);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setAppNameInCarrierWarningText();
    }
}
