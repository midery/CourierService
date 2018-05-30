package com.liarstudio.courierservice.ui.base.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liarstudio.courierservice.ui.base.BaseView
import com.liarstudio.courierservice.ui.base.LoadState
import dagger.android.DaggerFragment

abstract class BaseFragment<T : BaseScreenModel> : DaggerFragment(), BaseView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(getLayout(), container, false)
        requestPresenter().viewCreated()
        return view
    }

    abstract fun renderData(screenModel: T)

    abstract fun renderState(loadState: LoadState)

    fun render(screenModel: T) {
        renderState(screenModel.loadState)
        renderData(screenModel)
    }
}