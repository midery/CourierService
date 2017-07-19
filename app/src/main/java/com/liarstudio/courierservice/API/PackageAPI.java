package com.liarstudio.courierservice.API;

import com.liarstudio.courierservice.BaseClasses.Package;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by M1DERY on 20.07.2017.
 */

public interface PackageAPI {

    @FormUrlEncoded
    @POST("/package")
    Call<Package> load(@Body Package pkg);

}
