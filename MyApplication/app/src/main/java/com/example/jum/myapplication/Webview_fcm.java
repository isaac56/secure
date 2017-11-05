package com.example.jum.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by Jun on 2017-09-06.
 */

public class Webview_fcm extends AppCompatActivity {
    private WebView mWebView;
    String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Log.d("webview","in webview");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if( bundle != null){
            if(bundle.getString("url") != null && !bundle.getString("url").equalsIgnoreCase("")) {
                url = bundle.getString("url");
            }
        }

        Log.d("webview",url);

        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.loadUrl(url);
        //mWebView.setWebViewClient(new WebViewClientClass());
    }
    /*
    private class WebViewClientClass extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            view.loadUrl(url);
        }
    }*/
}
