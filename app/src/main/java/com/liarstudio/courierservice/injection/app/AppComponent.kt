package com.liarstudio.courierservice.injection.app

import com.liarstudio.courierservice.injection.logic.AuthModule
import com.liarstudio.courierservice.injection.logic.NetworkModule
import com.liarstudio.courierservice.injection.scope.PerApplication
import com.liarstudio.courierservice.injection.screen.ActivityBuilder
import com.liarstudio.courierservice.logic.user.AuthLoader
import dagger.Component
import dagger.android.AndroidInjectionModule

@PerApplication
@Component(modules = [
    AppModule::class,
    AndroidInjectionModule::class,
    NetworkModule::class,
    AuthModule::class,
    ActivityBuilder::class
])
interface AppComponent{
    fun inject(app: App)
    fun authLoader(): AuthLoader
}