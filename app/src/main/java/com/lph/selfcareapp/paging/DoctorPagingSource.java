package com.lph.selfcareapp.paging;

import static android.content.Context.MODE_PRIVATE;

import static com.lph.selfcareapp.stringee.common.Constant.TAG;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.model.Page;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;



public class DoctorPagingSource extends RxPagingSource<Integer, Doctor> {
    private Application application;
    private final String name;
    private final Integer clinicId;
    private final String spe;
    private final String sortBy;
    private final String direction;

    public DoctorPagingSource(Application application, String name, Integer clinicId, String spe, String sortBy, String direction) {
        this.application = application;
        this.name = name;
        this.clinicId = clinicId;
        this.spe = spe;
        this.sortBy = sortBy;
        this.direction = direction;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Doctor> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Doctor>> loadSingle(@NonNull LoadParams<Integer> params) {
        try {
            int page = params.getKey() != null ? params.getKey() : 0;
            int pageSize = params.getLoadSize();
            SharedPreferences sp = application.getSharedPreferences("UserData", MODE_PRIVATE);
            String jwt = sp.getString("jwt", "");
            return RetrofitInstance.getPagingService(jwt).getDoctors(name, clinicId, spe, page, pageSize, sortBy, direction)
                    .subscribeOn(Schedulers.io()) // Thực hiện gọi API trên thread IO
                    .map(response -> toLoadResult(response,page))
                    .onErrorReturn(LoadResult.Error::new);
        }catch(Exception e){
            return Single.just(new LoadResult.Error(e));
        }
    }

    private LoadResult<Integer, Doctor> toLoadResult(@NonNull Page<Doctor> response, int page) {
        List<Doctor> doctors = response.getContent();
        boolean isLastPage = response.getLast();

        Log.d("paging", "Received " + doctors.size() + " doctors, isLastPage: " + isLastPage);

        // Chỉ trả về nextKey nếu chưa phải trang cuối
        Integer nextKey = isLastPage ? null : page + 1;

        // Trang trước đó chỉ tồn tại nếu page > 0
        Integer prevKey = page > 0 ? page - 1 : null;

        return new LoadResult.Page<>(
                doctors,
                prevKey,
                nextKey
        );
    }
}
