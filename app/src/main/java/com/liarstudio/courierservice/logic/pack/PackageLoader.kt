package com.liarstudio.courierservice.logic.pack

import com.liarstudio.courierservice.entitiy.pack.Pack
import javax.inject.Inject

/**
 * Loader репозитория
 */
class PackageLoader @Inject constructor(
        private val packageRepo: PackageRepo
) {
    /**
     * Получение посылки по id
     *
     * @param id идентификатор посылки
     *
     * @return посылка
     */
    fun getPackage(id: Long) = packageRepo.getPackage(id)

    /**
     * Получение всех посылок пользователя
     *
     * @param id идентификатор пользователя
     * @param statuses статусы посылок, которые необходимо получить
     *
     * @return список посылок
     */
    fun getUserPackages(id: Long, vararg statuses: Int) = packageRepo.getUserPackages(id, *statuses)

    /**
     * Удаление посылки по id
     *
     * @param id идентификатор посылки
     *
     * @return Completable
     */
    fun delete(id: Long) = packageRepo.delete(id)

    /**
     * Добавление или изменение посылки
     *
     * @param p посылка
     *
     * @return Completable
     */
    fun add(p: Pack) = packageRepo.add(p)

}