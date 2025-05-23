package com.lph.selfcareapp.serviceAPI;

import com.lph.selfcareapp.dto.IntrospectRequest;
import com.lph.selfcareapp.dto.LoginRequest;
import com.lph.selfcareapp.dto.MedicalReport;
import com.lph.selfcareapp.model.ApiResponse;
import com.lph.selfcareapp.model.Appointment;
import com.lph.selfcareapp.model.Appointment2;
import com.lph.selfcareapp.model.CallDoctor;
import com.lph.selfcareapp.model.Clinic;
import com.lph.selfcareapp.model.ClinicList;
import com.lph.selfcareapp.model.Doctor;
import com.lph.selfcareapp.model.IntrospectResponse;
import com.lph.selfcareapp.model.IntrospectResult;
import com.lph.selfcareapp.model.LoginResponse;
import com.lph.selfcareapp.model.Page;
import com.lph.selfcareapp.model.Patient;
import com.lph.selfcareapp.model.PatientBody;
import com.lph.selfcareapp.model.Reschedule;
import com.lph.selfcareapp.model.Result;
import com.lph.selfcareapp.model.ReturnData;
import com.lph.selfcareapp.model.ScheduleTime;
import com.lph.selfcareapp.model.SlotDTO;
import com.lph.selfcareapp.model.SpecialtyList;
import com.lph.selfcareapp.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("edoc/user/update-session-key")
    Call<ApiResponse<User>> updateSessionKey(@Query("email") String email,
                                            @Query("sessionKey") String sessionKey);

    @GET("edoc/appointments/hasReport")
    Call<ApiResponse<Boolean>> hasReport(@Query("appoid") int appoid);

    @POST("edoc/patient/update-fcm-token")
    Call<ApiResponse<Patient>> updateToken(@Query("pid") int pid,
                                           @Query("fcmToken") String token);

    @GET("edoc/schedule/slots")
    Call<List<SlotDTO>> getSlots(@Query("docid") int doctorId);

    @GET("edoc/clinics/2")
    Single<Page<Clinic>> getPageClinics(@Query("name") String name,
                                    @Query("longitude") Double longitude,
                                    @Query("latitude") Double latitude,
                                    @Query("page") int page,
                                    @Query("size") int size,
                                    @Query("sortBy") String sortBy,
                                    @Query("direction") String direction);

    @GET("edoc/appointments/filter")
    Single<Page<Appointment>> getPageAppointment(@Query("pid") Integer pid,
                                                 @Query("appoid") Integer appoid,
                                                 @Query("startDate") String startDate,
                                                 @Query("endDate") String endDate,
                                                 @Query("hasDone") Integer hasDone,
                                                 @Query("docId") Integer docid,
                                                 @Query("page") int page,
                                                 @Query("size") int size,
                                                 @Query("sortBy") String sortBy,
                                                 @Query("direction") String direction);

    @GET("edoc/doctor/get2")
    Single<Page<Doctor>> getDoctors(@Query("name") String name,
                                    @Query("clinicId") Integer clinicId,
                                    @Query("spe") String spe,
                                    @Query("page") int page,
                                    @Query("size") int size,
                                    @Query("sortBy") String sortBy,
                                    @Query("direction") String direction);

    @POST("/edoc/otp/send")
    Call<ApiResponse<String>> sendOtp(@Query("email") String email); // <String>>>

    @POST("edoc/otp/verify")
    Call<LoginResponse> verifyOtp(@Query("email") String email, @Query("otp") String otp); // <LoginResponse>

    @POST("/edoc/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/edoc/auth/fingerprint")
    Call<LoginResponse> loginFingerprint(@Query("email") String email, @Query("sessionKey") String sessionKey);

    @POST("/edoc/patient")
    Call<ApiResponse<Patient>> signUp(@Body PatientBody patient); // <Patient>

    @GET("edoc/clinics")
    Call<ClinicList> getAllClinics();

    @GET("edoc/specialty")
    Call<SpecialtyList> getAllSpecialties();

    @GET("edoc/doctor")
    Call<List<Doctor>> getAllDoctor(@Query("speId") int clinicId,
                                         @Query("clinicId") int speId);

    @GET("edoc/schedule")
    Call<List<ScheduleTime>> getScheduleTime(@Query("docid") int doctorId,
                                             @Query("date") String date);

    @POST("edoc/vnpay/create-payment")
    Call<ReturnData> createPayment(@Query("amount") int amount,
                                    @Query("order_info") String orderInfo);

    @GET("edoc/appointments")
    Call<List<Appointment>> getAppointment(@Query("pid") int pid);

    @GET("edoc/appointments")
    Call<List<Appointment>> getAppointment2(@Query("docid") int docid);


    @POST("edoc/appointments/diagnose")
    Call<ResponseBody> diagnose(@Query("appoid") int appoid);

    @POST("edoc/appointments/uploadReport")
    Call<ApiResponse<Appointment>> uploadDiag(@Query("appoid") int appoid,
                            @Body MedicalReport report);

    @FormUrlEncoded
    @POST("api/1/upload")
    Call<String> getImageUrl(@Field("key") String key,
                             @Field("source") String source,
                             @Field("format") String format);

    @FormUrlEncoded
    @POST("edoc/appointment/uploadImage")
    Call<Result> uploadImage(@Field("image") String image,
                             @Field("appoid") int appoid);

    @GET("edoc/appointments/result")
    Call<List<Appointment>> getResult(@Query("pid") int pid);

    @GET("edoc/doctor/call")
    Call<List<CallDoctor>> getCallDoctor(@Query("pid") int pid);

    @POST("edoc/reschedule")
    Call<ApiResponse<Reschedule>> insertDate(@Query("pid") int pid,
                            @Query("docid") int docid,
                            @Query("date") String date);

    @GET("edoc/reschedule")
    Call<List<Reschedule>> showreschedule(@Query("pid") int pid);

    @POST("edoc/auth/introspect")
    Call<IntrospectResponse> checkToken(@Body IntrospectRequest token);
}
