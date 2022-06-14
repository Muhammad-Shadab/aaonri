package com.aaonri.app.data.classified.di

import com.aaonri.app.data.authentication.forgot_password.api.ForgotPasswordApi
import com.aaonri.app.data.authentication.forgot_password.repository.ForgotPasswordRepository
import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClassifiedModule {

    @Provides
    @Singleton
    fun provideClassifiedApi(
        retrofit: Retrofit.Builder
    ): ClassifiedApi =
        retrofit.build().create(ClassifiedApi::class.java)

    @Provides
    @Singleton
    fun provideClassifiedRepo(classifiedApi: ClassifiedApi) =
        ClassifiedRepository(classifiedApi)

}