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


    //Получаем список всех посылок
    @GET("/package")
    Call<List<Package>> loadList();

    //Получаем одну посылку по id
    @GET("/package")
    Call<Package> loadOne(@Query("pack_id") int id);

    //Получаем все посылки по id курьера и статусу(либо новая, либо нет)
    @GET("package/courier/{courier_id}")
    Call<List<Package>> loadForCourier(@Path("courier_id") int id, @Query("status") int status);

    //Получаем посылки для администратора(со статусом "Новая"/"Отклоненная"/"Завершенная"
    @GET("package/admin")
    Call<List<Package>> loadForAdmin();

    //Удаляем посылку
    @DELETE("package/admin")
    Call<Void> delete(@Query("pack_id") int id);

    //Добавляем посылку
    @POST("/package")
    Call<Void> add(@Body Package pack);


}
