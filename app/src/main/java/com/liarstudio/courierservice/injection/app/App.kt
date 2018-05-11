package com.liarstudio.courierservice.injection.app

import android.app.Activity
import android.app.Application
import android.app.Fragment
import com.orm.SugarContext
import dagger.android.support.DaggerApplication
import javax.inject.Inject
import dagger.android.AndroidInjector



class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out App> {
        return DaggerAppComponent.builder().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        SugarContext.init(this)
    }


    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }

}