package com.aaonri.app.data.home.di

import com.aaonri.app.data.home.api.HomeApi
import com.aaonri.app.data.home.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @Provides
    @ViewModelScoped
    fun provideClassifiedApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): HomeApi =
        retrofit.build().create(HomeApi::class.java)


    @Provides
    @ViewModelScoped
    fun provideHomeRepo(homeApi: HomeApi) =
        HomeRepository(homeApi)

}