package com.lph.selfcareapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.Utils.DoctorComparator;
import com.lph.selfcareapp.adapter.DoctorLoadStateAdapter;
import com.lph.selfcareapp.databinding.DoctorInfoBinding;
import com.lph.selfcareapp.factory.PagingDoctorViewModelFactory;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;
import com.lph.selfcareapp.adapter.DoctorAdapter;
import com.lph.selfcareapp.adapter.DoctorPagingAdapter;
import com.lph.selfcareapp.listener.ChooseDoctorListener;
import com.lph.selfcareapp.viewmodel.PagingDoctorViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChonBacSiActivity extends AppCompatActivity implements ChooseDoctorListener {
//    Dialog dialog;
    private RecyclerView doctorRecyclerView;
    ImageButton imageButton;
    ImageButton backBtn;
    TextView backText;;
    String jwt;
    private List<Doctor> doctorList;
    DoctorAdapter doctorAdapter;
    DoctorPagingAdapter doctorPagingAdapter;
    PagingDoctorViewModel pagingDoctorViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chon_bac_si);
        doctorRecyclerView =findViewById(R.id.doctorRecyclerview2);
        imageButton = findViewById(R.id.filterImageButton2);
        backText = findViewById(R.id.navText);
        backBtn = findViewById(R.id.back);
        backText.setText("Chọn bác sĩ");
        backBtn.setOnClickListener(v->finish());
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = sp.getString("jwt", "");
        int clinic_id = sp.getInt("clinic",0);
        doctorPagingAdapter= new DoctorPagingAdapter(new DoctorComparator(), this);
        pagingDoctorViewModel = new ViewModelProvider(this, new PagingDoctorViewModelFactory(getApplication(),null,clinic_id,null)).get(PagingDoctorViewModel.class);
        initRecyclerviewAndAdapter();

        pagingDoctorViewModel.doctorPagingDataFlowable.subscribe(doctorPagingData -> {
            doctorPagingAdapter.submitData(getLifecycle(),doctorPagingData);
        });
    }


    @Override
    public void onItemClicked(Doctor doctor) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedDoctor",  doctor);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void seeInfo(Doctor doctor) {
        DoctorInfoBinding doctorInfoBinding = DoctorInfoBinding.inflate(LayoutInflater.from(this));
        Dialog dialog = new Dialog(this);
        dialog.setContentView(doctorInfoBinding.getRoot());
        doctorInfoBinding.setDoctor(doctor);
        doctorInfoBinding.out.setOnClickListener(v->dialog.dismiss());
        String price = String.format("%,d", doctor.getPrice());
        price = price +"đ";
        doctorInfoBinding.docname.setText(doctor.getAcademicRank() + " " + doctor.getDocName());
        doctorInfoBinding.price.setText(price);
        String html = doctor.getIntro();
        WebView webView = doctorInfoBinding.detailWebView;
        WebSettings settings = webView.getSettings();

        String wrappedHtml = "<!DOCTYPE html><html><head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\">" +
                "<style>" +
                "   body { width: 100% !important; margin: 0 !important; padding: 8px !important; font-size: 16px !important; }" +
                "   * { max-width: 100% !important; }" +  // Áp dụng cho mọi thẻ
                "   table { width: 100% !important; border-collapse: collapse !important; }" +
                "   img, iframe { height: auto !important; }" +
                "   p, div { word-wrap: break-word !important; overflow-wrap: break-word !important; }" +  // Tự động xuống dòng
                "</style>" +
                "</head><body>" + html + "</body></html>";

        // Thiết lập WebView
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);  // Quan trọng
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);  // Cần thiết cho một số trang web

        // Xử lý nội dung động (nếu cần)
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Fix các phần tử cứng đầu bằng JavaScript
                view.evaluateJavascript(
                        "document.querySelectorAll('table, div').forEach(el => el.style.width = '100%');" +
                                "document.body.style.whiteSpace = 'normal';", null);
            }
        });

        webView.loadDataWithBaseURL(null, wrappedHtml, "text/html", "UTF-8", null);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.show();
    }

    private void initRecyclerviewAndAdapter() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);

        doctorRecyclerView.setLayoutManager(gridLayoutManager);



        doctorRecyclerView.setAdapter(
                doctorPagingAdapter.withLoadStateFooter(
                        new DoctorLoadStateAdapter(view -> {
                            doctorPagingAdapter.retry();
                        })
                )
        );

//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return doctorPagingAdapter.getItemViewType(position) == doctorPagingAdapter.LOADING_ITEM ? 1:2;
//            }
//        });


    }
}