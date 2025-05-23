package com.lph.selfcareapp.stringee.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lph.selfcareapp.stringee.common.AudioManagerUtils;
import com.lph.selfcareapp.stringee.common.Constant;
import com.lph.selfcareapp.stringee.common.NotificationUtils;
import com.lph.selfcareapp.stringee.common.Utils;
import com.lph.selfcareapp.stringee.manager.ClientManager;
import com.stringee.listener.StatusListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("Firebase token", "Refreshed token: " + token);
        if (ClientManager.getInstance(this).getStringeeClient() == null) {
            ClientManager.getInstance(this).connect();
        }
        ClientManager.getInstance(this).getStringeeClient().registerPushToken(token, new StatusListener() {
            @Override
            public void onSuccess() {

            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(Constant.TAG, remoteMessage.getData().toString());
        if (!remoteMessage.getData().isEmpty()) {
            String messageType = remoteMessage.getData().get("type");
            String pushFromStringee = remoteMessage.getData().get("stringeePushNotification");
            if (pushFromStringee != null) {
                String data = remoteMessage.getData().get("data");
                if (data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String callStatus = jsonObject.optString("callStatus");
                        if (callStatus.equals("started")) {
                            ClientManager.getInstance(this).connect();
                        }
                        if (callStatus.equals("ended") || callStatus.equals("answered") || callStatus.equals("agentEnded")) {
                            NotificationUtils.getInstance(this).cancelNotification(Constant.INCOMING_CALL_ID);
                            AudioManagerUtils.getInstance(this).stopRinging();
                        }
                    } catch (JSONException e) {
                        Utils.reportException(MyFirebaseMessagingService.class, e);
                    }
                }
            }
            else{
                // Đây là tin nhắn về lịch hẹn mới
                String title = remoteMessage.getData().get("title");
                String body = remoteMessage.getData().get("body");

                // Gọi phương thức để hiển thị Local Notification
                // Bạn đã có NotificationUtils, giờ chúng ta sẽ dùng nó để hiển thị một thông báo chung
                NotificationUtils.getInstance(this).showSimpleNotification(
                        messageType,
                        title != null ? title : "Thông báo mới",
                        body != null ? body : "Bạn có một thông báo quan trọng."
                );
            }
        }else{

        }
    }
}
