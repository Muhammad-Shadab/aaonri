package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.ViewModel
import com.aaonri.app.data.authentication.register.model.community.Community

class CommonViewModel : ViewModel() {

    var selectedCommunityList = mutableListOf<Community>()
        private set

    var selectedCountry: String? = null
        private set

    fun addCommunityList(value: List<Community>) {
        selectedCommunityList.addAll(value)
    }

    fun selectCountry(value: String) {
        selectedCountry = value
    }


}