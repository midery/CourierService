package com.liarstudio.courierservice.injection.app

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

/**
 * Модуль приложения, предоставляющий контекст
 */
@Module
abstract class AppModule(private val app: Application) {
    @Binds
    abstract fun provideContext(app: App): Context
}