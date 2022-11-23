package com.aaonri.app.data.jobs.seeker.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.jobs.recruiter.model.JobSearchRequest
import com.aaonri.app.data.jobs.recruiter.model.JobSearchResponse
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.data.jobs.seeker.model.*
import com.aaonri.app.data.jobs.seeker.repository.JobSeekerRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class JobSeekerViewModel @Inject constructor(private val jobSeekerRepository: JobSeekerRepository) :
    ViewModel() {

    val allActiveJobsData: MutableLiveData<Resource<AllJobsResponse>> = MutableLiveData()

    val allActiveAvailabilityData: MutableLiveData<Resource<ActiveJobAvailabilityResponse>> =
        MutableLiveData()

    val jobDetailData: MutableLiveData<Resource<JobDetailResponse>> = MutableLiveData()

    val allExperienceLevelData: MutableLiveData<Resource<ExperienceLevelResponse>> =
        MutableLiveData()

    val allActiveJobApplicabilityData: MutableLiveData<Resource<AllActiveJobApplicabilityResponse>> =
        MutableLiveData()

    val updateJobProfileData: MutableLiveData<Resource<AddJobProfileResponse>> = MutableLiveData()

    val addJobProfileData: MutableLiveData<Resource<AddJobProfileResponse>> = MutableLiveData()

    val applyJobData: MutableLiveData<Resource<ApplyJobResponse>> = MutableLiveData()

    val saveJobViewData: MutableLiveData<Resource<SaveJobViewRequest>> = MutableLiveData()

    val navigateAllJobToDetailsJobScreen: MutableLiveData<Int> = MutableLiveData()

    val navigateToUploadJobProfileScreen: MutableLiveData<Boolean> = MutableLiveData()

    val navigateToUpdateJobProfileScreen: MutableLiveData<Pair<Boolean, Int>> = MutableLiveData()

    val getUserJobProfileData: MutableLiveData<Resource<UserJobProfileResponse>> = MutableLiveData()

    val selectedExperienceLevel: MutableLiveData<ExperienceLevelResponseItem> = MutableLiveData()

    val selectedJobApplicability: MutableLiveData<AllActiveJobApplicabilityResponseItem> =
        MutableLiveData()

    val selectedJobAvailability: MutableLiveData<ActiveJobAvailabilityResponseItem> =
        MutableLiveData()

    val uploadResumeData: MutableLiveData<Resource<String>> = MutableLiveData()

    val searchJobData: MutableLiveData<Resource<JobSearchResponse>> =
        MutableLiveData()

    val userJobProfileCoverLetterValue: MutableLiveData<String> =
        MutableLiveData()

    var resumeFileUri: Uri? = null

    var selectedVisaStatusJobApplicability: MutableLiveData<List<AllActiveJobApplicabilityResponseItem>> =
        MutableLiveData()

    var userJobAlertData: MutableLiveData<Resource<JobAlertResponse>> = MutableLiveData()

    var createJobAlertData: MutableLiveData<Resource<CreateJobAlertResponse>> = MutableLiveData()

    var updateJobAlertData: MutableLiveData<Resource<CreateJobAlertResponse>> = MutableLiveData()

    var deleteJobAlertData: MutableLiveData<Resource<DeleteJobAlertResponse>> = MutableLiveData()

    var navigateToUpdateJobAlert: MutableLiveData<JobAlert> = MutableLiveData()

    var navigateToCreateJobAlert: MutableLiveData<Boolean> = MutableLiveData()

    var selectedJobList: MutableLiveData<List<JobType>> =
        MutableLiveData()

    var selectedJobListTemp = mutableListOf<JobType>()

    var jobSearchFilterData: MutableLiveData<JobSearchFilterModel> = MutableLiveData()

    var changeJobScreenTab: MutableLiveData<String> = MutableLiveData()

    fun getAllActiveJobs() = viewModelScope.launch {
        allActiveJobsData.postValue(Resource.Loading())
        val response = jobSeekerRepository.getAllActiveJobs()
        allActiveJobsData.postValue(handleAllActiveJobResponse(response))
    }

    private fun handleAllActiveJobResponse(response: Response<AllJobsResponse>): Resource<AllJobsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getJobDetails(jobId: Int) = viewModelScope.launch {
        jobDetailData.postValue(Resource.Loading())
        val response = jobSeekerRepository.getJobDetails(jobId)
        jobDetailData.postValue(handleJobDetailResponse(response))
    }

    private fun handleJobDetailResponse(response: Response<JobDetailResponse>): Resource<JobDetailResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getUserJobProfileByEmail(emailId: String, isApplicant: Boolean) = viewModelScope.launch {
        getUserJobProfileData.postValue(Resource.Loading())
        val response = jobSeekerRepository.getUserJobProfileByEmail(emailId, isApplicant)
        getUserJobProfileData.postValue(handleUserJobProfileResponse(response))
    }

    private fun handleUserJobProfileResponse(response: Response<UserJobProfileResponse>): Resource<UserJobProfileResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAllActiveAvailability() = viewModelScope.launch {
        allActiveAvailabilityData.postValue(Resource.Loading())
        val response = jobSeekerRepository.getAllActiveAvailability()
        allActiveAvailabilityData.postValue(handleAllActiveAvailabilityResponse(response))
    }

    private fun handleAllActiveAvailabilityResponse(response: Response<ActiveJobAvailabilityResponse>): Resource<ActiveJobAvailabilityResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun getAllActiveExperienceLevel() = viewModelScope.launch {
        allExperienceLevelData.postValue(Resource.Loading())
        val response = jobSeekerRepository.getAllActiveExperienceLevel()
        allExperienceLevelData.postValue(handleAllActiveExperienceResponse(response))
    }

    private fun handleAllActiveExperienceResponse(response: Response<ExperienceLevelResponse>): Resource<ExperienceLevelResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAllActiveJobApplicability() = viewModelScope.launch {
        allActiveJobApplicabilityData.postValue(Resource.Loading())
        val response = jobSeekerRepository.getAllActiveJobApplicability()
        allActiveJobApplicabilityData.postValue(handleJobApplicabilityResponse(response))
    }

    private fun handleJobApplicabilityResponse(response: Response<AllActiveJobApplicabilityResponse>): Resource<AllActiveJobApplicabilityResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun updateJobProfile(profileId: Int, addJobProfileRequest: AddJobProfileRequest) =
        viewModelScope.launch {
            updateJobProfileData.postValue(Resource.Loading())
            val response = jobSeekerRepository.updateJobProfile(profileId, addJobProfileRequest)
            updateJobProfileData.postValue(handleUpdateAndAddJobProfileResponse(response))
        }

    private fun handleUpdateAndAddJobProfileResponse(response: Response<AddJobProfileResponse>): Resource<AddJobProfileResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun addJobProfile(addJobProfileRequest: AddJobProfileRequest) = viewModelScope.launch {
        addJobProfileData.postValue(Resource.Loading())
        val response = jobSeekerRepository.addJobProfile(addJobProfileRequest)
        addJobProfileData.postValue(handleUpdateAndAddJobProfileResponse(response))
    }

    fun uploadResume(
        jobProfileId: Int,
        jobProfile: Boolean,
        file: MultipartBody.Part
    ) = viewModelScope.launch {
        uploadResumeData.postValue(Resource.Loading())
        val response = jobSeekerRepository.uploadResume(jobProfileId, jobProfile, file)
        uploadResumeData.postValue(handleUploadResumeResponse(response))
    }

    private fun handleUploadResumeResponse(response: Response<String>): Resource<String>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun applyJob(applyJobRequest: ApplyJobRequest) = viewModelScope.launch {
        applyJobData.postValue(Resource.Loading())
        val response = jobSeekerRepository.applyJob(applyJobRequest)
        applyJobData.postValue(handleApplyJobResponse(response))
    }

    private fun handleApplyJobResponse(response: Response<ApplyJobResponse>): Resource<ApplyJobResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getJobAlertsByJobProfileId(jobProfileId: Int) = viewModelScope.launch {
        userJobAlertData.postValue(Resource.Loading())
        val response = jobSeekerRepository.getJobAlertsByJobProfileId(jobProfileId)
        userJobAlertData.postValue(handleJobAlertResponse(response))
    }

    private fun handleJobAlertResponse(response: Response<JobAlertResponse>): Resource<JobAlertResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun createJobAlert(createAlertRequest: CreateAlertRequest) = viewModelScope.launch {
        createJobAlertData.postValue(Resource.Loading())
        val response = jobSeekerRepository.createJobAlert(createAlertRequest)
        createJobAlertData.postValue(handleCreateJobAlertResponse(response))
    }

    private fun handleCreateJobAlertResponse(response: Response<CreateJobAlertResponse>): Resource<CreateJobAlertResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun updateJobAlert(jobAlertId: Int, createAlertRequest: CreateAlertRequest) =
        viewModelScope.launch {
            updateJobAlertData.postValue(Resource.Loading())
            val response = jobSeekerRepository.updateJobAlert(jobAlertId, createAlertRequest)
            updateJobAlertData.postValue(handleUpdateJobAlertResponse(response))
        }

    private fun handleUpdateJobAlertResponse(response: Response<CreateJobAlertResponse>): Resource<CreateJobAlertResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun deleteJobAlert(jobAlertId: Int) = viewModelScope.launch {
        deleteJobAlertData.postValue(Resource.Loading())
        val response = jobSeekerRepository.deleteJobAlert(jobAlertId)
        deleteJobAlertData.postValue(handleDeleteJobAlertResponse(response))
    }

    private fun handleDeleteJobAlertResponse(response: Response<DeleteJobAlertResponse>): Resource<DeleteJobAlertResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveJobView(saveJobViewRequest: SaveJobViewRequest) = viewModelScope.launch {
        saveJobViewData.postValue(Resource.Loading())
        val response = jobSeekerRepository.saveJobView(saveJobViewRequest)
        saveJobViewData.postValue(handleSaveJobViewResponse(response))
    }

    fun searchJob(jobSearchRequest: JobSearchRequest) = viewModelScope.launch {
        searchJobData.postValue(Resource.Loading())
        val response = jobSeekerRepository.searchJob(jobSearchRequest)
        searchJobData.postValue(handleJobSearchResponse(response))
    }

    private fun handleJobSearchResponse(response: Response<JobSearchResponse>): Resource<JobSearchResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSaveJobViewResponse(response: Response<SaveJobViewRequest>): Resource<SaveJobViewRequest>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setJobSearchFilterData(value: JobSearchFilterModel) {
        jobSearchFilterData.postValue(value)
    }

    fun setNavigateAllJobToDetailsJobScreen(value: Int) {
        navigateAllJobToDetailsJobScreen.postValue(value)
    }

    fun setNavigateToUploadJobProfileScreen(value: Boolean) {
        navigateToUploadJobProfileScreen.postValue(value)
    }

    fun setNavigateToUpdateJobProfileScreen(value: Boolean, id: Int) {
        navigateToUpdateJobProfileScreen.postValue(Pair(first = value, second = id))
    }

    fun setSelectedExperienceLevel(value: ExperienceLevelResponseItem) {
        selectedExperienceLevel.postValue(value)
    }

    fun setSelectedJobApplicability(value: AllActiveJobApplicabilityResponseItem) {
        selectedJobApplicability.postValue(value)
    }

    fun setSelectedJobAvailability(value: ActiveJobAvailabilityResponseItem) {
        selectedJobAvailability.postValue(value)
    }

    fun setResumeFileUriValue(value: Uri?) {
        resumeFileUri = value
    }

    fun setUserJobProfileCoverLetterValue(value: String) {
        userJobProfileCoverLetterValue.postValue(value)
    }

    fun setSelectedVisaStatusJobApplicabilityValue(value: List<AllActiveJobApplicabilityResponseItem>) {
        selectedVisaStatusJobApplicability.postValue(value)
    }

    fun setNavigateToUpdateJobAlert(value: JobAlert) {
        navigateToUpdateJobAlert.postValue(value)
    }

    fun setNavigateToCreateJobAlert(value: Boolean) {
        navigateToCreateJobAlert.postValue(value)
    }

    fun setSelectJobListMutableValue(value: List<JobType>) {
        selectedJobList.postValue(value)
        selectedJobListTemp = value as MutableList<JobType>
    }

    fun setChangeJobScreenTab(value: String) {
        changeJobScreenTab.postValue(value)
    }

}