package com.example.myreactionanalizer;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class WebAppInterface2 {

    private Context mContext;
    private FindActivity activity;
    private FindViewHolder fvh = new FindViewHolder();
    private String text = "";
    // Instantiate the interface and set the context
    WebAppInterface2(Context c, FindActivity f) {
        mContext = c;
        activity = f;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    public void setText(String text) {
        this.text = text;
    }
    @JavascriptInterface
    public String getInput() {
        return text;
    }

    @JavascriptInterface
    public void setResult(String result) throws JSONException {
        fvh.resultImg = (ImageView)activity.findViewById(R.id.resultImg);
        fvh.resultText = (TextView)activity.findViewById(R.id.resultText);

        JSONObject jo = new JSONObject(result);
        final String s = jo.getString("text");
        final String te = "Таны одоогийн байдал '"+s+"' байна.";
        fvh.resultText.setText(te);

        new Thread(new Runnable() {
            public void run(){

                if(s.equalsIgnoreCase("Эерэг")) {
                    fvh.resultImg.setImageResource(R.mipmap.love);
                } else if(s.equalsIgnoreCase("Сөрөг")) {
                    fvh.resultImg.setImageResource(R.mipmap.like);
                } else {
                    fvh.resultImg.setImageResource(R.mipmap.sad);
                }
            }
        }).start();
    }
}
