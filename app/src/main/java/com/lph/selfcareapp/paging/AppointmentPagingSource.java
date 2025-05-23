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
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.model.Page;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppointmentPagingSource extends RxPagingSource<Integer, Appointment> {
    private Application application;
    private final Integer pid;
    private final Integer appoid;
    private final String startDate;
    private final Integer hasDone;
    private final Integer docid;
    private final String endDate;
    private final String sortBy;
    private final String direction;

    public AppointmentPagingSource(Integer pid, Integer appoid, String startDate, String endDate, Integer hasDone, Integer docid, String sortBy, String direction, Application application) {
        this.pid = pid;
        this.appoid = appoid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hasDone = hasDone;
        this.docid = docid;
        this.sortBy = sortBy;
        this.direction = direction;
        this.application = application;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Appointment>> loadSingle(@NonNull LoadParams<Integer> params) {
        Log.d("paging", "pid: " + pid + ", appoid: " + appoid + ", startDate: " + startDate + ", endDate: " + endDate + ", hasDone:" + hasDone + ", docid" + docid + ", sortBy: " + sortBy + ", direction: " + direction);
        try {
            int page = params.getKey() != null ? params.getKey() : 0;
            int pageSize = params.getLoadSize();
            SharedPreferences sp = application.getSharedPreferences("UserData", MODE_PRIVATE);
            String jwt = sp.getString("jwt", "");
            return RetrofitInstance.getPagingService(jwt).getPageAppointment(pid, appoid, startDate, endDate, hasDone, docid, page, pageSize, sortBy, direction)
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
    public Integer getRefreshKey(@NonNull PagingState<Integer, Appointment> pagingState) {
        return 0;
    }

    private LoadResult<Integer, Appointment> toLoadResult(@NonNull Page<Appointment> response, int page) {
        List<Appointment> appointments = response.getContent();
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
