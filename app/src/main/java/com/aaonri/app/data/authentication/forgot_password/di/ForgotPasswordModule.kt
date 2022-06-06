package com.aaonri.app.data.authentication.forgot_password.di

import com.aaonri.app.data.authentication.forgot_password.api.ForgotPasswordApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Retrofit

/*

@Module
@InstallIn(ActivityComponent::class)
object ForgotPasswordModule {

    @Provides
    @ActivityScoped
    fun provideForgotPasswordApi(
        retrofit: Retrofit.Builder
    ): ForgotPasswordApi =
        retrofit.build().create(ForgotPasswordApi::class.java)

    @Provides
    @ActivityScoped
    fun provideForgotPasswordRepo(forgotPasswordApi: ForgotPasswordApi) =
        ForgotPasswordRepository(forgotPasswordApi)



}*/
