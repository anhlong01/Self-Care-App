package com.lph.selfcareapp.menu.account;

import static com.lph.selfcareapp.Utils.SecureStorageHelper.clearSessionKey;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lph.selfcareapp.Login.LoginActivity;
import com.lph.selfcareapp.MainDoctorActivity;
import com.lph.selfcareapp.R;
import com.lph.selfcareapp.Utils.BottomNavigationViewHelper;
import com.lph.selfcareapp.menu.FingerprintHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AccountDoctorActivity extends AppCompatActivity implements FingerprintHelper.FingerprintAuthListener {
    private RelativeLayout logout_btn;
    private ImageView backButton;
    private ImageView avatar;
    private TextView usernameText;
    private ListView account_listview;
    private Switch fingerprintSwitch;
    private FingerprintHelper fingerprintHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_doctor);

        // Ánh xạ các thành phần giao diện
        avatar = findViewById(R.id.avatar);
        logout_btn = findViewById(R.id.logout_btn);
        usernameText = findViewById(R.id.username);
        fingerprintSwitch = findViewById(R.id.fingerprint_switch);
        fingerprintHelper = new FingerprintHelper(this);
        setupNavigationView();

        // Lấy username từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "lephiha");
        usernameText.setText(username);

        Boolean fingerprint = sharedPreferences.getBoolean("fingerprint", false);
        if(!fingerprint){
            fingerprintSwitch.setChecked(false);
        }else{
            fingerprintSwitch.setChecked(true);
        }

        fingerprintSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                authenticateWithFingerprint();
            }else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("fingerprint", false);
                editor.apply();
            }
        });


        // Thiết lập sự kiện cho ảnh đại diện
        avatar.setOnClickListener(v -> checkPermissionAndOpenImagePicker());

        // Thiết lập sự kiện cho nút đăng xuất
        logout_btn.setOnClickListener(v -> {
            try {
                logout();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Thiết lập ListView với các mục và sự kiện click
//        setupListView();
    }

    private void setupListView() {
        // Các mục trong ListView
        String[] itemNames = {
                "Quy định sử dụng",
                "Chính sách bảo mật",
                "Điều khoản dịch vụ",
                "Tổng đài CSKH 19001234",
                "Đánh giá ứng dụng",
                "Chia sẻ ứng dụng",
                "Một số câu hỏi thường gặp",
                "Đăng Xuất",
                "Đăng nhập vân tay"
        };


        // Xử lý sự kiện khi click vào một mục trong ListView
//        account_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = null;
//                switch (position) {
//                    case 0:
//                        intent = new Intent(AccountActivity.this, UsePolicyActivity.class);
//                        break;
//                    case 1:
//                        intent = new Intent(AccountActivity.this, PrivacyPolicyActivity.class);
//                        break;
//                    case 2:
//                        intent = new Intent(AccountActivity.this, TermServiceActivity.class);
//                        break;
//                    case 3:
//                        intent = new Intent(AccountActivity.this, CallActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 4:
//                        intent = new Intent(AccountActivity.this, RateApp.class);
//                        startActivity(intent);
//                        break;
//                    case 5:
//                        // Tạo Intent để chia sẻ
//                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                        shareIntent.setType("text/plain");
//                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Chia sẻ ứng dụng");
//
//                        // Nội dung chia sẻ
//                        String shareMessage = "Tải ứng dụng SelfCare tại: https://www.selfcare.com";
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//
//                        // Hiển thị menu chia sẻ với các ứng dụng có thể xử lý
//                        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
//                        break;
//
//                    case 6:
//                        intent = new Intent(AccountActivity.this, FAQ.class);
//                        break;
//
//                    case 7:
//                        // Xóa thông tin
//                        getApplication().getSharedPreferences("MyPrefs", MODE_PRIVATE).edit().clear().apply();
//                        getApplication().getSharedPreferences("Login", MODE_PRIVATE).edit().clear().apply();
//                        intent = new Intent(AccountActivity.this, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//
//                        finish();
//                        break;
//
//                }
//
//                if (intent != null) {
//                    startActivity(intent);
//                }
//            }
//        });
    }

    private void checkPermissionAndOpenImagePicker() {
        // Kiểm tra quyền truy cập bộ nhớ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1); // Mã yêu cầu là 1
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Xử lý ảnh được chọn
            Uri selectedImageUri = data.getData();
            if (avatar != null) {
                avatar.setImageURI(selectedImageUri);
            }
        }
    }

    private void authenticateWithFingerprint(){
        if (fingerprintHelper.isFingerprintAvailable()) {
            fingerprintHelper.showFingerprintDialog(this, this);
        } else {
            Toast.makeText(this,
                    "Thiết bị không hỗ trợ hoặc chưa đăng ký vân tay",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() throws GeneralSecurityException, IOException {
        clearSessionKey(AccountDoctorActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Kết thúc Activity hiện tại
    }

    private void setupNavigationView(){
        Log.d("Main", "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.doctorBottomNavBar);
        BottomNavigationViewHelper.setupTopNavigationView(tvEx);
        BottomNavigationViewHelper.enableNavigation2(AccountDoctorActivity.this, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    @Override
    public void onAuthenticationSuccessful() {
        runOnUiThread(() -> {
            Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
            getSharedPreferences("UserData", MODE_PRIVATE).edit().putBoolean("fingerprint", true).apply();
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
