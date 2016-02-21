package com.climate.farmr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

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
    public User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        try {
            session = new JSONObject((String) getIntent().getExtras().get("SessionToken"));
            lat = getIntent().getExtras().getDouble("Lat");
            log = getIntent().getExtras().getDouble("Long");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue = Volley.newRequestQueue(this);
        getUserInfo();
        WebView userProfileWebView = (WebView)this.findViewById(R.id.userProfileWebView);
        userProfileWebView.getSettings().setJavaScriptEnabled(true);
        userProfileWebView.addJavascriptInterface(new UserProfileJavaScriptInterface(this, userProfileWebView), "MyHandler");
        userProfileWebView.loadUrl("file:///android_asset/webviews/farm_profile/profile.html");
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
