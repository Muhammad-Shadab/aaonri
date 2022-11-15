package com.aaonri.app.data.jobs.recruiter.repository

import com.aaonri.app.data.jobs.recruiter.api.DeactivateJobApi
import com.aaonri.app.data.jobs.recruiter.api.JobRecruiterApi
import com.aaonri.app.data.jobs.recruiter.model.JobSearchRequest
import com.aaonri.app.data.jobs.recruiter.model.PostJobRequest
import com.aaonri.app.data.jobs.recruiter.model.SearchAllTalentRequest
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileRequest
import retrofit2.http.Body
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

    suspend fun getUserConsultantProfile(emailId: String, isApplicant: Boolean) =
        jobRecruiterApi.getUserConsultantProfile(emailId, isApplicant)

    suspend fun getJobApplicantList(jobId: Int) = jobRecruiterApi.getJobApplicantList(jobId)

    suspend fun findAllActiveJobBillingType() = jobRecruiterApi.findAllActiveJobBillingType()

    suspend fun getAllAvailability() = jobRecruiterApi.getAllAvailability()

    suspend fun getMyPostedJobs(
        jobSearchRequest: JobSearchRequest
    ) = jobRecruiterApi.getMyPostedJobs(jobSearchRequest)

    suspend fun getAllTalents(searchAllTalentRequest: SearchAllTalentRequest) = jobRecruiterApi.getAllTalents(searchAllTalentRequest)

    suspend fun changeJobActiveStatus(jobId: Int, activeStatus: Boolean) =
        deactivateJobApi.changeJobActiveStatus(jobId, activeStatus)

    suspend fun addConsultantProfile(
        addJobProfileRequest: AddJobProfileRequest
    ) = jobRecruiterApi.addConsultantProfile(addJobProfileRequest)

    suspend fun updateConsultantProfile(
        consultantProfileId: Int,
        addJobProfileRequest: AddJobProfileRequest
    ) = jobRecruiterApi.updateConsultantProfile(consultantProfileId, addJobProfileRequest)

    suspend fun postJob(postJobRequest: PostJobRequest) = jobRecruiterApi.postJob(postJobRequest)

    suspend fun updateJob(postJobRequest: PostJobRequest) =
        jobRecruiterApi.updateJob(postJobRequest)

}