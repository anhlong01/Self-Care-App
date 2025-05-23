package com.lph.selfcareapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lph.selfcareapp.R;
import com.lph.selfcareapp.viewmodel.DatLichViewModel;

public class ChonChuyenKhoaFragment extends Fragment {
    DatLichViewModel datLichViewModel;
    public ChonChuyenKhoaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ChonChuyenKhoaFragment", "onCreateView: ");
        View rootview =  inflater.inflate(R.layout.fragment_chon_chuyen_khoa, container, false);

        return rootview;
    }

    @Override
    public void onResume() {
        Log.d("ChonChuyenKhoaFragment", "onResume: ");
        datLichViewModel = new ViewModelProvider(requireActivity()).get(DatLichViewModel.class);
        datLichViewModel.setSelectedDoctor(null);
        super.onResume();
    }
}