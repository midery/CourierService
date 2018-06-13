package com.liarstudio.courierservice.injection.screen.activity

import android.support.v7.app.AppCompatActivity
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import dagger.Binds
import dagger.Module

/**
 * Модуль экрана авторизации [AuthActivity]
 */
@Module
abstract class AuthActivityModule {

    /**
     * Метод, привязывающий [AuthActivity] к [AppCompatActivity]
     */
    @Binds
    @PerActivity
    abstract fun provideAppCompatActivity(activity: AuthActivity): AppCompatActivity
}