package com.aaonri.app.data.jobs.seeker.repository

import com.aaonri.app.data.jobs.seeker.api.JobSeekerApi
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileRequest
import com.aaonri.app.data.jobs.seeker.model.ApplyJobRequest
import javax.inject.Inject

class JobSeekerRepository @Inject constructor(private val jobSeekerApi: JobSeekerApi) {

    suspend fun getAllActiveJobs() = jobSeekerApi.getAllActiveJobs()

    suspend fun getAllActiveExperienceLevel() = jobSeekerApi.getAllActiveExperienceLevel()

    suspend fun getAllActiveJobApplicability() = jobSeekerApi.getAllActiveJobApplicability()

    suspend fun updateJobProfile(addJobProfileRequest: AddJobProfileRequest) =
        jobSeekerApi.updateJobProfile(addJobProfileRequest)

    suspend fun addJobProfile(addJobProfileRequest: AddJobProfileRequest) =
        jobSeekerApi.addJobProfile(addJobProfileRequest)

    suspend fun applyJob(applyJobRequest: ApplyJobRequest) = jobSeekerApi.applyJob(applyJobRequest)

}