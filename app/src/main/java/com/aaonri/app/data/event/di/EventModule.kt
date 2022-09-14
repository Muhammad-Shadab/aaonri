package com.aaonri.app.data.event.di

import com.aaonri.app.data.authentication.register.api.ZipCodeApi
import com.aaonri.app.data.event.api.EventApi
import com.aaonri.app.data.event.repository.EventRepository
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
object EventModule {

    @Provides
    @ViewModelScoped
    fun provideEventApi(@Named("RetrofitForGlobal") retrofit: Retrofit.Builder): EventApi =
        retrofit.build().create(EventApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideEventRepository(eventApi: EventApi, zipCodeApi: ZipCodeApi) =
        EventRepository(eventApi, zipCodeApi)

}