package com.aaonri.app.data.classified.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponse
import com.aaonri.app.data.classified.model.PostClassifiedRequest
import com.aaonri.app.data.classified.model.UserAds
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

    var isProductNewCheckBox: Boolean = false
        private set

    var classifiedCategory: String = ""
        private set

    var classifiedSubCategory: String = ""
        private set

    var classifiedBasicDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var classifiedAddressDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var isAgreeToAaonri: Boolean = false
        private set

    var sendDataToClassifiedDetailsScreen: MutableLiveData<UserAds> = MutableLiveData()
        private set

    var navigateToClassifiedDetail = false
        private set

    var filterSelectedDataList: MutableLiveData<MutableList<String>> = MutableLiveData()

    val postClassifiedData: MutableLiveData<Resource<PostClassifiedRequest>> = MutableLiveData()

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

    fun addIsProductNewCheckBox(value: Boolean) {
        isProductNewCheckBox = value
    }

    fun addClassifiedBasicDetails(
        title: String,
        price: String,
        adDescription: String
    ) {
        classifiedBasicDetailsMap[ClassifiedConstant.TITLE] = title
        classifiedBasicDetailsMap[ClassifiedConstant.ASKING_PRICE] = price
        classifiedBasicDetailsMap[ClassifiedConstant.DESCRIPTION] = adDescription
    }

    fun addClassifiedAddressDetails(
        city: String,
        zip: String,
        email: String,
        phone: String,
        description: String,
    ) {
        classifiedAddressDetailsMap[ClassifiedConstant.CITY_NAME] = city
        classifiedAddressDetailsMap[ClassifiedConstant.ZIP_CODE] = zip
        classifiedAddressDetailsMap[ClassifiedConstant.EMAIL] = email
        classifiedAddressDetailsMap[ClassifiedConstant.PHONE] = phone
        classifiedAddressDetailsMap[ClassifiedConstant.DESCRIPTION] = description
    }

    fun addIsAgreeToAaonri(value: Boolean) {
        isAgreeToAaonri = value
    }

    fun postClassified(postClassifiedRequest: PostClassifiedRequest) = viewModelScope.launch {
        postClassifiedData.postValue(Resource.Loading())
        val response = classifiedRepository.postClassified(postClassifiedRequest)
        postClassifiedData.postValue(handlePostClassifiedResponse(response))
    }

    private fun handlePostClassifiedResponse(response: Response<PostClassifiedRequest>): Resource<PostClassifiedRequest>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setSendDataToClassifiedDetailsScreen(value: UserAds) {
        sendDataToClassifiedDetailsScreen.postValue(value)
    }

    fun setNavigateToClassifiedDetailsScreen(value: Boolean) {
        navigateToClassifiedDetail = value
    }

    fun setFilterData(value: MutableList<String>) {
        filterSelectedDataList.postValue(value)
    }

}