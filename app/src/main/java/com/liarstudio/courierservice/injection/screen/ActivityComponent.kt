package com.liarstudio.courierservice.injection.screen

import com.liarstudio.courierservice.injection.app.AppComponent
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.logic.user.AuthLoader
import dagger.Component

@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun authLoader(): AuthLoader
}