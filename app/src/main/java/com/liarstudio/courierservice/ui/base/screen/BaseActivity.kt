package com.liarstudio.courierservice.ui.base.screen

import android.os.Bundle
import android.os.PersistableBundle
import com.liarstudio.courierservice.ui.base.BaseView
import com.liarstudio.courierservice.ui.base.LoadState
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity<T : BaseScreenModel> : DaggerAppCompatActivity(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        requestPresenter().viewCreated()
    }

    abstract fun renderData(screenModel: T)

    abstract fun renderState(loadState: LoadState)

    fun render(screenModel: T) {
        renderState(screenModel.loadState)
        renderData(screenModel)
    }
}