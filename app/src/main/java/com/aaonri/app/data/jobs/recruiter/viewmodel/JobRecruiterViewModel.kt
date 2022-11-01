package com.aaonri.app.data.jobs.recruiter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.jobs.recruiter.JobRecruiterConstant
import com.aaonri.app.data.jobs.recruiter.model.*
import com.aaonri.app.data.jobs.recruiter.repository.JobRecruiterRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class JobRecruiterViewModel @Inject constructor(val repository: JobRecruiterRepository) :
    ViewModel() {

    val navigationForStepper: MutableLiveData<String> = MutableLiveData()

    val stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()

    val allJobProfileData: MutableLiveData<Resource<AllJobProfileResponse>> = MutableLiveData()

    val allActiveIndustryData: MutableLiveData<Resource<AllActiveIndustryResponse>> =
        MutableLiveData()

    val allExperienceLevelData: MutableLiveData<Resource<AllActiveExperienceLevelResponse>> =
        MutableLiveData()

    val allActiveJobApplicabilityData: MutableLiveData<Resource<AllActiveJobApplicabilityResponse>> =
        MutableLiveData()

    val jobProfileDetailsByIdData: MutableLiveData<Resource<JobProfileDetailsByIdResponse>> =
        MutableLiveData()

    val jobSearchData: MutableLiveData<Resource<JobSearchResponse>> =
        MutableLiveData()

    val recruiterPostJobDetails: MutableMap<String, String> = mutableMapOf()

    val navigateAllJobProfileScreenToTalentProfileDetailsScreen: MutableLiveData<Int> =
        MutableLiveData()

    val navigateFromMyPostedJobToJobDetailsScreen: MutableLiveData<Int> =
        MutableLiveData()

    val jobDetailsByIdData: MutableLiveData<Resource<JobDetails>> = MutableLiveData()

    fun addNavigationForStepper(value: String) {
        navigationForStepper.postValue(value)
    }

    fun getAllJobProfile() = viewModelScope.launch {
        allJobProfileData.postValue(Resource.Loading())
        val response = repository.getAllJobProfile()
        allJobProfileData.postValue(handleAllJobProfileResponse(response))
    }

    private fun handleAllJobProfileResponse(response: Response<AllJobProfileResponse>): Resource<AllJobProfileResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findAllActiveIndustry() = viewModelScope.launch {
        allActiveIndustryData.postValue(Resource.Loading())
        val response = repository.findAllActiveIndustry()
        allActiveIndustryData.postValue(handleAllActiveIndustryResponse(response))
    }

    private fun handleAllActiveIndustryResponse(response: Response<AllActiveIndustryResponse>): Resource<AllActiveIndustryResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findAllActiveExperienceLevel() = viewModelScope.launch {
        allExperienceLevelData.postValue(Resource.Loading())
        val response = repository.findAllActiveExperienceLevel()
        allExperienceLevelData.postValue(handleAllActiveExperienceLevelResponse(response))
    }

    private fun handleAllActiveExperienceLevelResponse(response: Response<AllActiveExperienceLevelResponse>): Resource<AllActiveExperienceLevelResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findAllActiveJobApplicability() = viewModelScope.launch {
        allActiveJobApplicabilityData.postValue(Resource.Loading())
        val response = repository.findAllActiveJobApplicability()
        allActiveJobApplicabilityData.postValue(handleAllJobApplicabilityResponse(response))
    }

    private fun handleAllJobApplicabilityResponse(response: Response<AllActiveJobApplicabilityResponse>): Resource<AllActiveJobApplicabilityResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findJobProfileById(jobProfileId: Int) = viewModelScope.launch {
        jobProfileDetailsByIdData.postValue(Resource.Loading())
        val response = repository.findJobProfileById(jobProfileId)
        jobProfileDetailsByIdData.postValue(handleJobProfileDetailsByIdResponse(response))
    }

    private fun handleJobProfileDetailsByIdResponse(response: Response<JobProfileDetailsByIdResponse>): Resource<JobProfileDetailsByIdResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setRecruiterPostJobDetails(
        jobTitle: String,
        cityName: String,
        state: String,
        country: String,
        contactPersonalEmail: String,
        recruiterName: String,
        skillSet: String
    ) {
        recruiterPostJobDetails[JobRecruiterConstant.JOB_TITLE] = jobTitle
        recruiterPostJobDetails[JobRecruiterConstant.CITY_NAME] = cityName
        recruiterPostJobDetails[JobRecruiterConstant.STATE_NAME] = state
        recruiterPostJobDetails[JobRecruiterConstant.COUNTRY_NAME] = country
        recruiterPostJobDetails[JobRecruiterConstant.CONTACT_PERSONAL_EMAIL] = contactPersonalEmail
        recruiterPostJobDetails[JobRecruiterConstant.RECRUITER_NAME] = recruiterName
        recruiterPostJobDetails[JobRecruiterConstant.SKILL_SET] = skillSet
    }

    fun jobSearch(
        jobSearchRequest: JobSearchRequest
    ) = viewModelScope.launch {
        jobSearchData.postValue(Resource.Loading())
        val response = repository.jobSearch(jobSearchRequest)
        jobSearchData.postValue(handleJobSearchResponse(response))
    }

    private fun handleJobSearchResponse(response: Response<JobSearchResponse>): Resource<JobSearchResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findJobDetailsById(jobId: Int) = viewModelScope.launch {
        jobDetailsByIdData.postValue(Resource.Loading())
        val response = repository.findJobDetailsById(jobId)
        jobDetailsByIdData.postValue(handleJobDetailsByIdResponse(response))
    }

    private fun handleJobDetailsByIdResponse(response: Response<JobDetails>): Resource<JobDetails>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setNavigateAllJobProfileScreenToTalentProfileDetailsScreen(
        profileId: Int
    ) {
        navigateAllJobProfileScreenToTalentProfileDetailsScreen.postValue(profileId)
    }

    fun setNavigateFromMyPostedJobToJobDetailsScreen(value: Int) {
        navigateFromMyPostedJobToJobDetailsScreen.postValue(value)
    }

}