package com.liarstudio.courierservice.logic.pack

import com.liarstudio.courierservice.logic.UrlPackage
import com.liarstudio.courierservice.logic.UrlPackage.GET_USER_PACKAGES
import com.liarstudio.courierservice.logic.pack.request.PackageRequest
import com.liarstudio.courierservice.logic.pack.response.PackResponse
import io.reactivex.Completable

import io.reactivex.Observable
import retrofit2.http.*

interface PackageApi {

    /**
     * Получение посылки по id
     *
     * @param id идентификатор посылки
     *
     * @return Observable с посылкой
     */
    @GET(UrlPackage.GET_PACKAGE)
    fun getPackage(@Path("package_id") id: Long): Observable<PackResponse>

    /**
     * Получение всех посылок пользователя
     *
     * @param id идентификатор пользователя
     * @param statuses статусы посылок, которые необходимо получить
     *
     * @return Observable со списком посылок
     */
    @GET(GET_USER_PACKAGES)
    fun getUserPackages(
            @Path("user_id") id: Long,
            @Query("status") vararg statuses: Int
    ): Observable<List<PackResponse>>

    /**
     * Удаление посылки по id
     *
     * @param id идентификатор посылки
     *
     * @return Completable
     */
    @DELETE(UrlPackage.BASE_PACKAGE_URL)
    fun delete(@Path("package_id") id: Long): Completable

    /**
     * Добавление или изменение посылки
     *
     * @param packageRequest маппинг-модель посылки
     *
     * @return Completable
     */
    @POST(UrlPackage.BASE_PACKAGE_URL)
    fun add(@Body packageRequest: PackageRequest): Completable
}
