package com.climate.farmr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.climate.farmr.domain.User;

import org.json.JSONException;
import org.json.JSONObject;


public class UserProfile extends AppCompatActivity {

    double lat, log;
    RequestQueue queue;
    JSONObject session = null;
    private static final String TAG = "User_Profile";
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue = Volley.newRequestQueue(this);
        getUserInfo();
    }

    public void getUserInfo() {
        try {
            JSONObject jUser = session.optJSONObject("user");
            user.setEmail(jUser.optString("email"));
            user.setLastName(jUser.optString("lastname"));
            user.setFirstName(jUser.optString("firstname"));
            user.setPhone(jUser.optString("phone"));
            user.setCity(jUser.optString("city"));
            user.setState(jUser.optString("state"));
            user.setCountry(jUser.optString("country"));
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
