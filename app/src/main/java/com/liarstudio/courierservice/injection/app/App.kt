package com.liarstudio.courierservice.injection.app

import android.app.Activity
import android.app.Application
import com.orm.SugarContext
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {
    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .create()
                .inject(this)
        SugarContext.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> =
            activityInjector


    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }

}