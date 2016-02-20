package com.climate.farmr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        getDetails();
    }

    public void getDetails() {
        Log.d(TAG, "getting details...");
        double ne_lat = lat + 0.05;
        double ne_log = log + 0.05;
        double sw_lat = lat - 0.05;
        double sw_log = log - 0.05;
        String s = "https://hackillinois.climate.com/api/clus?ne_lon="+ne_log+"&ne_lat="+ne_lat+"&sw_lon="+sw_log+"&sw_lat="+sw_lat;
        Log.d(TAG, s + "    ***********************");
        final List<Farm> farms = new ArrayList<>();
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, s, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, response.toString(2));
                            if (response.has("error")) {
                                test.setText(response.optString("error_description"));
                            } else {
                                JSONArray features = response.optJSONArray("features");
                                for (int i = 0; i < features.length(); ++i) {
                                    Farm farm = new Farm();
                                    JSONObject geo = features.optJSONObject(i).optJSONObject("geometry");
                                    JSONObject properties = features.optJSONObject(i).optJSONObject("properties");
                                    JSONArray coordinates = geo.optJSONArray("coordinates").optJSONArray(0).optJSONArray(0);
                                    for (int j=0;j<coordinates.length(); j++) {
                                        Double slog = (Double) coordinates.optJSONArray(j).opt(0);
                                        Double slat = (Double) coordinates.optJSONArray(j).opt(1);
                                        farm.getCoordinates().add(new Point(slog, slat));
                                    }
                                    farm.setTimezone(properties.optString("timezone"));
                                    farm.setTractNumber(properties.optString("tract-number"));
                                    farm.setFarmNumber(properties.optString("farm-number"));
                                    farm.setAcres(properties.optString("calc-acres"));
                                    farm.setCounty(properties.optString("county"));
                                    farm.setState(properties.optString("state"));
                                    JSONArray j_centroid = properties.optJSONArray("centroid");
                                    if (j_centroid != null) {
                                        Double slog = (Double) j_centroid.opt(0);
                                        Double slat = (Double) j_centroid.opt(1);
                                        farm.setCentroid(new Point(slog, slat));
                                    }
                                    JSONObject soil_types = properties.optJSONObject("soil-types");
                                    JSONObject sTypes = soil_types.optJSONObject("types");
                                    Iterator<String> it = sTypes.keys();
                                    while(it.hasNext()) {
                                        String sName = it.next();
                                        Soil soil = new Soil();
                                        soil.setName(sName);
                                        soil.setFieldProportion(sTypes.getJSONObject(sName).getString("field-proportion"));
                                        soil.setHydrogroup(sTypes.getJSONObject(sName).getString("hydrogroup"));
                                        soil.setQuatity(sTypes.getJSONObject(sName).getString("quantity"));
                                        soil.setUnit(sTypes.getJSONObject(sName).getString("units"));
                                        farm.getSoils().add(soil);
                                    }
                                    farms.add(farm);
                                }
                                Log.d(TAG, "SIZE FARM = " +farms.size());
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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }
}
