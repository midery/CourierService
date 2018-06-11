package com.liarstudio.courierservice.logic.pack

import com.liarstudio.courierservice.logic.UrlPackage
import com.liarstudio.courierservice.logic.UrlPackage.GET_USER_PACKAGES
import com.liarstudio.courierservice.logic.pack.request.PackageRequest
import com.liarstudio.courierservice.logic.pack.response.PackResponse
import io.reactivex.Completable

import io.reactivex.Observable
import retrofit2.http.*

interface PackageApi {

    //Получаем список всех посылок
    @GET(UrlPackage.GET_PACKAGES)
    fun getPackages(): Observable<List<PackResponse>>

    //Получаем одну посылку по id
    @GET(UrlPackage.GET_PACKAGE)
    fun getPackage(@Path("package_id") id: Long): Observable<PackResponse>

    //Получаем все посылки по id курьера и статусу(либо новая, либо нет)
    @GET(GET_USER_PACKAGES)
    fun getUserPackages(
            @Path("user_id") id: Long,
            @Query("status") vararg statuses: Int
    ): Observable<List<PackResponse>>

    //Удаляем посылку
    @DELETE(UrlPackage.BASE_PACKAGE_URL)
    fun delete(@Path("package_id") id: Long): Completable

    //Добавляем посылку
    @POST(UrlPackage.BASE_PACKAGE_URL)
    fun add(@Body packageRequest: PackageRequest): Completable
}
