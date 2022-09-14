package com.aaonri.app.data.jobs.seeker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.jobs.seeker.model.*
import com.aaonri.app.data.jobs.seeker.repository.JobSeekerRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun updateJobProfile(addJobProfileRequest: AddJobProfileRequest) = viewModelScope.launch {
        updateJobProfileData.postValue(Resource.Loading())
        val response = jobSeekerRepository.updateJobProfile(addJobProfileRequest)
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

    fun saveJobView(saveJobViewRequest: SaveJobViewRequest) = viewModelScope.launch {
        saveJobViewData.postValue(Resource.Loading())
        val response = jobSeekerRepository.saveJobView(saveJobViewRequest)
        saveJobViewData.postValue(handleSaveJobViewResponse(response))
    }

    private fun handleSaveJobViewResponse(response: Response<SaveJobViewRequest>): Resource<SaveJobViewRequest>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setNavigateAllJobToDetailsJobScreen(value: Int) {
        navigateAllJobToDetailsJobScreen.postValue(value)
    }

    fun setNavigateToUploadJobProfileScreen(value: Boolean) {
        navigateToUploadJobProfileScreen.postValue(value)
    }

}