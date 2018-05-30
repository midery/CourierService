package com.liarstudio.courierservice.injection.app

import android.content.Context
import com.liarstudio.courierservice.injection.logic.AuthModule
import com.liarstudio.courierservice.injection.logic.NetworkModule
import com.liarstudio.courierservice.injection.logic.PackageModule
import com.liarstudio.courierservice.injection.logic.UserModule
import com.liarstudio.courierservice.injection.scope.PerApplication
import com.liarstudio.courierservice.injection.screen.ActivityBuilder
import com.liarstudio.courierservice.injection.screen.fragment.FragmentBuilder
import com.liarstudio.courierservice.logic.auth.AuthLoader
import com.liarstudio.courierservice.logic.pack.PackageLoader
import com.liarstudio.courierservice.logic.user.UserLoader
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


@PerApplication
@Component(modules = [
    AppModule::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    AuthModule::class,
    PackageModule::class,
    UserModule::class,
    ActivityBuilder::class
])
interface AppComponent: AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()

    fun authLoader(): AuthLoader
    fun packageLoader(): PackageLoader
    fun userLoader(): UserLoader
}