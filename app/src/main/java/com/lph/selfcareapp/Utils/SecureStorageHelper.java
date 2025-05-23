package com.lph.selfcareapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecureStorageHelper {
    public static void saveSessionKey(Context context, String sessionKey) throws Exception{
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        // Tạo EncryptedSharedPreferences
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        // Lưu session key
        sharedPreferences.edit()
                .putString("SESSION_KEY", sessionKey)
                .apply();
    }

    public static void clearSessionKey(Context context) throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        // Tạo EncryptedSharedPreferences
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        // Lưu session key
        sharedPreferences.edit()
                .clear()
                .apply();
    }

    public static String getSessionKey(Context context) throws Exception {
        // Tạo MasterKey
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        // Tạo EncryptedSharedPreferences
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        // Lấy session key
        return sharedPreferences.getString("SESSION_KEY", null);
    }

}
