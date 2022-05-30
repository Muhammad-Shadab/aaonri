package com.aaonri.app.data.authentication.register.di

import com.aaonri.app.data.authentication.register.api.CountriesApi
import com.aaonri.app.data.authentication.register.api.RegistrationApi
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.aaonri.app.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RegistrationModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun providesRegistrationApi(retrofit: Retrofit.Builder): RegistrationApi =
        retrofit.build().create(RegistrationApi::class.java)

    @Provides
    @Singleton
    fun providesCountryApi(): CountriesApi =
        Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApi::class.java)


    @Provides
    @Singleton
    fun provideRegistrationRepository(
        registrationApi: RegistrationApi,
        countriesApi: CountriesApi
    ) =
        RegistrationRepository(registrationApi, countriesApi)


}