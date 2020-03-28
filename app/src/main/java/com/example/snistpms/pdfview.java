package com.example.snistpms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class pdfview extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    String path,url,type,htmlurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        progressBar = findViewById(R.id.progressbarweb);
        webView = (WebView) findViewById(R.id.showdocsweb);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        type=intent.getStringExtra("type");
       // htmlurl=intent.getStringExtra("htmlurl");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.supportZoom();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        webView.clearCache(true);
        webView.canGoBackOrForward(10);
        webSettings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        Log.i("Content-url", url);
        //Log.i("url extension", url.substring(url.lastIndexOf('.')));
        //if(url.substring(url.lastIndexOf('.')).equals(".ppt")||url.substring(url.lastIndexOf('.')).equals(".pptx")||url.substring(url.lastIndexOf('.')).equals(".doc")||url.substring(url.lastIndexOf('.')).equals(".docx")){
        if(type.equals("microsoft")){
            webView.loadUrl("https://view.officeapps.live.com/op/embed.aspx?src=" + url);
        }
        else if(type.equals("google")){
            webView.loadUrl("https://docs.google.com/viewer?url=" + url);
        }

        webView.setWebViewClient(new WebViewClient());
        // https://raw.githubusercontent.com/naruto-pdfs/DS-C-data-structures-and-c-/master/Unit%203.ppt
    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

}
