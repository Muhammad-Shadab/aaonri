package com.aaonri.app.data.jobs.seeker.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UploadResumeApi {
    @Multipart
    @POST("/api/v1/common/uploadResume")
    suspend fun uploadResume(
        @Query("jobOrProfileId") jobProfileId: Int,
        @Query("jobProfile") jobProfile: Boolean,
        @Part file: MultipartBody.Part
    ): Response<String>
}