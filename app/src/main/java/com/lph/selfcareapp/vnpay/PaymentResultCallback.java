package com.lph.selfcareapp.vnpay;

public interface PaymentResultCallback {
    void onPaymentSuccess(String url);
    void onPaymentFailure(String url);
}