package com.liarstudio.courierservice.injection.app

import com.liarstudio.courierservice.injection.logic.AuthModule
import com.liarstudio.courierservice.injection.logic.NetworkModule
import com.liarstudio.courierservice.injection.logic.PackageModule
import com.liarstudio.courierservice.injection.scope.PerApplication
import com.liarstudio.courierservice.injection.screen.ScreenBuilder
import com.liarstudio.courierservice.logic.auth.AuthLoader
import com.liarstudio.courierservice.logic.pack.PackageLoader
import dagger.Component
import dagger.android.AndroidInjectionModule

@PerApplication
@Component(modules = [
    AppModule::class,
    AndroidInjectionModule::class,
    NetworkModule::class,
    AuthModule::class,
    PackageModule::class,
    ScreenBuilder::class
])
interface AppComponent{
    fun inject(app: App)
    fun authLoader(): AuthLoader
    fun packageLoader(): PackageLoader
}