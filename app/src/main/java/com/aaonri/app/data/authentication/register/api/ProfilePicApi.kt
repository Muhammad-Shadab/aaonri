package com.aaonri.app.data.authentication.register.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfilePicApi {

    @Multipart
    @POST("/api/v1/common/uploadProfilePic")
    suspend fun uploadProfilePic(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
    ): Response<String>
}