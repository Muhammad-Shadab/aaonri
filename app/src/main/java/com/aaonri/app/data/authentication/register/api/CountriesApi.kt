package com.aaonri.app.data.authentication.register.api

import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import retrofit2.http.GET

interface CountriesApi {

    @GET("/v2/countries")
    suspend fun getCountriesList(): CountriesResponse

}