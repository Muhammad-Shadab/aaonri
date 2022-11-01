package com.aaonri.app.data.jobs.recruiter.api

import com.aaonri.app.data.jobs.recruiter.model.*
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileRequest
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileResponse
import com.aaonri.app.data.jobs.seeker.model.UserJobProfileResponse
import retrofit2.Response
import retrofit2.http.*

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

    @GET("/api/v1/jobprofile/findByEmailIdAndApplicantFlag")
    suspend fun getUserConsultantProfile(
        @Query("emailId") emailId: String,
        @Query("isApplicant") isApplicant: Boolean
    ): Response<UserJobProfileResponse>

    @POST("/api/v1/jobs/search")
    suspend fun jobSearchApi(
        @Body jobSearchRequest: JobSearchRequest
    ): Response<JobSearchResponse>

    @POST("/api/v1/jobprofile/add")
    suspend fun addConsultantProfile(
        @Body addJobProfileRequest: AddJobProfileRequest
    ): Response<AddJobProfileResponse>

    @PUT("/api/v1/jobprofile/update/{consultantProfileId}")
    suspend fun updateConsultantProfile(
        @Path("consultantProfileId") consultantProfileId: Int,
        @Body updateJobProfileRequest: AddJobProfileRequest
    ): Response<AddJobProfileResponse>

    //@POST("")


}