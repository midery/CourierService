package com.liarstudio.courierservice.injection.screen

import com.liarstudio.courierservice.injection.app.AppComponent
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.ui.base.MessageShower
import dagger.Component

/**
 * Компонент Activity
 */
@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun snackController(): MessageShower
}