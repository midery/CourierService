package com.liarstudio.courierservice.logic.pack

import com.liarstudio.courierservice.entitiy.pack.Package
import com.liarstudio.courierservice.logic.UrlPackage
import io.reactivex.Completable

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import io.reactivex.Observable

interface PackageApi {

    //Получаем список всех посылок
    @GET(UrlPackage.GET_PACKAGES)
    fun getPackages(): Observable<List<Package>>

    //Получаем одну посылку по id
    @GET(UrlPackage.GET_PACKAGE)
    fun getPackage(@Path("package_id") id: Long): Observable<Package>

    //Получаем все посылки по id курьера и статусу(либо новая, либо нет)
    @GET("package/courier/{courier_id}")
    fun getCourierPackages(@Path("courier_id") id: Long): Observable<List<Package>>

    @GET("package/courier/new/{courier_id}")
    fun getNewCourierPackages(@Path("courier_id") id: Long): Observable<List<Package>>

    //Получаем посылки для администратора(со статусом "Новая"/"Отклоненная"/"Завершенная"
    @GET(UrlPackage.GET_PACKAGES_ADMIN)
    fun getAdminPackages(): Observable<List<Package>>

    //Удаляем посылку
    @DELETE(UrlPackage.DELETE_PACKAGE)
    fun delete(@Path("package_id") id: Long): Completable

    //Добавляем посылку
    @POST(UrlPackage.BASE_PACKAGE_URL)
    fun add(@Body pack: Package): Completable
}
