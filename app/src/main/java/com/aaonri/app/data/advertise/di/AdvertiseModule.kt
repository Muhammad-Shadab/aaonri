package com.aaonri.app.data.advertise.di

import com.aaonri.app.data.advertise.api.AdvertiseApi
import com.aaonri.app.data.advertise.api.CancelAdvertiseApi
import com.aaonri.app.data.advertise.repository.AdvertiseRepository
import com.aaonri.app.data.classified.api.PostClassifiedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object AdvertiseModule {

    @Provides
    @ViewModelScoped
    fun provideAdvertiseApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): AdvertiseApi =
        retrofit.build().create(AdvertiseApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideCancelAdvertiseApi(
        @Named("RetrofitForScalerConverter") retrofit: Retrofit.Builder
    ): CancelAdvertiseApi =
        retrofit.build().create(CancelAdvertiseApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideAdvertiseRepository(
        advertiseApi: AdvertiseApi,
        cancelAdvertiseApi: CancelAdvertiseApi
    ) = AdvertiseRepository(advertiseApi, cancelAdvertiseApi)

}