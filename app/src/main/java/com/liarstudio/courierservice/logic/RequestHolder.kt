package com.liarstudio.courierservice.logic

interface RequestHolder<T> {
    fun from(entity: T): RequestHolder<T>
}