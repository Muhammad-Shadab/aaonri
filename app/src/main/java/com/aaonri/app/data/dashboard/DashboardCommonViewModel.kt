package com.aaonri.app.data.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardCommonViewModel : ViewModel() {

    var isGuestUser: MutableLiveData<Boolean> = MutableLiveData()
        private set

    fun setGuestUser(value: Boolean) {
        isGuestUser.value = value
    }

}