package com.example.myreactionanalizer;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    @Override
    public void onPageFinished (WebView view, String url) {
        //Calling a javascript function in html page
        view.loadUrl("javascript:alert(showVersion('called by Android'))");
    }
}
