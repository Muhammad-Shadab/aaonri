package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.ViewModel
import com.aaonri.app.data.authentication.register.model.community.Community

class CommonViewModel : ViewModel() {

    var selectedCommunityList = mutableListOf<Community>()
        private set

    var selectedCountry: Pair<String, String>? = null
        private set

    fun addCommunityList(value: List<Community>) {
        selectedCommunityList.addAll(value)
    }

    fun selectCountry(value: String, countryFlag: String) {
        selectedCountry = Pair(first = value, second = countryFlag)
    }


}