package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.community.Community
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.model.zip_code.ZipCodeResponse
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    val registrationRepository: RegistrationRepository
) : ViewModel() {

    var basicDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var locationDetails: MutableMap<String, String> = mutableMapOf()
        private set

    var addressDetails: MutableMap<String, String> = mutableMapOf()
        private set

    var selectedCommunityList: MutableLiveData<List<Community>> = MutableLiveData()
        private set

    var selectedServicesList: MutableLiveData<MutableList<ServicesResponseItem>> = MutableLiveData()
        private set

    var selectedCountry: MutableLiveData<Triple<String, String, String>>? = MutableLiveData()
        private set

    var companyEmailAliasName: MutableLiveData<Pair<String, String>>? = MutableLiveData()
        private set

    var companyEmailAliasCheckBoxValue: MutableMap<String, Boolean> = mutableMapOf()
        private set

    val countriesData: MutableLiveData<Resource<CountriesResponse>> = MutableLiveData()

    val zipCodeData: MutableLiveData<Resource<ZipCodeResponse>> = MutableLiveData()

    val communitiesList: MutableLiveData<Resource<CommunitiesListResponse>> = MutableLiveData()

    fun addCommunityList(value: MutableList<Community>) {
        selectedCommunityList.value = value
    }

    fun addServicesList(value: MutableList<ServicesResponseItem>) {
        selectedServicesList.value = value
    }

    fun addSelectedCountry(countryName: String, countryFlag: String, countryCode: String) {
        selectedCountry?.value =
            Triple(first = countryName, second = countryFlag, third = countryCode)
    }

    fun addBasicDetails(
        firstName: String,
        lastName: String,
        emailAddress: String,
        password: String
    ) {
        basicDetailsMap["firstName"] = firstName
        basicDetailsMap["lastName"] = lastName
        basicDetailsMap["emailAddress"] = emailAddress
        basicDetailsMap["password"] = password
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
        belongToCricketCheckBox: Boolean
    ) {
        companyEmailAliasCheckBoxValue["isRecruiterCheckBox"] = isRecruiterCheckBox
        companyEmailAliasCheckBoxValue["isAliasNameCheckBox"] = isAliasNameCheckBox
        companyEmailAliasCheckBoxValue["belongToCricketCheckBox"] = belongToCricketCheckBox
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

}