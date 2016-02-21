package com.climate.farmr;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


/**
 * Created by ashishvshenoy on 2/21/16.
 */
public class UserProfileJavaScriptInterface {
    protected UserProfile parentActivity;
    protected WebView mWebView;

    public UserProfileJavaScriptInterface(UserProfile _activity, WebView _webView)  {
        parentActivity = _activity;
        mWebView = _webView;

    }


    /** Show a toast from the web page */
    @JavascriptInterface
    public String getUserEmail() {
        return parentActivity.user.getEmail();
    }

    @JavascriptInterface
    public String getUserLastName() {
        return parentActivity.user.getLastName();
    }

    @JavascriptInterface
        public String getUserFirstName() {
        return parentActivity.user.getFirstName();
    }

    @JavascriptInterface
        public String getUserPhone() {
        return parentActivity.user.getPhone();
    }

    @JavascriptInterface
        public String getUserCity() {
        Log.d("UP", "Hello from the get User City :" + parentActivity.user.getFirstName());
        return parentActivity.user.getCity();
    }


    @JavascriptInterface
    public String getUserCountry() {
        return parentActivity.user.getCountry();
    }
}
