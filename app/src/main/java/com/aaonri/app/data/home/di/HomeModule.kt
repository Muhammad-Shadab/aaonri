package com.aaonri.app.data.home.di

import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.api.PostClassifiedApi
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import com.aaonri.app.data.home.api.HomeApi
import com.aaonri.app.data.home.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideClassifiedApi(
        retrofit: Retrofit.Builder
    ): HomeApi =
        retrofit.build().create(HomeApi::class.java)


    @Provides
    @Singleton
    fun provideHomeRepo(homeApi: HomeApi) =
        HomeRepository(homeApi)

}