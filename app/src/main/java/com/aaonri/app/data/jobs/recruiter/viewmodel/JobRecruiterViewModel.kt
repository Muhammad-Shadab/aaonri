package com.aaonri.app.data.jobs.recruiter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.jobs.recruiter.JobRecruiterConstant
import com.aaonri.app.data.jobs.recruiter.model.*
import com.aaonri.app.data.jobs.recruiter.repository.JobRecruiterRepository
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileRequest
import com.aaonri.app.data.jobs.seeker.model.AddJobProfileResponse
import com.aaonri.app.data.jobs.seeker.model.UserJobProfileResponse
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

    val changeJobStatusData: MutableLiveData<Resource<String>> = MutableLiveData()

    val addConsultantProfileData: MutableLiveData<Resource<AddJobProfileResponse>> =
        MutableLiveData()

    val updateConsultantProfileData: MutableLiveData<Resource<AddJobProfileResponse>> =
        MutableLiveData()

    val getUserConsultantProfileData: MutableLiveData<Resource<UserJobProfileResponse>> =
        MutableLiveData()

    val navigateFromMyPostedJobToJobApplicantScreen: MutableLiveData<Int> =
        MutableLiveData()

    val jobApplicantListData: MutableLiveData<Resource<JobApplicantResponse>> =
        MutableLiveData()

    var visibilityToTheFloatingActionBtn: MutableLiveData<Boolean> = MutableLiveData()

    var navigateToUploadConsultantProfile: MutableLiveData<Boolean> = MutableLiveData()

    var userSelectedState: MutableLiveData<String> = MutableLiveData()

    var jobBillingTypeData: MutableLiveData<Resource<BillingTypeResponse>> = MutableLiveData()

    var postJobData: MutableLiveData<Resource<PostJobRequest>> = MutableLiveData()

    var updateJobData: MutableLiveData<Resource<PostJobRequest>> = MutableLiveData()

    var userSelectedExperienceLevel: MutableLiveData<AllActiveExperienceLevelResponseItem> =
        MutableLiveData()

    var userSelectedIndustry: MutableLiveData<AllActiveIndustryResponseItem> = MutableLiveData()

    var userSelectedBillingType: MutableLiveData<BillingTypeResponseItem> = MutableLiveData()

    var isUpdateJob = false

    var isUpdateConsultantProfile = false

    var selectedVisaStatusJobApplicability: MutableLiveData<List<AllActiveJobApplicabilityResponseItem>> =
        MutableLiveData()

    var selectedJobList: MutableLiveData<List<JobType>> =
        MutableLiveData()

    var selectedJobListTemp = mutableListOf<JobType>()

    var selectedAvailability: MutableLiveData<String> = MutableLiveData()

    var allAvailabilityData: MutableLiveData<Resource<AvailabilityResponse>> = MutableLiveData()

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

    fun changeJobActiveStatus(jobId: Int, activeStatus: Boolean) = viewModelScope.launch {
        changeJobStatusData.postValue(Resource.Loading())
        val response = repository.changeJobActiveStatus(jobId, activeStatus)
        changeJobStatusData.postValue(handleChangeJobStatusResponse(response))
    }

    private fun handleChangeJobStatusResponse(response: Response<String>): Resource<String>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun addConsultantProfile(
        addJobProfileRequest: AddJobProfileRequest
    ) = viewModelScope.launch {
        addConsultantProfileData.postValue(Resource.Loading())
        val response = repository.addConsultantProfile(addJobProfileRequest)
        addConsultantProfileData.postValue(handleAddConsultantProfileResponse(response))
    }

    private fun handleAddConsultantProfileResponse(response: Response<AddJobProfileResponse>): Resource<AddJobProfileResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun updateConsultantProfile(
        consultantProfileId: Int,
        addJobProfileRequest: AddJobProfileRequest
    ) = viewModelScope.launch {
        updateConsultantProfileData.postValue(Resource.Loading())
        val response = repository.updateConsultantProfile(consultantProfileId, addJobProfileRequest)
        updateConsultantProfileData.postValue(handleUpdateConsultantProfileResponse(response))
    }

    private fun handleUpdateConsultantProfileResponse(response: Response<AddJobProfileResponse>): Resource<AddJobProfileResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getUserConsultantProfile(emailId: String, isApplicant: Boolean) = viewModelScope.launch {
        getUserConsultantProfileData.postValue(Resource.Loading())
        val response = repository.getUserConsultantProfile(emailId, isApplicant)
        getUserConsultantProfileData.postValue(handleUserConsultantProfileResponse(response))
    }

    private fun handleUserConsultantProfileResponse(response: Response<UserJobProfileResponse>): Resource<UserJobProfileResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getJobApplicantList(jobId: Int) = viewModelScope.launch {
        jobApplicantListData.postValue(Resource.Loading())
        val response = repository.getJobApplicantList(jobId)
        jobApplicantListData.postValue(handleJobApplicantListResponse(response))
    }

    private fun handleJobApplicantListResponse(response: Response<JobApplicantResponse>): Resource<JobApplicantResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findAllActiveJobBillingType() = viewModelScope.launch {
        jobBillingTypeData.postValue(Resource.Loading())
        val response = repository.findAllActiveJobBillingType()
        jobBillingTypeData.postValue(handleJobBillingResponse(response))
    }

    private fun handleJobBillingResponse(response: Response<BillingTypeResponse>): Resource<BillingTypeResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAllAvailability() = viewModelScope.launch {
        allAvailabilityData.postValue(Resource.Loading())
        val response = repository.getAllAvailability()
        allAvailabilityData.postValue(handleAllAvailabilityResponse(response))
    }

    private fun handleAllAvailabilityResponse(response: Response<AvailabilityResponse>): Resource<AvailabilityResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun postJob(postJobRequest: PostJobRequest) = viewModelScope.launch {
        postJobData.postValue(Resource.Loading())
        val response = repository.postJob(postJobRequest)
        postJobData.postValue(handlePostJobResponse(response))
    }

    private fun handlePostJobResponse(response: Response<PostJobRequest>): Resource<PostJobRequest>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun updateJob(postJobRequest: PostJobRequest) = viewModelScope.launch {
        updateJobData.postValue(Resource.Loading())
        val response = repository.updateJob(postJobRequest)
        updateJobData.postValue(handleUpdateJobResponse(response))
    }

    private fun handleUpdateJobResponse(response: Response<PostJobRequest>): Resource<PostJobRequest>? {
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

    fun setNavigateFromMyPostedJobToJobApplicantScreen(value: Int) {
        navigateFromMyPostedJobToJobApplicantScreen.postValue(value)
    }

    fun setVisibilityToTheFloatingActionBtnValue(hideFloatingBtn: Boolean) {
        visibilityToTheFloatingActionBtn.postValue(hideFloatingBtn)
    }

    fun setNavigateToUploadConsultantProfile(value: Boolean) {
        navigateToUploadConsultantProfile.postValue(value)
    }

    fun setIsUpdateJobValue(value: Boolean) {
        isUpdateJob = value
    }

    fun setIsUpdateConsultantProfileValue(value: Boolean) {
        isUpdateConsultantProfile = value
    }

    fun setUserSelectedState(value: String) {
        userSelectedState.postValue(value)
    }

    fun setUserSelectedExperienceLevel(value: AllActiveExperienceLevelResponseItem) {
        userSelectedExperienceLevel.postValue(value)
    }

    fun setUserSelectedIndustry(value: AllActiveIndustryResponseItem) {
        userSelectedIndustry.postValue(value)
    }

    fun setUserSelectedBillingType(value: BillingTypeResponseItem) {
        userSelectedBillingType.postValue(value)
    }

    fun setSelectedVisaStatusJobApplicabilityValue(value: List<AllActiveJobApplicabilityResponseItem>) {
        selectedVisaStatusJobApplicability.postValue(value)
    }

    fun setSelectJobListMutableValue(value: List<JobType>) {
        selectedJobList.postValue(value)
        selectedJobListTemp = value as MutableList<JobType>
    }

    fun setSelectedAvailability(value: String) {
        selectedAvailability.postValue(value)
    }


}