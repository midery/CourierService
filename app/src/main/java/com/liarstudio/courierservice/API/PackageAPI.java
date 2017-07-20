package com.liarstudio.courierservice.API;

import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PackageAPI {

    @GET("/package")
    Call<List<Package>> loadList(@Query("user_id") int id);

    @GET("/package")
    Call<Package> loadOne(@Query("pack_id") int id);

    @POST("/package")
    Call<List<Package>> add(@Body Package pack);
}
