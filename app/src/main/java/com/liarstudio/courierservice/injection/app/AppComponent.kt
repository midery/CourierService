package com.liarstudio.courierservice.injection.app

import com.liarstudio.courierservice.injection.logic.*
import com.liarstudio.courierservice.injection.scope.PerApplication
import com.liarstudio.courierservice.injection.screen.ActivityBuilder
import com.liarstudio.courierservice.logic.auth.AuthLoader
import com.liarstudio.courierservice.logic.pack.PackageLoader
import com.liarstudio.courierservice.logic.user.UserLoader
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Компонент приложения
 * Содержит модули logic и ui-слоев, а так же дополнительные зависимости
 * dagger-библиотеки. Действует как singleton
 */
@PerApplication
@Component(modules = [
    AppModule::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    StorageModule::class,
    AuthModule::class,
    PackageModule::class,
    UserModule::class,
    ActivityBuilder::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()

    fun authLoader(): AuthLoader
    fun packageLoader(): PackageLoader
    fun userLoader(): UserLoader
}