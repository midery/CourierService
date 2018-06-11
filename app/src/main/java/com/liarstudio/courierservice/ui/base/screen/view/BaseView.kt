package com.liarstudio.courierservice.ui.base.screen.view

import com.liarstudio.courierservice.ui.base.screen.presenter.BasePresenter

/**
 * Интерфейс базового представления
 * Используется везде, где необходима работа паттерна MVP
 * Именно с этим классом работает Presenter. Чтобы View корректно им обрабатывалось,
 * необходимо унаследоваться от BaseView
 */
interface BaseView {

    /**
     * Функция, запрашивающая Presenter на раннем этапе инициализации
     * Необходима для скрытого вызова presenter.viewCreated, когда View будет создана
     *
     * @return presenter для данной View
     */
    fun requestPresenter(): BasePresenter<*>

    /**
     * Привязка XML-дизайна к view
     * Необходимо для упрощения работы с дизайном и уменьшения количества шаблонного кода
     */
    fun getLayout(): Int
}