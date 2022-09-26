package com.aaonri.app.data.authentication.register.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.model.CommunityAuth
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.model.zip_code.ZipCodeResponse
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.aaonri.app.data.classified.model.FindByEmailDetailResponse
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthCommonViewModel @Inject constructor(
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    var basicDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    val findByEmailData: MutableLiveData<Resource<FindByEmailDetailResponse>> =
        MutableLiveData()

    var saveState: MutableLiveData<String> = MutableLiveData()

    var saveCountry: MutableLiveData<String> = MutableLiveData()

    var locationDetails: MutableMap<String, String> = mutableMapOf()
        private set

    var originLocationDetails: MutableMap<String, String> = mutableMapOf()
        private set

    var addressDetails: MutableMap<String, String> = mutableMapOf()
        private set

    var selectedCommunityList: MutableLiveData<MutableList<CommunityAuth>> = MutableLiveData()

    var uploadProfilePicData: MutableLiveData<Resource<String>> = MutableLiveData()

    var profilePicUri: Uri? = null

    var selectedServicesList: MutableLiveData<MutableList<ServicesResponseItem>> = MutableLiveData()
        private set

    var selectedCountryAddressScreen: MutableLiveData<Triple<String, String, String>>? =
        MutableLiveData()
        private set

    var selectedCountryLocationScreen: MutableLiveData<Triple<String, String, String>>? =
        MutableLiveData()
        private set

    var companyEmailAliasName: MutableLiveData<Pair<String, String>>? = MutableLiveData()
        private set

    var companyEmailAliasCheckBoxValue: MutableMap<String, Boolean> = mutableMapOf()
        private set

    val countriesData: MutableLiveData<Resource<CountriesResponse>> = MutableLiveData()

    val zipCodeData: MutableLiveData<Resource<ZipCodeResponse>> = MutableLiveData()

    val navigationForStepper: MutableLiveData<String> = MutableLiveData()

    val stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()

    val communitiesList: MutableLiveData<Resource<CommunitiesListResponse>> = MutableLiveData()

    val countryClicked: MutableLiveData<Boolean> = MutableLiveData()

    var isUpdateProfile = false

    var isSelectedServices: Boolean? = false

    var isNewUserRegisterUsingGmail = false
        private set

    var isCountrySelected: Boolean = false
        private set

    var countryFlagBmp: Bitmap? = null
        private set

    fun addNavigationForStepper(value: String) {
        navigationForStepper.value = value
    }

    fun addStepViewLastTick(value: Boolean) {
        stepViewLastTick.value = value
    }

    fun addCommunityList(value: MutableList<CommunityAuth>) {
        selectedCommunityList.postValue(value)
    }

    fun addServicesList(value: MutableList<ServicesResponseItem>) {
        selectedServicesList.postValue(value)
    }

    fun setSelectedCountryAddressScreen(
        countryName: String,
        countryFlag: String,
        countryCode: String
    ) {
        selectedCountryAddressScreen?.value =
            Triple(first = countryName, second = countryFlag, third = countryCode)
    }

    fun setSelectedCountryLocationScreen(
        countryName: String,
        countryFlag: String,
        countryCode: String
    ) {
        selectedCountryLocationScreen?.value =
            Triple(first = countryName, second = countryFlag, third = countryCode)
    }

    fun addBasicDetails(
        firstName: String,
        lastName: String,
        emailAddress: String,
        password: String
    ) {
        basicDetailsMap[AuthConstant.FIRST_NAME] = firstName
        basicDetailsMap[AuthConstant.LAST_NAME] = lastName
        basicDetailsMap[AuthConstant.EMAIL_ADDRESS] = emailAddress
        basicDetailsMap[AuthConstant.BASIC_DETAILS_PASS] = password
    }

    fun addLocationDetails(
        zipCode: String,
        state: String,
        city: String
    ) {
        locationDetails["zipCode"] = zipCode
        locationDetails["state"] = state
        locationDetails["city"] = city
    }

    fun addOriginLocationDetails(
        originState: String,
        originCity: String
    ) {
        originLocationDetails["originState"] = originState
        originLocationDetails["originCity"] = originCity
    }

    fun addAddressDetails(
        address1: String,
        address2: String,
        phoneNumber: String
    ) {
        addressDetails["address1"] = address1
        addressDetails["address2"] = address2
        addressDetails["phoneNumber"] = phoneNumber
    }

    fun addCompanyEmailAliasName(companyEmail: String, aliasName: String) {
        companyEmailAliasName?.value = Pair(companyEmail, aliasName)
    }

    fun addCompanyEmailAliasCheckBoxValue(
        isRecruiterCheckBox: Boolean,
        isAliasNameCheckBox: Boolean,
        //belongToCricketCheckBox: Boolean
    ) {
        companyEmailAliasCheckBoxValue["isRecruiterCheckBox"] = isRecruiterCheckBox
        companyEmailAliasCheckBoxValue["isAliasNameCheckBox"] = isAliasNameCheckBox
        //companyEmailAliasCheckBoxValue["belongToCricketCheckBox"] = belongToCricketCheckBox
    }

    fun getCommunities() = viewModelScope.launch {
        communitiesList.postValue(Resource.Loading())
        val response = registrationRepository.getCommunitiesList()
        communitiesList.postValue(handleCommunitiesResponse(response))
    }

    private fun handleCommunitiesResponse(response: Response<CommunitiesListResponse>): Resource<CommunitiesListResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getCountries() = viewModelScope.launch {
        countriesData.postValue(Resource.Loading())
        val response = registrationRepository.getCountries()
        countriesData.postValue(handleCountriesResponse(response))
    }

    private fun handleCountriesResponse(response: Response<CountriesResponse>): Resource<CountriesResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getLocationByZipCode(postalCode: String, countryCode: String) = viewModelScope.launch {
        zipCodeData.postValue(Resource.Loading())
        val response = registrationRepository.getLocationByZipCode(postalCode, countryCode)
        zipCodeData.postValue(handleZipCodeResponse(response))
    }

    private fun handleZipCodeResponse(response: Response<ZipCodeResponse>): Resource<ZipCodeResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun addCountryClicked(value: Boolean) {
        countryClicked.value = value
    }

    fun setIsCountrySelected(value: Boolean) {
        isCountrySelected = value
    }


    fun countryFlagBmp(value: Bitmap?) {
        countryFlagBmp = value
    }

    fun setIsNewUserRegisterUsingGmail(value: Boolean) {
        isNewUserRegisterUsingGmail = value
    }

    fun setSelectedServices(value: Boolean) {
        isSelectedServices = value
    }

    fun uploadProfilePic(
        file: MultipartBody.Part,
        userId: RequestBody
    ) = viewModelScope.launch {
        uploadProfilePicData.postValue(Resource.Loading())
        val response = registrationRepository.uploadProfilePic(file, userId)
        uploadProfilePicData.postValue(handleUploadImageResponse(response))
    }

    private fun handleUploadImageResponse(response: Response<String>): Resource<String>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setProfilePicUriValue(value: Uri?) {
        profilePicUri = value
    }

    fun setIsUpdateProfile(value: Boolean) {
        isUpdateProfile = value
    }

    fun findByEmail(email: String) = viewModelScope.launch {
        findByEmailData.postValue(Resource.Loading())
        val response = registrationRepository.findByEmail(email)
        findByEmailData.postValue(handleClassifiedSellerNameResponse(response))
    }

    private fun handleClassifiedSellerNameResponse(response: Response<FindByEmailDetailResponse>): Resource<FindByEmailDetailResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}