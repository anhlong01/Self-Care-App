package com.lph.selfcareapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.lph.selfcareapp.menu.FingerprintHelper;

public class FingerPrintActivity extends AppCompatActivity implements FingerprintHelper.FingerprintAuthListener {
    private FingerprintHelper fingerprintHelper;
    SwitchMaterial materialSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        fingerprintHelper = new FingerprintHelper(this);

        materialSwitch = findViewById(R.id.material_switch);
        materialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (isChecked) {
                editor.putBoolean("fingerprint", true);
                editor.apply();
//                authenticateWithFingerprint();
            } else {
                editor.putBoolean("fingerprint", false);
                editor.apply();
                Toast.makeText(this, "Đã tắt xác thực vân tay", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void authenticateWithFingerprint() {
        if (fingerprintHelper.isFingerprintAvailable()) {
            fingerprintHelper.showFingerprintDialog(this, this);
        } else {
            Toast.makeText(this,
                    "Thiết bị không hỗ trợ hoặc chưa đăng ký vân tay",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAuthenticationSuccessful() {
        runOnUiThread(() -> {
            Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
            // Thực hiện hành động sau khi xác thực thành công
        });
    }

    @Override
    public void onAuthenticationError(int errorCode, String errorMessage) {
        runOnUiThread(() ->
                Toast.makeText(this, "Lỗi xác thực: " + errorMessage, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAuthenticationFailed() {
        runOnUiThread(() ->
                Toast.makeText(this, "Xác thực thất bại", Toast.LENGTH_SHORT).show());
    }
}