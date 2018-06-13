package com.liarstudio.courierservice.logic.base

import io.reactivex.Observable

/**
 * Преобразует содержание Observable, которое наследует [Transformable]
 */
fun <T : EntityHolder<R>, R> Observable<T>.toEntity(): Observable<R> = map { it.toEntity() }

/**
 * Преобразует коллекцию внутри Observable, элементы которой являются [Transformable]
 */
fun <T : Collection<EntityHolder<R>>, R> Observable<T>.toEntityCollection(): Observable<List<R>> =
        map {
            it.map {
                it.toEntity()
            }
        }
