package com.aaonri.app.data.jobs.recruiter.api

import com.aaonri.app.data.jobs.recruiter.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JobRecruiterApi {

    @GET("/api/v1/jobprofile/all")
    suspend fun getAllJobProfile(): Response<AllJobProfileResponse>

    @GET("/api/v1/jobs/findAllActiveIndustryType")
    suspend fun findAllActiveIndustry(): Response<AllActiveIndustryResponse>

    @GET("/api/v1/jobs/findAllActiveExperienceLevel")
    suspend fun findAllActiveExperienceLevel(): Response<AllActiveExperienceLevelResponse>

    @GET("/api/v1/jobs/findAllActiveJobApplicability")
    suspend fun findAllActiveJobApplicability(): Response<AllActiveJobApplicabilityResponse>

    @GET("/api/v1/jobprofile/findById/{jobProfileId}")
    suspend fun findJobProfileById(
        @Path("jobProfileId") jobProfileId: Int
    ): Response<JobProfileDetailsByIdResponse>

    @GET("/api/v1/jobs/{jobId}")
    suspend fun findJobDetailsById(
        @Path("jobId") jobId: Int
    ): Response<JobDetails>

    @POST("/api/v1/jobs/search")
    suspend fun jobSearchApi(
        @Body jobSearchRequest: JobSearchRequest
    ): Response<JobSearchResponse>

    //@POST("")


}