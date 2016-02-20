package com.climate.farmr;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.climate.farmr.domain.Farm;
import com.climate.farmr.domain.Point;
import com.climate.farmr.domain.Soil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FarmsActivity extends FragmentActivity implements OnMapReadyCallback {

    JSONObject session = null;
    private GoogleMap mMap;
    double lat, log;
    private static final String TAG = "Farms_Activity";
    private List<Farm> farms = new ArrayList<>();
    private List<Farm> topFarms = new ArrayList<>();
    SupportMapFragment mapFragment;
    LatLng currentLocation;
    HashMap<String, Farm> farmNumberMap = new HashMap<String,Farm>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.loading_screen);
        setContentView(R.layout.activity_farms);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            session = new JSONObject((String) getIntent().getExtras().get("SessionToken"));
            lat = getIntent().getExtras().getDouble("Lat");
            log = getIntent().getExtras().getDouble("Long");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        currentLocation = new LatLng(lat, log);
        getDetails();
        mapFragment.getMapAsync(this);
        //once get the farm details start the map
    }

    public void getDetails() {
        Log.d(TAG, "getting details...");
        double ne_lat = lat + 0.05;
        double ne_log = log + 0.05;
        double sw_lat = lat - 0.05;
        double sw_log = log - 0.05;
        String s = "https://hackillinois.climate.com/api/clus?ne_lon=" + ne_log + "&ne_lat=" + ne_lat + "&sw_lon=" + sw_log + "&sw_lat=" + sw_lat;
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
                                JSONArray features = response.optJSONArray("features");
                                for (int i = 0; i < features.length(); ++i) {
                                    Farm farm = new Farm();
                                    JSONObject geo = features.optJSONObject(i).optJSONObject("geometry");
                                    JSONObject properties = features.optJSONObject(i).optJSONObject("properties");
                                    JSONArray coordinates = geo.optJSONArray("coordinates").optJSONArray(0).optJSONArray(0);
                                    for (int j = 0; j < coordinates.length(); j++) {
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
                                    while (it.hasNext()) {
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
                                    farmNumberMap.put(farm.getFarmNumber(),farm);
                                }
                                Log.d(TAG, "SIZE FARM = " + farms.size());
                            }
                            topFarms = FarmsActivity.this.getNearestFiveFarms(farms);
                            FarmsActivity.this.mapFragment.getMapAsync(FarmsActivity.this);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in current location and move the camera

        Log.d(TAG, lat + " " + log + " *****");
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here!"));
        for (Farm f : topFarms) {
            Log.d(TAG, "farm marking here");
            MarkerOptions mo = new MarkerOptions().position(new LatLng(f.getCentroid().getLat(), f.getCentroid().getLog())).title(f.getCounty() + " - " + f.getFarmNumber());
            Marker marker = mMap.addMarker(mo);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                Intent intent = new Intent(FarmsActivity.this, FarmDetailsActivity.class);
                Log.d(TAG, "Marker clicked!!");
                Farm f = farmNumberMap.get(arg0.getTitle().split("-")[1].trim());
                arg0.showInfoWindow();
                Toast.makeText(FarmsActivity.this, arg0.getTitle(), Toast.LENGTH_LONG).show();
                FarmsActivity.this.changeView(f);
                return true;
            }

        });
    }

    public ArrayList<Farm> getNearestFiveFarms(List<Farm> farms) {
        TreeMap<Float, Farm> listOfDistances = new TreeMap<Float,Farm>();
        for(Farm f : farms){
            listOfDistances.put(compareDistanceWithCurrent(f.getCentroid().getLat(),f.getCentroid().getLog()),f);
        }
        ArrayList<Farm> nearestFarms = new ArrayList<Farm>();
        nearestFarms.add(listOfDistances.get(listOfDistances.keySet().toArray()[0]));
        nearestFarms.add(listOfDistances.get(listOfDistances.keySet().toArray()[1]));
        nearestFarms.add(listOfDistances.get(listOfDistances.keySet().toArray()[2]));
        nearestFarms.add(listOfDistances.get(listOfDistances.keySet().toArray()[3]));
        nearestFarms.add(listOfDistances.get(listOfDistances.keySet().toArray()[4]));
        return nearestFarms;


    }

    public float compareDistanceWithCurrent(double x1, double y1) {
        Location locationA = new Location("point A");
        locationA.setLatitude(lat);
        locationA.setLongitude(log);
        Location locationB = new Location("point B");
        locationB.setLatitude(x1);
        locationB.setLongitude(y1);
        return locationA.distanceTo(locationB) ;
    }

    public void changeView(Farm f){
        setContentView(R.layout.farm_details);
        TextView acres, farmNumber, county,state;
        acres = (TextView) findViewById(R.id.acres);
        farmNumber = (TextView) findViewById(R.id.farm_number);
        county = (TextView) findViewById(R.id.county);
        state = (TextView) findViewById(R.id.state);

        acres.setText(f.getAcres());
        farmNumber.setText(f.getFarmNumber());
        county.setText(f.getCounty());
        state.setText(f.getState());

    }
}
