package com.liarstudio.courierservice.injection.screen

import android.support.v7.app.AppCompatActivity
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.base.SnackController
import dagger.Module
import dagger.Provides


@Module
class ActivityModule(val activity: AppCompatActivity) {

    @Provides
    fun provideSnackController() = SnackController(activity)
}