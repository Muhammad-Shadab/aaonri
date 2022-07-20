package com.aaonri.app.data.classified.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteClassifiedApi {

    @DELETE("/api/v1/asd/delete/{classifiedId}")
    suspend fun deleteClassified(
        @Path("classifiedId") classifiedId: Int
    ): Response<String>

}