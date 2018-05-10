package com.liarstudio.courierservice.injection.app

import android.app.Activity
import android.app.Application
import android.app.Fragment
import com.orm.SugarContext
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasFragmentInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector, HasFragmentInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .create()
                .inject(this)
        SugarContext.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> =
            activityInjector

    override fun fragmentInjector(): AndroidInjector<Fragment> =
            fragmentInjector

    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }

}