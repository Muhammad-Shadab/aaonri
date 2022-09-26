package com.aaonri.app.data.main.di

import com.aaonri.app.data.main.api.MainApi
import com.aaonri.app.data.main.repository.MainRepository
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
object MainModule {

    @Provides
    @ViewModelScoped
    fun provideMainApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): MainApi =
        retrofit.build().create(MainApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideMainRepository1(
        mainApi: MainApi
    ) = MainRepository(mainApi)

}