package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ReviewActivity extends AppCompatActivity {

    private String mUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mWebView = (WebView) findViewById(R.id.webview_review);

        Intent intentThatStartedThisActivity = getIntent();

        // Display the contents news link that was passed from NewsActivity
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mUrl = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mWebView.loadUrl(mUrl);
            }
        }

    }
}
