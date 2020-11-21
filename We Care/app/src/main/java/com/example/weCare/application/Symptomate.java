package com.example.weCare.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Symptomate extends AppCompatActivity {

    WebView wvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptomate);

        wvNews = (WebView) findViewById(R.id.wvNews);

        wvNews.getSettings().setJavaScriptEnabled(true);
        wvNews.setWebViewClient(new WebViewClient());
        wvNews.loadUrl("https://symptomate.com/diagnosis/#0-66");
    }
}