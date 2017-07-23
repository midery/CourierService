package com.liarstudio.courierservice.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by M1DERY on 19.07.2017.
 */

public interface UserAPI {
    @GET("/auth")
    Call<User> login(@Query("email") String email, @Query("password") String password);

    @FormUrlEncoded
    @POST("/auth")
    Call<User> register(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password);

    @GET("/users")
    Call<List<User>> loadUsers(@Query("role") int role);

    @GET("/users/{id}")
    Call<User> loadUser(@Path("id") int id);


}
