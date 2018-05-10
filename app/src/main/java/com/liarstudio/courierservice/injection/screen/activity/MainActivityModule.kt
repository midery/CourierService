package com.liarstudio.courierservice.injection.screen.activity

import android.support.v7.app.AppCompatActivity
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.screen.main.MainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @Binds
    @PerScreen
    abstract fun provideAppCompatActivity(activity: MainActivity): AppCompatActivity
}