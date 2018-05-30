package com.liarstudio.courierservice.ui.base.screen

import com.liarstudio.courierservice.ui.base.BaseView
import com.liarstudio.courierservice.ui.base.rx.emptyAction
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.schedulers.Schedulers


abstract class BasePresenter<V : BaseView>(val view: V) {

    open fun viewCreated() {

    }
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
    ): Disposable = subscribe(observable, onNext, {})

    protected open fun subscribe(
            completable: Completable,
            onComplete: () -> Unit
    ): Disposable = subscribe(completable, onComplete, {})

    protected open fun subscribe(
            completable: Completable,
            onComplete: () -> Unit,
            onError: (Throwable) -> Unit
    ): Disposable = completable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)
            //.subscribeWith( LambdaObserver({}, onError, onComplete, {}) )

}