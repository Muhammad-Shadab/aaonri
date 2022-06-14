package com.aaonri.app.data.classified.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClassifiedCommonViewModel : ViewModel() {

    val navigationForStepper: MutableLiveData<String> = MutableLiveData()

    val stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()

    fun addNavigationForStepper(value: String) {
        navigationForStepper.value = value
    }

    fun addStepViewLastTick(value: Boolean) {
        stepViewLastTick.value = value
    }
}