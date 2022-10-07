package com.aaonri.app.data.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem

class DashboardCommonViewModel : ViewModel() {

    var isGuestUser: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var isSeeAllClassifiedClicked: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var isAdvertiseClicked: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var isShopWithUsClicked: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var isFilterApplied: MutableLiveData<String> = MutableLiveData()

    var showBottomNavigation: MutableLiveData<Boolean> = MutableLiveData()

    var selectedServicesList: MutableLiveData<MutableList<ServicesResponseItem>> = MutableLiveData()
        private set

    fun setGuestUser(value: Boolean) {
        isGuestUser.value = value
    }

    fun setIsSeeAllClassifiedClicked(value: Boolean) {
        isSeeAllClassifiedClicked.postValue(value)
    }

    fun setIsFilterApplied(value: String) {
        isFilterApplied.postValue(value)
    }

    fun setIsAdvertiseClicked(value: Boolean) {
        isAdvertiseClicked.postValue(value)
    }

    fun setIsShopWithUsClicked(value: Boolean) {
        isShopWithUsClicked.postValue(value)
    }

    fun addServicesList(value: MutableList<ServicesResponseItem>) {
        selectedServicesList.postValue(value)
    }

    fun setShowBottomNavigation(value: Boolean) {
        showBottomNavigation.postValue(value)
    }


}