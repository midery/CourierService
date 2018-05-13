package com.liarstudio.courierservice.logic

interface EntityHolder<T> {
    fun toEntity(): T
}