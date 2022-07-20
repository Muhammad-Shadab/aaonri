package com.aaonri.app.data.classified.di

import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.api.DeleteClassifiedApi
import com.aaonri.app.data.classified.api.PostClassifiedApi
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClassifiedModule {

    @Provides
    @Singleton
    fun provideClassifiedApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): ClassifiedApi =
        retrofit.build().create(ClassifiedApi::class.java)

    @Provides
    @Singleton
    fun providePostClassifiedApi(
        @Named("RetrofitForGlobal") retrofit: Retrofit.Builder
    ): PostClassifiedApi =
        retrofit.build().create(PostClassifiedApi::class.java)

    @Provides
    @Singleton
    fun provideDeleteClassifiedApi(
        @Named("RetrofitForScalerConverter") retrofit: Retrofit.Builder
    ): DeleteClassifiedApi =
        retrofit.build().create(DeleteClassifiedApi::class.java)

    @Provides
    @Singleton
    fun provideClassifiedRepo(
        classifiedApi: ClassifiedApi,
        postClassifiedApi: PostClassifiedApi,
        deleteClassifiedApi: DeleteClassifiedApi
    ) =
        ClassifiedRepository(classifiedApi, postClassifiedApi, deleteClassifiedApi)

}