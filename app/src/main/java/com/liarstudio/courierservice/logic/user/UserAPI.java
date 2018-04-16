package com.liarstudio.courierservice.API;

import com.liarstudio.courierservice.entities.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserAPI {

    //Попытка входа пользователя в систему. При удачном исходе будет возвращен экземпляр класса пользователь
    @GET("/auth")
    Call<User> login(@Query("email") String email, @Query("password") String password);


    //Попытка создания пользователя. При удачном исходе будет возвращен созданный пользователь
    @FormUrlEncoded
    @POST("/auth")
    Call<User> register(
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password);



    //Получение списка всех пользователей с указанной в параметрах ролью
    @GET("/users")
    Call<List<User>> loadUsers(@Query("role") int role);


    //Получение пользователя с заданным id
    @GET("/users/{id}")
    Call<User> loadUser(@Path("id") int id);


}
