package com.aaonri.app.data.jobs.recruiter.repository

import com.aaonri.app.data.jobs.recruiter.api.DeactivateJobApi
import com.aaonri.app.data.jobs.recruiter.api.JobRecruiterApi
import com.aaonri.app.data.jobs.recruiter.model.JobSearchRequest
import javax.inject.Inject

class JobRecruiterRepository @Inject constructor(
    val jobRecruiterApi: JobRecruiterApi,
    val deactivateJobApi: DeactivateJobApi
) {

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

    suspend fun changeJobActiveStatus(jobId: Int, activeStatus: Boolean) =
        deactivateJobApi.changeJobActiveStatus(jobId, activeStatus)

}