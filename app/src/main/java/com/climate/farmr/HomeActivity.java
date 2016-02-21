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
import com.climate.farmr.domain.Field;
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
    private List<Field> fields = new ArrayList<>();
    private Object details;
    private static final String TAG = "hmc";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        test = (TextView) findViewById(R.id.test);
        queue = Volley.newRequestQueue(this);
        setSupportActionBar(toolbar);
        try {
            session = new JSONObject((String) getIntent().getExtras().get("SessionToken"));
            lat = getIntent().getExtras().getDouble("Lat");
            log = getIntent().getExtras().getDouble("Long");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getFields();
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

    public void getFields() {
        String s = "https://hackillinois.climate.com/api/fields";
        Log.d(TAG, s + "    ***********************");
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, s, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, response.toString(2));
                            if (response.has("error")) {
                                Log.e(TAG, response.optString("error_description"));
                            } else {
                                JSONArray jfields = response.optJSONArray("fields");
                                for (int i = 0; i < jfields.length(); i++) {
                                    Field field = new Field();
                                    String name = jfields.optJSONObject(i).optString("name");
                                    field.setName(name);
                                    String farmId = jfields.optJSONObject(i).optString("farmId");
                                    field.setFarmId(farmId);
                                    JSONArray centroid = jfields.optJSONObject(i).optJSONObject("centroid").optJSONArray("coordinates");
                                    if (centroid != null) {
                                        Double slog = (Double) centroid.opt(0);
                                        Double slat = (Double) centroid.opt(1);
                                        field.setCentroid(new Point(slog, slat));
                                    }
                                    fields.add(field);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                HashMap<String, String> map = new HashMap<>();
                map.putAll(headers);
                String auth = null;
                auth = "Bearer " + session.opt("access_token");
                map.put("Authorization", auth);
                return map;
            }
        };
        queue.add(jsObjRequest);
    }
}