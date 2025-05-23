package com.lph.selfcareapp.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.adapter.DoctorAdapter;
import com.lph.selfcareapp.viewmodel.DatLichViewModel;

import java.util.List;

public class ChonBacSiFragment extends Fragment {
    TextView chonBacSi;
    Dialog dialog;
    EditText editText;
    private RecyclerView doctorRecyclerView;
    ImageButton imageButton;
    String jwt;
    private List<Doctor> doctorList;
    DoctorAdapter doctorAdapter;
    DatLichViewModel viewModel;
    private ActivityResultLauncher<Intent> chonBacSiLauncher;
    public ChonBacSiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_chon_bac_si, container, false);
        chonBacSi = rootview.findViewById(R.id.chooseDoctorFrag);
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_searchable_doctor);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SharedPreferences sp = requireContext().getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = sp.getString("jwt", "");
        viewModel = new ViewModelProvider(requireActivity()).get(DatLichViewModel.class);

        chonBacSiLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == RESULT_OK){
                        Doctor doctor = (Doctor) result.getData().getSerializableExtra("selectedDoctor");
                        viewModel.setSelectedDoctor(doctor);
                        chonBacSi.setText(doctor.getDocName());
                    }
                }
        );

        chonBacSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChonBacSiActivity.class);
                chonBacSiLauncher.launch(intent);
            }
        });
        return rootview;
    }




}