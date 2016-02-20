package com.climate.farmr;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class FarmsActivity extends FragmentActivity implements OnMapReadyCallback {

    JSONObject session = null;
    private GoogleMap mMap;
    double lat, log;
    private static final String TAG = "Farms_Activity";
    private List<Farm> farms = new ArrayList<>();
    private List<Farm> topFarms = new ArrayList<>();
    SupportMapFragment mapFragment;
    LatLng currentLocation;
    HashMap<String, Farm> farmNumberMap = new HashMap<String, Farm>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                                    farmNumberMap.put(farm.getFarmNumber(), farm);
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
            MarkerOptions mo = new MarkerOptions().position(new LatLng(f.getCentroid().getLat(), f.getCentroid().getLog()))
                    .title(f.getCounty() + " - " + f.getFarmNumber())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.plantation));;
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
        PriorityQueue<Farm> priorityQueue = new PriorityQueue<>(10, new Comparator<Farm>() {
            @Override
            public int compare(Farm lhs, Farm rhs) {
                return (int) (rhs.getValue() - lhs.getValue());
            }
        });
        for (Farm f : farms) {
            double value = 0;
            double acre = Double.parseDouble(f.getAcres());
            for (Soil soil : f.getSoils()) {
                double acreProportion = Double.parseDouble(soil.getFieldProportion());
                for (String crop : CropInformation.crops) {
                    if (CropInformation.cropToSoilType.get(crop).contains(soil.getName())) {
                        double temp = acre * acreProportion * CropInformation.cropToYieldPerAcre.get(crop);
                        value += temp;
                        if (f.getCropValueMap().containsKey(crop)) {
                            temp += f.getCropValueMap().get(crop);
                        }
                        f.putCropValue(crop, temp);
                    }
                }
                value += HydrocarbonInformation.valueInfo.get(soil.getHydrogroup()) * acreProportion;
            }
            f.setDistance(compareDistanceWithCurrent(f.getCentroid().getLat(), f.getCentroid().getLog()));
            f.setValue(value - (f.getDistance() * 0.01));
            priorityQueue.add(f);
        }
        ArrayList<Farm> nearestFarms = new ArrayList<Farm>();
        for (int i=0;i<10;i++) {
            Farm contender = priorityQueue.remove();
            nearestFarms.add(contender);
            Log.d(TAG, "TOP FARM :::: " + contender.getValue());
            Log.d(TAG, "TOP FARM DISTANCE :::: " + contender.getDistance());
        }
        return nearestFarms;
    }

    public float compareDistanceWithCurrent(double destinationLat, double destination_long) {
        Location locationA = new Location("point A");
        locationA.setLatitude(lat);
        locationA.setLongitude(log);
        Location locationB = new Location("point B");
        locationB.setLatitude(destinationLat);
        locationB.setLongitude(destination_long);
        return locationA.distanceTo(locationB);
    }

    public void changeView(Farm f) {
        setContentView(R.layout.farm_details);
        TextView acres, farmNumber, county, state;
        acres = (TextView) findViewById(R.id.acres);
        farmNumber = (TextView) findViewById(R.id.farm_number);
        county = (TextView) findViewById(R.id.county);
        state = (TextView) findViewById(R.id.state);

        acres.setText(f.getAcres());
        farmNumber.setText(f.getFarmNumber());
        county.setText(f.getCounty());
        state.setText(f.getState());
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Farms Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.climate.farmr/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Farms Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.climate.farmr/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}