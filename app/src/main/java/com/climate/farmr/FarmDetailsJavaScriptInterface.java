package com.climate.farmr;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.climate.farmr.domain.Soil;

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
    public String getAcres() {
        return parentActivity.farm.getAcres();
    }

    @JavascriptInterface
    public String getCentroidLatitude() {
        return String.valueOf(parentActivity.farm.getCentroid().getLat());
    }
    @JavascriptInterface
    public String getCentroidLongitude() {
        return String.valueOf(parentActivity.farm.getCentroid().getLog());
    }
    @JavascriptInterface
    public String getFarmNumber() {
        return String.valueOf(parentActivity.farm.getFarmNumber());
    }
    @JavascriptInterface
    public String getCounty() {
        return parentActivity.farm.getCounty();
    }

    @JavascriptInterface
    public String getState() {
        return parentActivity.farm.getState();
    }

    @JavascriptInterface
    public String getSoilType() {
        return parentActivity.farm.getDefaultSoilType();
    }

    @JavascriptInterface
    public String getGeometryType() {
        return parentActivity.farm.getGeometryType();
    }

    @JavascriptInterface
    public String getSoilList() {
        String soilList="";
        for(Soil s : parentActivity.farm.getSoils()){
            soilList+=s.getFieldProportion()+";"+s.getName()+";"+s.getHydrogroup()+";"+s.getQuatity();
            soilList+="+";
        }
        return soilList;
    }
}
