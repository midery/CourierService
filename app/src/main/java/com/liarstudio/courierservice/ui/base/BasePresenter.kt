package com.liarstudio.courierservice.ui.base

import com.liarstudio.courierservice.ui.base.rx.emptyAction
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.schedulers.Schedulers


abstract class BasePresenter<V : BaseView>(val view: V) {

    protected open fun <T> subscribe(
            observable: Observable<T>,
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit
    ): Disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                    LambdaObserver(onNext, onError, emptyAction(), {})
            )

    protected open fun <T> subscribe(
            observable: Observable<T>,
            onNext: (t: T) -> Unit
    ): Disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                    LambdaObserver(onNext, {}, emptyAction(), {})
            )
}