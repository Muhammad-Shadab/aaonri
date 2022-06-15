package com.aaonri.app.data.classified.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponse
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostClassifiedViewModel @Inject constructor(
    private val classifiedRepository: ClassifiedRepository
) : ViewModel() {

    val navigationForStepper: MutableLiveData<String> = MutableLiveData()

    val stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()

    var classifiedCategory: MutableLiveData<Resource<ClassifiedCategoryResponse>> =
        MutableLiveData()

    fun addNavigationForStepper(value: String) {
        navigationForStepper.value = value
    }

    fun addStepViewLastTick(value: Boolean) {
        stepViewLastTick.value = value
    }


    fun getClassifiedCategory() = viewModelScope.launch {
        classifiedCategory.postValue(Resource.Loading())
        val response = classifiedRepository.getClassifiedCategory()
        classifiedCategory.postValue(handleClassifiedCategoryResponse(response))
    }

    private fun handleClassifiedCategoryResponse(response: Response<ClassifiedCategoryResponse>): Resource<ClassifiedCategoryResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}