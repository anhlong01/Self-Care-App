package com.lph.selfcareapp.vnpay;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class VNPayWebViewClient extends WebViewClient {
    private final PaymentResultCallback callback;

    public VNPayWebViewClient(PaymentResultCallback callback) {
        this.callback = callback;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();

        // Kiểm tra URL trả về từ VNPay
        if (url.contains("vnp_ResponseCode=00")) {
            view.loadUrl(url);

            // Delay 3 giây SAU KHI trang load xong mới gọi callback
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (view.getContext() instanceof Activity) {
                    callback.onPaymentSuccess(url);
                }
            }, 3000); // Đợi 3 giây để người dùng xem thông báo
            return true;

        } else if (url.contains("vnp_ResponseCode") && !url.contains("vnp_ResponseCode=00")) {
            view.loadUrl(url);

            // Delay 3 giây SAU KHI trang load xong mới gọi callback
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (view.getContext() instanceof Activity) {
                    callback.onPaymentFailure(url);
                }
            }, 3000); // Đợi 3 giây để người dùng xem thông báo
            // Thanh toán thất bại

            return true;
        }

        return false;
    }


}

