package com.aaonri.app.data.jobs.seeker.api

import com.aaonri.app.data.jobs.seeker.model.*
import retrofit2.Response
import retrofit2.http.*

interface JobSeekerApi {

    @GET("/api/v1/jobs/allActiveJobs")
    suspend fun getAllActiveJobs(): Response<AllJobsResponse>

    @GET("/api/v1/jobs/{jobId}")
    suspend fun getJobDetails(
        @Path("jobId") jobId: Int
    ): Response<JobDetailResponse>

    @GET("/api/v1/jobs/findAllActiveExperienceLevel")
    suspend fun getAllActiveExperienceLevel(): Response<ExperienceLevelResponse>

    @GET("/api/v1/jobs/findAllActiveJobApplicability")
    suspend fun getAllActiveJobApplicability(): Response<AllActiveJobApplicabilityResponse>

    @GET("/api/v1/jobs/findAllActiveAvailability")
    suspend fun getAllActiveAvailability(): Response<ActiveJobAvailabilityResponse>

    @GET("/api/v1/jobprofile/findByEmailIdAndApplicantFlag")
    suspend fun getUserJobProfileByEmail(
        @Query("emailId") emailId: String,
        @Query("isApplicant") isApplicant: Boolean
    ): Response<UserJobProfileResponse>

    @PUT("/api/v1/jobprofile/update/{profileId}")
    suspend fun updateJobProfile(
        @Path("profileId") profileId: Int,
        @Body addJobProfileRequest: AddJobProfileRequest
    ): Response<AddJobProfileResponse>

    @POST("/api/v1/jobprofile/add")
    suspend fun addJobProfile(
        @Body addJobProfileRequest: AddJobProfileRequest
    ): Response<AddJobProfileResponse>

    @POST("/api/v1/jobs/applyJob")
    suspend fun applyJob(
        @Body applyJobRequest: ApplyJobRequest
    ): Response<ApplyJobResponse>

    @POST("/api/v1/jobs/saveJobView")
    suspend fun saveJobView(
        @Body saveJobViewRequest: SaveJobViewRequest
    ): Response<SaveJobViewRequest>

}