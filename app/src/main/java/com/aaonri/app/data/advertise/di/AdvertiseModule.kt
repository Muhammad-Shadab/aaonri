package com.aaonri.app.data.advertise.di

import com.aaonri.app.data.advertise.api.AdvertiseApi
import com.aaonri.app.data.advertise.repository.AdvertiseRepository
import com.aaonri.app.data.classified.api.PostClassifiedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdvertiseModule {

    @Provides
    @Singleton
    fun provideAdvertiseApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): AdvertiseApi =
        retrofit.build().create(AdvertiseApi::class.java)

    @Provides
    @Singleton
    fun provideAdvertiseRepository(
        advertiseApi: AdvertiseApi
    ) = AdvertiseRepository(advertiseApi)

}