package com.aaonri.app.data.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardCommonViewModel : ViewModel() {

    var isGuestUser: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var isSeeAllClassifiedClicked: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var isFilterApplied: MutableLiveData<String> = MutableLiveData()

    fun setGuestUser(value: Boolean) {
        isGuestUser.value = value
    }

    fun setIsSeeAllClassifiedClicked(value: Boolean) {
        isSeeAllClassifiedClicked.postValue(value)
    }

    fun setIsFilterApplied(value: String) {
        isFilterApplied.postValue(value)
    }

}