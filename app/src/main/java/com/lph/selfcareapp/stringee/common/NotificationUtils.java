package com.lph.selfcareapp.stringee.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Builder;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.RescheduleActivity;
import com.lph.selfcareapp.SeeResultActivity;
import com.lph.selfcareapp.menu.MedicalTicketActivity;
import com.lph.selfcareapp.stringee.activity.CallActivity;
import com.lph.selfcareapp.stringee.service.RejectCallReceiver;


public class NotificationUtils {
    private static volatile NotificationUtils instance;
    private final Context context;
    private final NotificationManager nm;
    private static final String GENERAL_CHANNEL_ID = "general_notifications";
    private static final String GENERAL_CHANNEL_NAME = "General Notifications";
    private static final int SIMPLE_NOTIFICATION_ID = 1001;
    public NotificationUtils(Context context) {
        this.context = context.getApplicationContext();
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            nm = context.getSystemService(NotificationManager.class);
        } else {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    public static synchronized NotificationUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (NotificationUtils.class) {
                if (instance == null) {
                    instance = new NotificationUtils(context);
                }
            }
        }
        return instance;
    }

    public void cancelNotification(int notificationId) {
        if (nm != null) {
            nm.cancel(notificationId);
        }
    }

    private String createCallNotificationChannel() {
        int channelIndex = PrefUtils.getInstance(context).getInt(Constant.PREF_INCOMING_CALL_CHANNEL_ID_INDEX, 0);
        String channelId = Constant.INCOMING_CALL_CHANNEL_ID + channelIndex;
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            //delete old channel
            NotificationChannel channel = nm.getNotificationChannel(channelId);
            if (channel != null && !(channel.getImportance() == NotificationManager.IMPORTANCE_MAX || channel.getImportance() == NotificationManager.IMPORTANCE_HIGH)) {
                nm.deleteNotificationChannel(channelId);
                channelIndex = channelIndex + 1;
                channelId = Constant.INCOMING_CALL_CHANNEL_ID + channelIndex;
            }
            //create new channel
            channel = new NotificationChannel(channelId, Constant.INCOMING_CALL_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            PrefUtils.getInstance(context).putInt(Constant.PREF_INCOMING_CALL_CHANNEL_ID_INDEX, channelIndex);
            channel.setDescription(Constant.INCOMING_CALL_CHANNEL_DESC);
            channel.setSound(null, null);
            nm.createNotificationChannel(channel);
        }
        return channelId;
    }

    public void showIncomingCallNotification(String from, boolean isStringeeCall, boolean isVideoCall) {
        String channelId = createCallNotificationChannel();

        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        if (VERSION.SDK_INT >= VERSION_CODES.S) {
            flag = PendingIntent.FLAG_IMMUTABLE;
        }

        Intent fullScreenIntent = new Intent(context, CallActivity.class);
        fullScreenIntent.putExtra(Constant.PARAM_IS_VIDEO_CALL, isVideoCall);
        fullScreenIntent.putExtra(Constant.PARAM_IS_INCOMING_CALL, true);
        fullScreenIntent.putExtra(Constant.PARAM_IS_STRINGEE_CALL, isStringeeCall);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, (int) (System.currentTimeMillis() & 0xfffffff), fullScreenIntent, flag);

        Intent actionAnswerIntent = new Intent(context, CallActivity.class);
        actionAnswerIntent.putExtra(Constant.PARAM_IS_VIDEO_CALL, isVideoCall);
        actionAnswerIntent.putExtra(Constant.PARAM_IS_INCOMING_CALL, true);
        actionAnswerIntent.putExtra(Constant.PARAM_IS_STRINGEE_CALL, isStringeeCall);
        actionAnswerIntent.putExtra(Constant.PARAM_ACTION_ANSWER_FROM_PUSH, true);
        PendingIntent actionAnswerPendingIntent = PendingIntent.getActivity(context, (int) (System.currentTimeMillis() & 0xfffffff), actionAnswerIntent, flag);

        Intent actionRejectIntent = new Intent(context, RejectCallReceiver.class);
        actionRejectIntent.putExtra(Constant.PARAM_IS_VIDEO_CALL, isVideoCall);
        actionRejectIntent.putExtra(Constant.PARAM_IS_INCOMING_CALL, true);
        actionRejectIntent.putExtra(Constant.PARAM_IS_STRINGEE_CALL, isStringeeCall);
        PendingIntent actionRejectPendingIntent = PendingIntent.getBroadcast(context, (int) (System.currentTimeMillis() & 0xfffffff), actionRejectIntent, flag);

        SpannableString answerTitle = new SpannableString("Answer");
        answerTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#57D24D")), 0, answerTitle.length(), 0);

        Spannable endTitle = new SpannableString("Reject");
        endTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#F64D64")), 0, endTitle.length(), 0);

        Builder notificationBuilder = new Builder(context, channelId);
        notificationBuilder.setContentTitle("Incoming call");
        notificationBuilder.setContentText("incoming call from: " + from);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setOngoing(true);
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setVibrate(new long[0]);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setCategory(NotificationCompat.CATEGORY_CALL);
        notificationBuilder.addAction(R.drawable.ic_answer_call, answerTitle, actionAnswerPendingIntent);
        notificationBuilder.addAction(R.drawable.ic_end_call, endTitle, actionRejectPendingIntent);
        notificationBuilder.setFullScreenIntent(fullScreenPendingIntent, true);
        notificationBuilder.setShowWhen(false);

        Notification incomingCallNotification = notificationBuilder.build();

        AudioManagerUtils.getInstance(context).startRingtoneAndVibration();

        nm.notify(Constant.INCOMING_CALL_ID, incomingCallNotification);
    }

    private void createMediaServiceChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constant.MEDIA_CHANNEL_ID, Constant.MEDIA_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Constant.MEDIA_CHANNEL_DESC);
            channel.setSound(null, null);
            nm.createNotificationChannel(channel);
        }
    }

    public Notification createMediaNotification() {
        createMediaServiceChannel();

        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        if (VERSION.SDK_INT >= VERSION_CODES.S) {
            flag = PendingIntent.FLAG_IMMUTABLE;
        }

        Intent intent = new Intent(context, CallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) (System.currentTimeMillis() & 0xfffffff), intent, flag);

        Builder builder = new Builder(context, Constant.MEDIA_CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.icon);
        builder.setSound(null);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentTitle("Capturing screen");
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);
        return builder.build();
    }

    public void showSimpleNotification(String type, String title, String body) {
        createGeneralNotificationChannel(); // Đảm bảo channel được tạo

        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        if (VERSION.SDK_INT >= VERSION_CODES.S) {
            flag = PendingIntent.FLAG_IMMUTABLE;
        }
        Intent intent = null;
        if(type.equals("new_appointment")) {
            intent = new Intent(context, MedicalTicketActivity.class); // Thay bằng Activity bạn muốn mở
            // Hoặc Intent intent = new Intent(context, AppointmentDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Quan trọng cho thông báo
        }else if(type.equals("new_report")){
            intent = new Intent(context, SeeResultActivity.class); // Thay bằng Activity bạn muốn mở
            // Hoặc Intent intent = new Intent(context, AppointmentDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Quan trọng cho thông báo
        }else if(type.equals("reschedule")){
            intent = new Intent(context, RescheduleActivity.class); // Thay bằng Activity bạn muốn mở
            // Hoặc Intent intent = new Intent(context, AppointmentDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Quan trọng cho thông báo
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                (int) System.currentTimeMillis(), // Request code duy nhất
                intent,
                flag
        );


        Builder builder = new Builder(context, GENERAL_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) // Icon thông báo của bạn
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Quan trọng: PRIORITY_HIGH cho Heads-up
                .setAutoCancel(true) // Tự động hủy khi click
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL); // Âm thanh, rung, đèn LED mặc định

        // Hiển thị thông báo với một ID duy nhất
        // Sử dụng một ID cụ thể, ví dụ Constant.APPOINTMENT_NOTIFICATION_ID nếu bạn muốn cập nhật/hủy nó sau
        nm.notify(Constant.APPOINTMENT_NOTIFICATION_ID, builder.build()); // Đảm bảo Constant.APPOINTMENT_NOTIFICATION_ID là một int duy nhất
    }

    private void createGeneralNotificationChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    GENERAL_CHANNEL_ID,
                    GENERAL_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH // Vẫn dùng HIGH để có Heads-up nếu bạn muốn
            );
            channel.setDescription("Channel for general app notifications like new appointments.");
            nm.createNotificationChannel(channel);
        }
    }
}
