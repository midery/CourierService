package com.liarstudio.courierservice.ui.base

import android.support.annotation.StyleRes
import com.liarstudio.courierservice.ui.base.screen.BasePresenter

interface BaseView {

    fun requestPresenter(): BasePresenter<*>

    fun getLayout(): Int
}