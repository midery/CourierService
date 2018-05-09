package com.liarstudio.courierservice.ui.base.rx

import io.reactivex.functions.Consumer

interface SafeConsumer<T>: Consumer<T> {
    override fun accept(t: T)
}