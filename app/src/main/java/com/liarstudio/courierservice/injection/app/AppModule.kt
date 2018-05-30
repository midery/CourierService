package com.liarstudio.courierservice.injection.app

import android.app.Application
import android.content.Context
import com.liarstudio.courierservice.injection.scope.PerApplication
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule(private val app: Application) {
    @Binds
    abstract fun provideContext(app: App): Context
}