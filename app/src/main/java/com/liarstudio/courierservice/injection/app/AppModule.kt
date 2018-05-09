package com.liarstudio.courierservice.injection.app

import android.app.Application
import android.content.Context
import com.liarstudio.courierservice.injection.scope.PerApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: Application) {

    @PerApplication
    @Provides
    fun provideContext(): Context {
        return app
    }
}