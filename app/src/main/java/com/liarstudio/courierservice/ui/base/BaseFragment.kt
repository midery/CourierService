package com.liarstudio.courierservice.ui.base

import android.app.Fragment
import android.os.Bundle
import dagger.android.AndroidInjection

abstract class BaseFragment<T : BaseScreenModel>: Fragment(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
    abstract fun render(screenModel: T)
}