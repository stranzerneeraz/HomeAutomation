package com.example.homeautomation;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String myUrl = "file:///android_asset/index.html";
        WebView view = this.findViewById(R.id.webView);
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebChromeClient(new WebChromeClient());
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.addJavascriptInterface(new WebInterface(MainActivity.this), "Android");
        view.loadUrl(myUrl);
    }
    private final class WebInterface {
        private WeakReference<Activity> mContextRef;
        public Toast toast;

        public WebInterface(Activity context) {
            this.mContextRef = new WeakReference <Activity>(context);
        }

        @JavascriptInterface
        public void showToast(final String toastMsg) {
            if (TextUtils.isEmpty(toastMsg) || mContextRef.get() == null) {
                return;
            }

            mContextRef.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(mContextRef.get(), toastMsg , Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
        @JavascriptInterface
        public boolean isNetworkAvailable() {
        // Get Connectivity Manager class object from Systems Service
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get Network Info from connectivity Manager
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if no network is available networkInfo will be null
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    }

}
