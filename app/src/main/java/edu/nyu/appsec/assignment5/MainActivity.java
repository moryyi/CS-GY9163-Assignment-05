package edu.nyu.appsec.assignment5;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
//    private static final String SPELL_CHECK_URL = "http://appsecclass.report:8080/";
//    private static final String KNOWN_HOST = "appsecclass.report";
    private static final String SPELL_CHECK_URL = "http://10.0.2.2:5000/";
    private static final String KNOWN_HOST = "10.0.2.2";

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = String.valueOf(request.getUrl());
            String host = Uri.parse(url).getHost();

            if (KNOWN_HOST.equals(host)) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView view = new WebView(this);
        view.setWebViewClient(new MyWebViewClient());

        WebSettings settings = view.getSettings();
        settings.setAllowFileAccessFromFileURLs(false);  // <- "This setting is not secure"
                                                        // check 1. https://developer.android.com/reference/android/webkit/WebSettings#setAllowFileAccessFromFileURLs(boolean)
        settings.setJavaScriptEnabled(false);            // <- "Using setJavaScriptEnabled can introduce XSS vulnerabilities into you application"
                                                        // check 1. https://stackoverflow.com/questions/20138434/alternate-solution-for-setjavascriptenabledtrue
                                                        // check 2. https://pentestlab.blog/2017/02/12/android-webview-vulnerabilities/
                                                        // if there is no JS to be loaded, just don't set this
                                                        // this may allow attackers to interact with functionalities of the device
        settings.setAllowUniversalAccessFromFileURLs(false); // <- "This setting is not secure"
                                                            // check 1. https://developer.android.com/reference/android/webkit/WebSettings#setAllowUniversalAccessFromFileURLs(boolean)
                                                            // may allow malicious scripts loaded in a "file://" context

        setContentView(view);
        view.loadUrl(SPELL_CHECK_URL + "register");
    }
}
