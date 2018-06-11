package com.liarstudio.courierservice.ui.base.screen

/**
 * Перечисление, показывающее состояние загрузки экрана
 *
 */
enum class LoadState {
    NONE, //Стандартное состояние, загрузки нет
    LOADING, //Загрузка данных
    ERROR //Состояние ошибки, загрузки нет
}