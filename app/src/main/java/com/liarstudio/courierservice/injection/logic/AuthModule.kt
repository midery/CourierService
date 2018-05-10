package com.liarstudio.courierservice.injection.logic

import com.liarstudio.courierservice.logic.auth.AuthApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @Provides
    fun provideAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)
}