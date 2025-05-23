package com.lph.selfcareapp.vnpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lph.selfcareapp.MainActivity;
import com.lph.selfcareapp.R;



public class VnpayActivity extends AppCompatActivity implements PaymentResultCallback {
    private WebView webView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnpay);
        progressDialog = new ProgressDialog(this);
        webView = findViewById(R.id.vnpayWebview);
        setupWebView();

        // Lấy URL thanh toán từ Intent
        String paymentUrl = getIntent().getStringExtra("payment_url");
        if (paymentUrl != null) {
            webView.loadUrl(paymentUrl);
        }
    }

    private void setupWebView(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        // Bật các tính năng cần thiết cho thanh toán
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        webView.setWebViewClient(new VNPayWebViewClient(this));
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String url) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("payment_result", url);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPaymentFailure(String url) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("payment_status", "failure");
        resultIntent.putExtra("return_url", url);
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }
}


