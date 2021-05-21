package com.wisewolf.midhilaarts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.vimeo.networking.model.Video;
import com.wisewolf.midhilaarts.Global.GlobalData;

public class WebAndroid extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_android);
        webView=findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        WebSettings settings = webView.getSettings();
        settings.setMinimumFontSize(18);
        settings.setLoadWithOverviewMode(false);
        settings.setUseWideViewPort(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);





        final String mimeType = "text/html";
        final String encoding = "UTF-8";

String link="<iframe src=\"https://player.vimeo.com/video/429889560?title=0&amp;byline=0&amp;portrait=0&amp;badge=0&amp;autopause=0&amp;player_id=0&amp;app_id=179175\" width=\"1920\" height=\"1080\" frameborder=\"0\" allow=\"autoplay; fullscreen\" allowfullscreen title=\"TINY TOTS\"></iframe>";
        webView.loadData(link, mimeType, encoding);
    }
}