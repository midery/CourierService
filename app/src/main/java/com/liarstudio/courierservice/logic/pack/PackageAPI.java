package com.liarstudio.courierservice.API;

import com.liarstudio.courierservice.entities.pack.Package;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PackageAPI {


    //Получаем список всех посылок
    @GET("/package")
    Call<List<Package>> loadPackages();

    //Получаем одну посылку по id
    @GET("/package")
    Call<Package> loadPackages(@Query("pack_id") int id);

    //Получаем все посылки по id курьера и статусу(либо новая, либо нет)
    @GET("package/courier/{courier_id}")
    Call<List<Package>> loadCourierPackages(@Path("courier_id") int id, @Query("status") int status);

    //Получаем посылки для администратора(со статусом "Новая"/"Отклоненная"/"Завершенная"
    @GET("package/admin")
    Call<List<Package>> loadAdminPackages();

    //Удаляем посылку
    @DELETE("package/admin")
    Call<Void> delete(@Query("pack_id") int id);

    //Добавляем посылку
    @POST("/package")
    Call<Void> add(@Body Package pack);
}
