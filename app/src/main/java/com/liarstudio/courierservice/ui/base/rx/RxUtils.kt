package com.liarstudio.courierservice.ui.base.rx

import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins

fun emptyAction() = {}

fun <T: Throwable> emptyError() = object: SafeConsumer<T> {
    override fun accept(t: T) { RxJavaPlugins.onError(OnErrorNotImplementedException(t)) } }