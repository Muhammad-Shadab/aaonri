package com.aaonri.app.data.home_filter.di

import com.aaonri.app.data.home_filter.api.HomeFilterApi
import com.aaonri.app.data.home_filter.repository.HomeFilterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object HomeFilterModule {

    @Provides
    @ViewModelScoped
    fun provideHomeFilterApi(@Named("RetrofitForGlobal") retrofit: Retrofit.Builder): HomeFilterApi =
        retrofit.build().create(HomeFilterApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideHomeFilterRepo(filterApi: HomeFilterApi) = HomeFilterRepository(filterApi)

}