package com.aaonri.app.data.authentication.forgot_password.di

import com.aaonri.app.data.authentication.forgot_password.api.ForgotPasswordApi
import com.aaonri.app.data.authentication.forgot_password.repository.ForgotPasswordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ForgotPasswordModule {

    @Provides
    @Singleton
    fun provideForgotPasswordApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): ForgotPasswordApi =
        retrofit.build().create(ForgotPasswordApi::class.java)

    @Provides
    @Singleton
    fun provideForgotPasswordRepo(forgotPasswordApi: ForgotPasswordApi) =
        ForgotPasswordRepository(forgotPasswordApi)


}
