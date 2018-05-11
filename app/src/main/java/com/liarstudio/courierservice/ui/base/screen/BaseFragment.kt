package com.liarstudio.courierservice.ui.base.screen

import android.os.Bundle
import com.liarstudio.courierservice.ui.base.BaseView
import com.liarstudio.courierservice.ui.base.LoadState
import dagger.android.DaggerFragment

abstract class BaseFragment<T : BaseScreenModel> : DaggerFragment(), BaseView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    abstract fun renderData(screenModel: T)

    abstract fun renderState(loadState: LoadState)

    fun render(screenModel: T) {
        renderState(screenModel.loadState)
        renderData(screenModel)
    }
}