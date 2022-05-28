package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.ViewModel

class CommonViewModel : ViewModel() {

    var selectedCommunityList: MutableList<String> = mutableListOf()
        private set

    fun addToCommunityList(value: String) {
        selectedCommunityList.add(value)
    }

}