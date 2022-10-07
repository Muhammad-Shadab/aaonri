package com.aaonri.app.data.authentication.register.di

import com.aaonri.app.data.authentication.register.api.CountriesApi
import com.aaonri.app.data.authentication.register.api.ProfilePicApi
import com.aaonri.app.data.authentication.register.api.RegistrationApi
import com.aaonri.app.data.authentication.register.api.ZipCodeApi
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object RegistrationModule {

    @Provides
    @ViewModelScoped
    fun providesRegistrationApi(@Named("RetrofitForGlobal") retrofit: Retrofit.Builder): RegistrationApi =
        retrofit.build().create(RegistrationApi::class.java)

    @Provides
    @ViewModelScoped
    fun providesCountryApi(okHttpClient: OkHttpClient): CountriesApi =
        Retrofit.Builder()
            .baseUrl("https://corona.lmao.ninja")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApi::class.java)

    @Provides
    @ViewModelScoped
    fun providesZipCodeApi(okHttpClient: OkHttpClient): ZipCodeApi =
        Retrofit.Builder()
            .baseUrl("https://api.worldpostallocations.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZipCodeApi::class.java)

    /*@Provides
    @ViewModelScoped
    fun providesProfilePicApi(@Named("RetrofitForScalerConverter") retrofit: Retrofit.Builder): ProfilePicApi =
        retrofit.build().create(ProfilePicApi::class.java)*/

    @Provides
    @ViewModelScoped
    fun provideRegistrationRepository(
        registrationApi: RegistrationApi,
        countriesApi: CountriesApi,
        zipCodeApi: ZipCodeApi
    ) =
        RegistrationRepository(registrationApi, countriesApi, zipCodeApi)


}