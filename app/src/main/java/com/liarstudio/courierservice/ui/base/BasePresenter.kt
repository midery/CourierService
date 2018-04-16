package com.liarstudio.courierservice.ui.base

import android.support.v7.app.AppCompatActivity

abstract class BasePresenter<V : BaseView>(val view: V) {
}