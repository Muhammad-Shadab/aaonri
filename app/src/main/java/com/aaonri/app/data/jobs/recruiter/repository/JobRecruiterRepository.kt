package com.aaonri.app.data.jobs.recruiter.repository

import com.aaonri.app.data.jobs.recruiter.api.JobRecruiterApi
import com.aaonri.app.data.jobs.recruiter.model.JobSearchRequest
import retrofit2.http.Path
import javax.inject.Inject

class JobRecruiterRepository @Inject constructor(val jobRecruiterApi: JobRecruiterApi) {

    suspend fun getAllJobProfile() = jobRecruiterApi.getAllJobProfile()

    suspend fun findAllActiveIndustry() = jobRecruiterApi.findAllActiveIndustry()

    suspend fun findAllActiveExperienceLevel() = jobRecruiterApi.findAllActiveExperienceLevel()

    suspend fun findAllActiveJobApplicability() = jobRecruiterApi.findAllActiveJobApplicability()

    suspend fun findJobProfileById(jobProfileId: Int) =
        jobRecruiterApi.findJobProfileById(jobProfileId)

    suspend fun findJobDetailsById(jobId: Int) = jobRecruiterApi.findJobDetailsById(jobId)

    suspend fun jobSearch(
        jobSearchRequest: JobSearchRequest
    ) = jobRecruiterApi.jobSearchApi(jobSearchRequest)

}