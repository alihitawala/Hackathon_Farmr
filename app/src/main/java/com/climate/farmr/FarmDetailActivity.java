package com.climate.farmr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.climate.farmr.domain.Farm;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class FarmDetailActivity extends AppCompatActivity {

    JSONObject session = null;
    double lat, log;
    Farm farm = null;
    LatLng currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        try {
            session = new JSONObject((String) getIntent().getExtras().get("SessionToken"));
            lat = getIntent().getExtras().getDouble("Lat");
            log = getIntent().getExtras().getDouble("Long");
            farm = (Farm) getIntent().getSerializableExtra("Farm");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        currentLocation = new LatLng(lat, log);
        TextView acres, farmNumber, county, state;
        acres = (TextView) findViewById(R.id.acres);
        farmNumber = (TextView) findViewById(R.id.farm_number);
        county = (TextView) findViewById(R.id.county);
        state = (TextView) findViewById(R.id.state);

        acres.setText(farm.getAcres());
        farmNumber.setText(farm.getFarmNumber());
        county.setText(farm.getCounty());
        state.setText(farm.getState());
    }

}
