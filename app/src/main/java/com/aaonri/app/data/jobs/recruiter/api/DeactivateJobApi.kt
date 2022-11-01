package com.aaonri.app.data.jobs.recruiter.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DeactivateJobApi {

    @GET("/api/v1/jobs/changeJobStatus/{jobId}/{activeStatus}")
    suspend fun changeJobActiveStatus(
        @Path("jobId") jobId: Int,
        @Path("activeStatus") activeStatus: Boolean
    ): Response<String>

}