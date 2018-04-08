package androidified.logavimadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.airbnb.lottie.LottieAnimationView;



public class AdminPanelActivity extends AppCompatActivity {
    WebView webView;
    Boolean doubleBackToExitPressedOnce = false;

    private class MyWebViewClient extends WebViewClient {

        LottieAnimationView l = (LottieAnimationView) findViewById(R.id.animation_view);

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admin_panel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webView1);
        progressDialog = new ProgressDialog(AdminPanelActivity.this);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                LottieAnimationView l = (LottieAnimationView) findViewById(R.id.animation_view);
                l.pauseAnimation();
                l.setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LottieAnimationView l = (LottieAnimationView) findViewById(R.id.animation_view);
                view.loadUrl(url);
                l.playAnimation();
                l.setVisibility(View.VISIBLE);
                return true;
            }
        });
        openURL();
    }

    private void openURL() {
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        webView.loadUrl("https://www.indiapost.gov.in/sites/DOPMobile/Pages/DopMobile.aspx");
        webView.requestFocus();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}