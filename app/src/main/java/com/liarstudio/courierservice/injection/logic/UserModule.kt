package com.liarstudio.courierservice.injection.logic

import com.liarstudio.courierservice.logic.auth.AuthApi
import com.liarstudio.courierservice.logic.user.UserApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UserModule {

    @Provides
    fun provideUserApi(retrofit: Retrofit) = retrofit.create(UserApi::class.java)
}