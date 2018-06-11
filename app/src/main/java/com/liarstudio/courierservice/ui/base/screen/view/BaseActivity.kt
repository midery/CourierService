package com.liarstudio.courierservice.ui.base.screen.view

import android.os.Bundle
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.base.screen.model.BaseScreenModel
import dagger.android.support.DaggerAppCompatActivity

/**
 * Базовый класс Activity
 * Наследуется от [DaggerAppCompatActivity] для Dependency Injection,
 * а так же от [BaseView] для участия в MVP
 *
 * Единственный способ связи View и Model здесь - метод [render],
 * при котором происходит перерисовка View со значениями из модели
 */
abstract class BaseActivity<T : BaseScreenModel> : DaggerAppCompatActivity(), BaseView {

    /**
     * Переопределенный метод [DaggerAppCompatActivity.onCreate],
     * выполняемый для создания дизайна и уведомления об этом presenter'а
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        requestPresenter().viewCreated()
    }

    /**
     * Функция отрисовки данных модели
     *
     * @param screenModel модель экрана
     */
    abstract fun renderData(screenModel: T)

    /**
     * Функция отрисовки состояния загрузки модели
     *
     * @param loadState оостояние загрузки
     */
    abstract fun renderState(loadState: LoadState)

    /**
     * Функция полной отрисовки модели
     *
     * @param screenModel модель экрана
     */
    fun render(screenModel: T) {
        renderState(screenModel.loadState)
        renderData(screenModel)
    }
}