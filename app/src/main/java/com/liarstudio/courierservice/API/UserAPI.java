package com.liarstudio.courierservice.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by M1DERY on 19.07.2017.
 */

public interface UserAPI {
    @FormUrlEncoded
    @POST("/login")
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/register")
    Call<User> register(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password);



}
