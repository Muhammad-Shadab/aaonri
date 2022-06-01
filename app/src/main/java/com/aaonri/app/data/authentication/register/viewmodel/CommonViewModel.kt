package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aaonri.app.data.authentication.register.model.community.Community
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem

class CommonViewModel : ViewModel() {

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

    fun addCommunityList(value: MutableList<Community>) {
        selectedCommunityList.value = value
    }

    fun addServicesList(value: MutableList<ServicesResponseItem>) {
        selectedServicesList.value = value
    }

    fun selectCountry(value: String, countryFlag: String, countryCode: String) {
        selectedCountry?.value = Triple(first = value, second = countryFlag, third = countryCode)
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


}