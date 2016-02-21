package com.climate.farmr;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.climate.farmr.domain.Soil;

import java.text.DecimalFormat;

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
        DecimalFormat newFormat = new DecimalFormat("#.##");
        double twoDecimal =  Double.valueOf(newFormat.format(parentActivity.farm.getCentroid().getLat()));
        return String.valueOf(twoDecimal);
    }
    @JavascriptInterface
    public String getCentroidLongitude() {
        DecimalFormat newFormat = new DecimalFormat("#.##");
        double twoDecimal =  Double.valueOf(newFormat.format(parentActivity.farm.getCentroid().getLog()));
        return String.valueOf(twoDecimal);
    }
    @JavascriptInterface
    public String getFarmNumber() {
        return String.valueOf(parentActivity.farm.getFarmNumber());
    }
    @JavascriptInterface
    public String getCounty() {
        String county = parentActivity.farm.getCounty();
        if (county != null && county.length() > 5)
            county = county.substring(0,5);
        return county;
    }

    @JavascriptInterface
    public String getState() {
        return parentActivity.farm.getState();
    }

    @JavascriptInterface
    public String getPotatoYield() {
        if (!parentActivity.farm.getCropValueMap().containsKey("Potato"))
            return "Not suitable";
        DecimalFormat newFormat = new DecimalFormat("#.##");
        double twoDecimal =  Double.valueOf(newFormat.format(parentActivity.farm.getCropValueMap().get("Potato")));
        return String.valueOf(twoDecimal);
    }

    @JavascriptInterface
    public String getCornYield() {
        if (!parentActivity.farm.getCropValueMap().containsKey("Corn"))
            return "Not suitable";
        DecimalFormat newFormat = new DecimalFormat("#.##");
        double twoDecimal =  Double.valueOf(newFormat.format(parentActivity.farm.getCropValueMap().get("Corn")));
        return String.valueOf(twoDecimal);
    }

    @JavascriptInterface
    public String getSoybeanYield() {
        if (!parentActivity.farm.getCropValueMap().containsKey("Soybean"))
            return "Not suitable";
        DecimalFormat newFormat = new DecimalFormat("#.##");
        double twoDecimal =  Double.valueOf(newFormat.format(parentActivity.farm.getCropValueMap().get("Soybean")));
        return String.valueOf(twoDecimal);
    }

    @JavascriptInterface
    public String getWheatYield() {
        if (!parentActivity.farm.getCropValueMap().containsKey("Wheat"))
            return "Not suitable";
        DecimalFormat newFormat = new DecimalFormat("#.##");
        double twoDecimal =  Double.valueOf(newFormat.format(parentActivity.farm.getCropValueMap().get("Wheat")));
        return String.valueOf(twoDecimal);
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
