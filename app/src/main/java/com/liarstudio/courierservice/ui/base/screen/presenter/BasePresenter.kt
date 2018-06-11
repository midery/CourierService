package com.liarstudio.courierservice.ui.base.screen.presenter

import com.liarstudio.courierservice.ui.base.screen.view.BaseView
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.schedulers.Schedulers

/**
 * Базовый Presenter
 * Содержит BaseView, а так же методы подписки на различные
 * действия с потоками данных.
 */
abstract class BasePresenter<V : BaseView>(val view: V) {

    /**
     * Функция, которая выполнится при создании и инициализации представления
     */
    open fun viewCreated() { }

    /**
     * Подписка на определенное событие, ожидание которой осуществляется
     * в побочном потоке, а обработка успешного выполнения или ошибки -
     * в главном.
     *
     * Функция предполагает наличие результирующего параметра T
     *
     * @param observable действие, на которое мы подписываемся
     * @param onNext действие, выполняемое при успешном исходе
     * @param onError действие при ошибке
     */
    protected open fun <T> subscribe(
            observable: Observable<T>,
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit
    ): Disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                    LambdaObserver(onNext, onError, {}, {})
            )

    /**
     * Подписка на определенное событие, ожидание которой осуществляется
     * в побочном потоке, а обработка успешного выполнения или ошибки -
     * в главном.
     *
     * Это перегруженная версия [subscribe], выполняемая без обработки ошибки.
     *
     * @param observable действие, на которое мы подписываемся
     * @param onNext действие, выполняемое при успешном исходе
     */
    protected open fun <T> subscribe(
            observable: Observable<T>,
            onNext: (t: T) -> Unit
    ): Disposable = subscribe(observable, onNext, {})


    /**
     * Подписка на определенное событие, ожидание которой осуществляется
     * в побочном потоке, а обработка успешного выполнения или ошибки -
     * в главном.
     *
     * Это перегруженная версия [subscribe], выполняемая для функций
     * без возвращаемого типа
     *
     * @param completable действие, на которое мы подписываемся
     * @param onComplete действие, выполняемое при успешном исходе
     * @param onError действие в случае ошибки
     */
    protected open fun subscribe(
            completable: Completable,
            onComplete: () -> Unit,
            onError: (Throwable) -> Unit
    ): Disposable = completable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)

    /**
     * Подписка на определенное событие, ожидание которой осуществляется
     * в побочном потоке, а обработка успешного выполнения или ошибки -
     * в главном.
     *
     * Это перегруженная версия [subscribe] с [onComplete],
     * выполняемая без обработки ошибок
     *
     * @param completable действие, на которое мы подписываемся
     * @param onComplete действие, выполняемое при успешном исходе
     */
    protected open fun subscribe(
            completable: Completable,
            onComplete: () -> Unit
    ): Disposable = subscribe(completable, onComplete, {})

}