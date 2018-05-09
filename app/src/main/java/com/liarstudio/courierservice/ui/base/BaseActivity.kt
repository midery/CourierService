package com.liarstudio.courierservice.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import dagger.Provides
import dagger.android.AndroidInjection

abstract class BaseActivity<T : BaseScreenModel>: AppCompatActivity(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
    abstract fun render(screenModel: T)
}