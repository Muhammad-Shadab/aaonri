package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.ViewModel
import com.aaonri.app.data.authentication.register.model.Community

class CommonViewModel : ViewModel() {

    var selectedCommunityList = mutableListOf<Community>()
        private set

    fun addCommunityList(value: List<Community>) {
        selectedCommunityList.addAll(value)
    }

}