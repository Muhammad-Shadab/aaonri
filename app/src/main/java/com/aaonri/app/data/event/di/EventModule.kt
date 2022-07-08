package com.aaonri.app.data.event.di

import com.aaonri.app.data.event.api.EventApi
import com.aaonri.app.data.event.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {

    @Provides
    @Singleton
    fun provideEventApi(retrofit: Retrofit.Builder): EventApi =
        retrofit.build().create(EventApi::class.java)

    @Provides
    @Singleton
    fun provideEventRepository(eventApi: EventApi) = EventRepository(eventApi)

}