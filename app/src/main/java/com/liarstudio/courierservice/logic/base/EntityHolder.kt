package com.liarstudio.courierservice.logic.base

interface EntityHolder<T> {
    fun toEntity(): T
}