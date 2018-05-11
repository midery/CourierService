package com.liarstudio.courierservice.ui.base.screen

import com.liarstudio.courierservice.ui.base.BaseView
import com.liarstudio.courierservice.ui.base.LoadState
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity<T : BaseScreenModel> : DaggerAppCompatActivity(), BaseView {

    abstract fun renderData(screenModel: T)

    abstract fun renderState(loadState: LoadState)

    fun render(screenModel: T) {
        renderState(screenModel.loadState)
        renderData(screenModel)
    }
}