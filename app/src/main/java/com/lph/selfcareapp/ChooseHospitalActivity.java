package com.lph.selfcareapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.Utils.ClinicComparator;
import com.lph.selfcareapp.adapter.ClinicPagingAdapter;
import com.lph.selfcareapp.adapter.DoctorLoadStateAdapter;
import com.lph.selfcareapp.databinding.ActivityChooseHospitalBinding;
import com.lph.selfcareapp.factory.PagingAppointmentViewModelFactory;
import com.lph.selfcareapp.factory.PagingClinicViewModelFactory;
import com.lph.selfcareapp.fragment.DatLichActivity;
import com.lph.selfcareapp.model.Clinic;
import com.lph.selfcareapp.model.ClinicList;
import com.lph.selfcareapp.adapter.ClinicAdapter;
import com.lph.selfcareapp.listener.ChooseClinicListener;
import com.lph.selfcareapp.viewmodel.ClinicViewModel;
import com.lph.selfcareapp.viewmodel.PagingAppointmentsViewModel;
import com.lph.selfcareapp.viewmodel.PagingClinicViewModel;

public class ChooseHospitalActivity extends AppCompatActivity implements ChooseClinicListener {
    private ActivityChooseHospitalBinding binding;
    private RecyclerView recyclerView;
    private ClinicPagingAdapter clinicPagingAdapter;
    private PagingClinicViewModel pagingClinicViewModel;
    ImageButton back;
    EditText search;
    Dialog dialog;
    PagingAppointmentsViewModel appointmentsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_hospital);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_choose_hospital);
        search = binding.searchBar;
        recyclerView = binding.clinicRecyclerview;
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        double longitude = Double.parseDouble(sp.getString("longitude",""));
        double latitude = Double.parseDouble(sp.getString("latitude",""));
        clinicPagingAdapter = new ClinicPagingAdapter(new ClinicComparator(), this, this);
        pagingClinicViewModel = new ViewModelProvider(this, new PagingClinicViewModelFactory(getApplication(), null, longitude, latitude, "clinicId")).get(PagingClinicViewModel.class);
        displayClinicsInRecyclerview();
        setUpSearchListener();
        binding.filterImageButton.setOnClickListener(v->openFilterDialog());
    }


    public void displayClinicsInRecyclerview(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(gridLayoutManager);


        recyclerView.setAdapter(
                clinicPagingAdapter.withLoadStateFooter(
                        new DoctorLoadStateAdapter(view -> {
                            clinicPagingAdapter.retry();
                        })
                )
        );

        pagingClinicViewModel.clinicPagingData.observe(this, appointmentPagingData -> {
            clinicPagingAdapter.submitData(getLifecycle(), appointmentPagingData);
        });
    }

    @Override
    public void onItemClicked(Clinic clinic) {
        Intent intent = new Intent(getApplication(), DatLichActivity.class);
        intent.putExtra("clinic", clinic);
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("clinic", clinic.getClinic_id());
        editor.apply();
        startActivity(intent);
    }

    @Override
    public void openMap(Clinic clinic) {
        String geoUri = "http://maps.google.com/maps?q=loc:" + clinic.getLatitude() + "," + clinic.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        startActivity(intent);;
    }

    private void setUpSearchListener(){
        final Handler handler= new Handler();
        final Runnable[] runnable = {null};
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Xóa callback trước đó nếu có
                if (runnable[0] != null) {
                    handler.removeCallbacks(runnable[0]);
                }

                // Tạo callback mới
                runnable[0] = () -> {
                    String searchQuery = s.toString().trim();
                    pagingClinicViewModel.setName(searchQuery);
                };

                // Đợi 500ms sau khi ngừng gõ
                handler.postDelayed(runnable[0], 500);

            }
        });
    }

    private void openFilterDialog(){
        dialog = new Dialog(ChooseHospitalActivity.this);
        dialog.setContentView(R.layout.dialog_filter_clinic);
        dialog.getWindow().setLayout(800,1500);
        RadioButton radioOption1 = dialog.findViewById(R.id.radio_option1);
        RadioButton radioOption2 = dialog.findViewById(R.id.radio_option2);
        Button showBtn = dialog.findViewById(R.id.showbutton);
        if(pagingClinicViewModel.getFilterParams("sortBy").equals("clinicId")){
            radioOption2.setChecked(true);
        }
        else{
            radioOption1.setChecked(true);
        }
        radioOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RadioButton", "checked");
                radioOption1.setChecked(true);
            }
        });

        showBtn.setOnClickListener(v->{
            if(radioOption1.isChecked()){
                Log.d("RadioButton", "filtering");
                pagingClinicViewModel.setSortBy("distance");

            }else{
                pagingClinicViewModel.setSortBy("clinicId");
            }

            dialog.dismiss();
        });
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}