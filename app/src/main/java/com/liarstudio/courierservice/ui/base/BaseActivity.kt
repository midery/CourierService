package com.liarstudio.courierservice.ui.base

import android.support.v7.app.AppCompatActivity

abstract class BaseActivity<T : BaseScreenModel>: AppCompatActivity(), BaseView {

    abstract fun render(screenModel: T)
}