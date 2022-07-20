package com.aaonri.app.data.home.di

import com.aaonri.app.data.home.api.HomeApi
import com.aaonri.app.data.home.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideClassifiedApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): HomeApi =
        retrofit.build().create(HomeApi::class.java)


    @Provides
    @Singleton
    fun provideHomeRepo(homeApi: HomeApi) =
        HomeRepository(homeApi)

}