package com.climate.farmr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.climate.login.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity
		implements LoginButton.LoginListener, ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private static final String TAG = "tcc";
	private double lat, log;
	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LoginButton loginButton = (LoginButton) findViewById(R.id.login);
		loginButton.setCredentials("dpi808t3vd530f", "lbvkvpldao592rsvb9cem9qde7");
		loginButton.registerListener(this);
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}
	}

	@Override
	public void onLogin(final JSONObject session) {
		Log.d(TAG, "onLogin");

		final JsonObjectRequest jsObjRequest = new JsonObjectRequest
				(Request.Method.GET, "https://hackillinois.climate.com/api/fields", null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						//go to home page
						Log.d(TAG, "fields: ");
						try {
							Log.d(TAG, response.toString(2));
							if (response.has("error")) {
								Log.e(TAG, "ERROR _ MAIN");
							} else {
								JSONArray fields = response.optJSONArray("fields");
								StringBuilder stringBuilder = new StringBuilder("Field:\n\n");
								for (int i = 0; i < fields.length(); ++i) {
									stringBuilder.append(fields.optJSONObject(i).optString("name"));
									stringBuilder.append("\n");
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent(MainActivity.this, HomeActivity.class);
						intent.putExtra("SessionToken", session.toString());
						intent.putExtra("Lat", lat);
						intent.putExtra("Long", log);
						startActivity(intent);
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
				Set<String> strings = map.keySet();
				Log.d(TAG, map.get("Authorization") + "**********************\n\n");
				for(String s : strings){
					Log.d(TAG, map.get(s) + "\n");
				}
				Log.d(TAG, map.get("Authorization") + "**********************\n\n");
				return map;
			}
		};
		RequestQueue queue = Volley.newRequestQueue(this);
		queue.add(jsObjRequest);
	}

	@Override
	public void onError(Exception exception) {
		Log.d(TAG, "onError");
	}

	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(Bundle bundle) {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		if (mLastLocation != null) {
			lat = mLastLocation.getLatitude();
			log = mLastLocation.getLongitude();
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}
}
