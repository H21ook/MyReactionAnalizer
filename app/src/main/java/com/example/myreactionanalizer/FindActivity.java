package com.example.myreactionanalizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class FindActivity extends AppCompatActivity {

    private WebAppInterface2 webAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        Button btn = (Button) findViewById(R.id.calcBtn);
        final EditText inputText = (EditText)findViewById(R.id.inputText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputText.getText().toString().length() > 0) {
                    //WebAppInterface
                    WebView webView = (WebView) findViewById(R.id.webView2);
                    webView.loadUrl("file:///android_asset/index2.html");
                    webAppInterface = new WebAppInterface2(FindActivity.this, FindActivity.this);
                    webAppInterface.setText(inputText.getText().toString());
                    webView.addJavascriptInterface(webAppInterface, "AndroidInterface");

                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);

                    webView.setWebViewClient(new CustomWebViewClient());
                    webView.setWebChromeClient(new CustomWebChromeClient());
                    //WebAppInterface
                }
            }
        });
    }
}
