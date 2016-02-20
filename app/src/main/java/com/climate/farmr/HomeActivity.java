package com.climate.farmr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.climate.farmr.domain.Farm;
import com.climate.farmr.domain.Point;
import com.climate.farmr.domain.Soil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    JSONObject session = null;
    double lat, log;
    TextView test = null;
    private Object details;
    private static final String TAG = "hmc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        test = (TextView) findViewById(R.id.test);
        setSupportActionBar(toolbar);
        try {
            session = new JSONObject((String) getIntent().getExtras().get("SessionToken"));
            lat = getIntent().getExtras().getDouble("Lat");
            log = getIntent().getExtras().getDouble("Long");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TypefaceProvider.registerDefaultIconSets();
    }


    public void showFarms(View view) {
        Intent intent = new Intent(HomeActivity.this, FarmsActivity.class);
        Log.d(TAG, "Button clicked!!");
        intent.putExtra("SessionToken", session.toString());
        intent.putExtra("Lat", lat);
        intent.putExtra("Long", log);
        startActivity(intent);
    }
}
