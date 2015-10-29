package io.github.arpankapoor.verifymobilenumber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.github.arpankapoor.country.Country;

public class CountrySelector extends AppCompatActivity {

    private List<String> getCountryList() {
        List<String> countries = new ArrayList<>();

        Set<String> supportedRegions = PhoneNumberUtil.getInstance().getSupportedRegions();

        for (String countryIso : supportedRegions) {
            Country country = new Country(countryIso);
            countries.add(country.toString());
        }

        Collections.sort(countries);
        return countries;
    }

    private void setListView() {
        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(new ArrayAdapter<>(
                this,
                R.layout.textview_country_list,
                getCountryList()
        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String country = (String) parent.getItemAtPosition(position);

                Intent output = new Intent();
                output.putExtra("country", country);
                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setListView();
    }
}