package com.liarstudio.courierservice.logic.pack

import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.logic.base.toEntity
import com.liarstudio.courierservice.logic.base.toEntityCollection
import com.liarstudio.courierservice.logic.pack.request.PackageRequest
import javax.inject.Inject

/**
 * Репозиторий посылки [Package]
 */
class PackageRepo @Inject constructor(
        private val packageApi: PackageApi
) {
    /**
     * Получение посылки по id
     *
     * @param id идентификатор посылки
     *
     * @return посылка
     */
    fun getPackage(id: Long) = packageApi.getPackage(id).toEntity()

    /**
     * Получение всех посылок пользователя
     *
     * @param id идентификатор пользователя
     * @param statuses статусы посылок, которые необходимо получить
     *
     * @return список посылок
     */
    fun getUserPackages(id: Long, vararg statuses: Int) =
            packageApi.getUserPackages(id, *statuses).toEntityCollection()

    /**
     * Удаление посылки по id
     *
     * @param id идентификатор посылки
     *
     * @return Completable
     */
    fun delete(id: Long) = packageApi.delete(id)

    /**
     * Добавление или изменение посылки
     *
     * @param p посылка
     *
     * @return Completable
     */
    fun add(p: Pack) = packageApi.add(PackageRequest(p))

}