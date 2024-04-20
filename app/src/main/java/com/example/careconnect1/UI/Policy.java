package com.example.careconnect1.UI;

import static com.example.careconnect1.Utilities.Config.IP;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.careconnect1.R;
import com.example.careconnect1.Utilities.AppCompatClass;
import com.example.careconnect1.Utilities.LoadingLayout;

public class Policy extends AppCompatClass {
    private  WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        setMethods("Privacy policy","");
    }

    @Override
    public void setInitialize() {
        super.setInitialize();
        webView=findViewById(R.id.webView);
        LoadingLayout.show(Policy.this);
    }

    @Override
    public void setActions() {
        super.setActions();
        webView.setWebViewClient(new webViewClient());
        webView.loadUrl(IP + "privacy.html");
    }

    public class webViewClient extends android.webkit.WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LoadingLayout.hide(Policy.this);
        }
    }
}