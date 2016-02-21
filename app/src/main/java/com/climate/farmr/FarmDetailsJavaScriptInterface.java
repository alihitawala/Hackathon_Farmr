package com.climate.farmr;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by ashishvshenoy on 2/20/16.
 */
public class FarmDetailsJavaScriptInterface {
        protected FarmDetailActivity parentActivity;
        protected WebView mWebView;

        public FarmDetailsJavaScriptInterface(FarmDetailActivity _activity, WebView _webView)  {
            parentActivity = _activity;
            mWebView = _webView;

        }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(parentActivity, toast, Toast.LENGTH_SHORT).show();
    }


    /** Show a toast from the web page */
    @JavascriptInterface
    public String getValues() {
        return "100";
    }
}
