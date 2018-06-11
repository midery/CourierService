package com.liarstudio.courierservice.ui.base.screen.model

import com.liarstudio.courierservice.ui.base.screen.LoadState

/**
 * Базовая модель экрана
 * Хранит в себе состояние загрузки
 *
 * Все модели в MVP должны быть унаследованы от нее
 */
open class BaseScreenModel {
    var loadState = LoadState.NONE
}