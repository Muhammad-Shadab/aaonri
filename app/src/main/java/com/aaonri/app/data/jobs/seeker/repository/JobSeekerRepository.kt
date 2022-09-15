package com.aaonri.app.data.jobs.seeker.repository

import com.aaonri.app.data.jobs.seeker.api.JobSeekerApi
import com.aaonri.app.data.jobs.seeker.api.UploadResumeApi
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileRequest
import com.aaonri.app.data.jobs.seeker.model.ApplyJobRequest
import com.aaonri.app.data.jobs.seeker.model.SaveJobViewRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class JobSeekerRepository @Inject constructor(
    private val jobSeekerApi: JobSeekerApi,
    private val uploadResumeApi: UploadResumeApi
) {

    suspend fun getAllActiveJobs() = jobSeekerApi.getAllActiveJobs()

    suspend fun getJobDetails(jobId: Int) = jobSeekerApi.getJobDetails(jobId)

    suspend fun getAllActiveExperienceLevel() = jobSeekerApi.getAllActiveExperienceLevel()

    suspend fun getAllActiveJobApplicability() = jobSeekerApi.getAllActiveJobApplicability()

    suspend fun getAllActiveAvailability() = jobSeekerApi.getAllActiveAvailability()

    suspend fun getUserJobProfileByEmail(emailId: String, isApplicant: Boolean) =
        jobSeekerApi.getUserJobProfileByEmail(emailId, isApplicant)

    suspend fun updateJobProfile(addJobProfileRequest: AddJobProfileRequest) =
        jobSeekerApi.updateJobProfile(addJobProfileRequest)

    suspend fun addJobProfile(addJobProfileRequest: AddJobProfileRequest) =
        jobSeekerApi.addJobProfile(addJobProfileRequest)

    suspend fun uploadResume(
        jobProfileId: Int,
        jobProfile: Boolean,
        file: MultipartBody.Part
    ) = uploadResumeApi.uploadResume(jobProfileId, jobProfile, file)

    suspend fun applyJob(applyJobRequest: ApplyJobRequest) = jobSeekerApi.applyJob(applyJobRequest)

    suspend fun saveJobView(saveJobViewRequest: SaveJobViewRequest) =
        jobSeekerApi.saveJobView(saveJobViewRequest)

}