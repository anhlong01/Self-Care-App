package com.lph.selfcareapp;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks {
    private int activeActivities = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        Log.d("AppLifeCycle2", "App is created");
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Log.d("AppLifeCycle2", "Activity Created: " + activity.getClass().getSimpleName()); // Thêm log để theo dõi
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        activeActivities++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        activeActivities--;
        if (activeActivities == 0) {
            // Đóng ứng dụng khi không có hoạt động nào đang chạy
            Log.d("AppLifeCycle2", "App is in the background");
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if(activeActivities == 0){
            SharedPreferences.Editor sp = getSharedPreferences("UserData", MODE_PRIVATE).edit();
            sp.remove("jwt");
            sp.commit();
            Log.d("AppLifeCycle2", "App is completely destroyed");
        }
    }
}
