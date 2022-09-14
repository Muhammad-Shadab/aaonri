package com.aaonri.app.data.immigration.di

import com.aaonri.app.data.immigration.api.ImmigrationApi
import com.aaonri.app.data.immigration.repository.ImmigrationRepository
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
object ImmigrationModule {

    @Provides
    @ViewModelScoped
    fun provideImmigrationApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): ImmigrationApi =
        retrofit.build().create(ImmigrationApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideImmigrationRepository(immigrationApi: ImmigrationApi) =
        ImmigrationRepository(immigrationApi)

}