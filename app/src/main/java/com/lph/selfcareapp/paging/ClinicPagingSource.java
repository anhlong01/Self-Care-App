package com.lph.selfcareapp.paging;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Clinic;
import com.lph.selfcareapp.model.Page;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ClinicPagingSource extends RxPagingSource<Integer, Clinic> {
    private Application application;
    private final String name;
    private final Double longitude;
    private final Double latitude;
    private final String sortBy;
    private final String direction;

    public ClinicPagingSource(Application application, String name, Double longitude, Double latitude, String sortBy, String direction) {
        this.application = application;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sortBy = sortBy;
        this.direction = direction;
    }


    @NonNull
    @Override
    public Single<LoadResult<Integer, Clinic>> loadSingle(@NonNull LoadParams<Integer> params) {
        Log.d("paging", "name: " + name + ", latitude: " + latitude + ", longitude: " + longitude + ", sortBy: " + sortBy + ", direction: " + direction);
        try {
            int page = params.getKey() != null ? params.getKey() : 0;
            int pageSize = params.getLoadSize();
            SharedPreferences sp = application.getSharedPreferences("UserData", MODE_PRIVATE);
            String jwt = sp.getString("jwt", "");
            return RetrofitInstance.getPagingService(jwt).getPageClinics(name, latitude, longitude, page, pageSize, sortBy, direction)
                    .subscribeOn(Schedulers.io()) // Thực hiện gọi API trên thread IO
                    .map(response -> toLoadResult(response,page))
                    .onErrorReturn(error->{
                        Log.e("PagingError", "Load failed", error);
                        return new LoadResult.Error<>(error);
                    });
        }catch(Exception e){
            Log.d("Error", e.toString());
            return Single.just(new LoadResult.Error(e));
        }
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Clinic> pagingState) {
        return 0;
    }

    private LoadResult<Integer, Clinic> toLoadResult(@NonNull Page<Clinic> response, int page) {
        List<Clinic> appointments = response.getContent();
        boolean isLastPage = response.getLast();

        Log.d("paging", "Received " + appointments.size() + " doctors, isLastPage: " + isLastPage);

        // Chỉ trả về nextKey nếu chưa phải trang cuối
        Integer nextKey = isLastPage ? null : page + 1;

        // Trang trước đó chỉ tồn tại nếu page > 0
        Integer prevKey = page > 0 ? page - 1 : null;

        return new LoadResult.Page<>(
                appointments,
                prevKey,
                nextKey
        );
    }
}
