package com.liarstudio.courierservice.API;

import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PackageAPI {

    @GET("/package")
    Call<List<Package>> loadList();

    @GET("/package")
    Call<Package> loadOne(@Query("pack_id") int id);

    @GET("package/courier/{courier_id}")
    Call<List<Package>> loadForCourier(@Path("courier_id") int id, @Query("status") int status);

    @GET("package/admin")
    Call<List<Package>> loadForAdmin();

    @DELETE("package/admin")
    Call<Void> delete(@Query("pack_id") int id);

    @POST("/package")
    Call<Void> add(@Body Package pack);


}
