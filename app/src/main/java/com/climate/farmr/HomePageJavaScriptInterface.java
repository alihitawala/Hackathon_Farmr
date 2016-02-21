package com.climate.farmr;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by ashishvshenoy on 2/20/16.
 */
public class HomePageJavaScriptInterface {
    protected HomeActivity parentActivity;
    protected WebView mWebView;

    public HomePageJavaScriptInterface(HomeActivity _activity, WebView _webView)  {
        parentActivity = _activity;
        mWebView = _webView;

    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showFieldsNearBy() {
        parentActivity.showFarms();
    }

    @JavascriptInterface
    public void showFarmsNearMyFarms(String farmName) {
        parentActivity.showFarmsNearMyFarms(farmName);
    }

    @JavascriptInterface
    public void showMyProfile() {
        parentActivity.showMyProfile();
    }

    @JavascriptInterface
    public String populateDetailsDropDown() {
        return parentActivity.populateFieldDropDown();
    }
}
