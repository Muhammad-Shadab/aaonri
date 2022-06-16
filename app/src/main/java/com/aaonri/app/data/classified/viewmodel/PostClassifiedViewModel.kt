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

    var navigationForStepper: MutableLiveData<String> = MutableLiveData()
        private set

    var stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var classifiedCategoryData: MutableLiveData<Resource<ClassifiedCategoryResponse>> =
        MutableLiveData()

    var classifiedCategory: String = ""
        private set

    var classifiedSubCategory: String = ""
        private set

    fun addNavigationForStepper(value: String) {
        navigationForStepper.value = value
    }

    fun addStepViewLastTick(value: Boolean) {
        stepViewLastTick.value = value
    }


    fun getClassifiedCategory() = viewModelScope.launch {
        classifiedCategoryData.postValue(Resource.Loading())
        val response = classifiedRepository.getClassifiedCategory()
        classifiedCategoryData.postValue(handleClassifiedCategoryResponse(response))
    }

    private fun handleClassifiedCategoryResponse(response: Response<ClassifiedCategoryResponse>): Resource<ClassifiedCategoryResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun addClassifiedCategory(category: String) {
        classifiedCategory = category
    }

    fun addClassifiedSubCategory(subCategory: String) {
        classifiedSubCategory = subCategory
    }

}