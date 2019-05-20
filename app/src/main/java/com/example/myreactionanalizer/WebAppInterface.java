package com.example.myreactionanalizer;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class WebAppInterface {

    Context mContext;
    private ArrayList<JSONObject> postData;
    private ArrayList<JSONObject> chartData;

    @JavascriptInterface
    public int checkLoadedData() {
        if(this.postData != null && this.postData.size() > 0 && this.chartData != null && this.chartData.size() > 0) {
            Log.d("CHART", chartData.get(1).toString());
            return 1;
        }
        else
            return 0;
    }

    @JavascriptInterface
    public String getPostData() {
        String result = "[";
        for(int i = 0; i <postData.size(); i++) {

            if(postData.size() - 1 == i)
                result += postData.get(i).toString();
            else
                result += postData.get(i).toString() + ",";

        }
        result += "]";
        return result;
    }

    public void setPostData(ArrayList<JSONObject> content) {
        this.postData = content;
    }

    @JavascriptInterface
    public String getChartData() {

        String result = "[";
        for(int i = 0; i <chartData.size(); i++) {

            if(chartData.size() - 1 == i)
                result += chartData.get(i).toString();
            else
                result += chartData.get(i).toString() + ",";

        }
        result += "]";
        return result;
    }

    public void setChartData(ArrayList<JSONObject> chartData) {
        this.chartData = chartData;
    }

    // Instantiate the interface and set the context
    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public String getFbId() {
        return mContext.getResources().getString(R.string.facebook_app_id);
    }

}
