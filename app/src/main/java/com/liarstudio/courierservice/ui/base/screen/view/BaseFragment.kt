package com.liarstudio.courierservice.ui.base.screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liarstudio.courierservice.ui.base.screen.model.BaseScreenModel
import com.liarstudio.courierservice.ui.base.screen.LoadState
import dagger.android.DaggerFragment

/**
 * Базовый класс фрагмента
 * Наследуется от [DaggerFragment] для Dependency Injection,
 * а так же от [BaseView] для участия в MVP
 *
 * Единственный способ связи View и Model здесь - метод [render],
 * при котором происходит перерисовка View со значениями из модели
 */
abstract class BaseFragment<T : BaseScreenModel> : DaggerFragment(), BaseView {

    /**
     * Переопределенный метод [DaggerFragment.onCreate]
     * для того, чтобы фрагмент сохранял * свое состояние при повороте экрана
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * Переопределенный метод [DaggerFragment.onCreateView], выполняемый для создания фрагмента
     * из файла дизайна и уведомления об этом presenter'а
     */
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(getLayout(),
                container, false)
        requestPresenter().viewCreated()
        return view
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