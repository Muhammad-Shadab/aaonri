package com.aaonri.app.data.classified.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.*
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import com.aaonri.app.utils.Resource
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostClassifiedViewModel @Inject constructor(
    private val classifiedRepository: ClassifiedRepository
) : ViewModel() {

    var navigationForStepper: MutableLiveData<String> = MutableLiveData()
        private set

    var isUpdateClassified = false
        private set

    var clearSubCategory = false
        private set

    var isNavigateBackBasicDetails = false
        private set

    var updateClassifiedId = 0
        private set

    var stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var classifiedCategoryData: MutableLiveData<Resource<ClassifiedCategoryResponse>> =
        MutableLiveData()

    var isProductNewCheckBox: Boolean = false
        private set

    var classifiedBasicDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var selectedClssifiedCategoryWhileUpdating = ""

    var classifiedAddressDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var isAgreeToAaonri: Boolean = false
        private set

    var sendDataToClassifiedDetailsScreen: MutableLiveData<Int> = MutableLiveData()
        private set

    var sendFavoriteDataToClassifiedDetails: MutableLiveData<Classified> = MutableLiveData()
        private set

    var navigateToClassifiedDetail = false
        private set

    var navigateToMyClassifiedScreen = false
        private set

    var navigateToAllClassified: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var selectedClassifiedCategory: MutableLiveData<ClassifiedCategoryResponseItem> =
        MutableLiveData()
        private set

    var selectedClassifiedSubCategoryList: MutableLiveData<ClassifiedCategoryResponseItem> =
        MutableLiveData()
        private set

    var selectedSubClassifiedCategory: MutableLiveData<ClassifiedSubcategoryX> =
        MutableLiveData()
        private set

    var filterSelectedDataList: MutableLiveData<MutableList<String>> = MutableLiveData()

    val classifiedAdDetailsData: MutableLiveData<Resource<ClassifiedAdDetailsResponse>> =
        MutableLiveData()

    val postClassifiedData: MutableLiveData<Resource<PostClassifiedRequest>> = MutableLiveData()

    val updateClassifiedData: MutableLiveData<Resource<PostClassifiedRequest>> = MutableLiveData()

    val uploadImagesData: MutableLiveData<Resource<UploadImagesResponse>> = MutableLiveData()

    var listOfImagesUri = mutableListOf<Uri>()
        private set

    var clickedOnFilter: MutableLiveData<Boolean> = MutableLiveData()

    var clickOnClearAllFilter: MutableLiveData<Boolean> = MutableLiveData()

    /*var minMaxPriceRangeZipCode: MutableLiveData<Triple<String, String, String>> = MutableLiveData()
        private set*/

    var uploadClassifiedPics: MutableLiveData<Resource<ClassifiedUploadPicResponse>> =
        MutableLiveData()

    var classifiedUploadedImagesIdList = mutableListOf<Int>()
        private set

    var imageIdGoindToRemove = mutableListOf<Int>()
        private set

    var minMaxValueInFilter: MutableLiveData<String> = MutableLiveData()
        private set

    var minValueInFilterScreen: MutableLiveData<String> = MutableLiveData()
        private set

    var maxValueInFilterScreen: MutableLiveData<String> = MutableLiveData()
        private set

    var zipCodeInFilterScreen: MutableLiveData<String> = MutableLiveData()
        private set

    var isMyLocationCheckedInFilterScreen: MutableLiveData<Boolean> = MutableLiveData()
        private set

    val classifiedDeleteData: MutableLiveData<Resource<JsonElement>> =
        MutableLiveData()

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

    fun addIsProductNewCheckBox(value: Boolean) {
        isProductNewCheckBox = value
    }

    fun addClassifiedBasicDetails(
        title: String,
        price: String,
        adDescription: String,
        classifiedCategory: String,
        classifiedSubCategory: String,
    ) {
        classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_TITLE] = title
        classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_ASKING_PRICE] = price
        classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_DESCRIPTION] = adDescription
        classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_CATEGORY] = classifiedCategory
        classifiedBasicDetailsMap[ClassifiedConstant.BASIC_DETAILS_SUB_CATEGORY] =
            classifiedSubCategory
    }

    fun addClassifiedAddressDetails(
        city: String,
        zip: String,
        email: String,
        phone: String,
        keyword: String,
    ) {
        classifiedAddressDetailsMap[ClassifiedConstant.ADDRESS_DETAILS_CITY_NAME] = city
        classifiedAddressDetailsMap[ClassifiedConstant.ADDRESS_DETAILS_ZIP_CODE] = zip
        classifiedAddressDetailsMap[ClassifiedConstant.ADDRESS_DETAILS_EMAIL] = email
        classifiedAddressDetailsMap[ClassifiedConstant.ADDRESS_DETAILS_PHONE] = phone
        classifiedAddressDetailsMap[ClassifiedConstant.ADDRESS_DETAILS_KEYWORD] = keyword
    }

    fun addIsAgreeToAaonri(value: Boolean) {
        isAgreeToAaonri = value
    }

    fun updateClassified(postClassifiedRequest: PostClassifiedRequest) = viewModelScope.launch {
        updateClassifiedData.postValue(Resource.Loading())
        val response = classifiedRepository.updateClassified(postClassifiedRequest)
        updateClassifiedData.postValue(handlePostClassifiedResponse(response))
    }

    fun deleteClassified(classifiedId: Int) = viewModelScope.launch {
        classifiedDeleteData.postValue(Resource.Loading())
        val response = classifiedRepository.deleteClassified(classifiedId)
        classifiedDeleteData.postValue(handleClassifiedDeleteResponse(response))
    }

    private fun handleClassifiedDeleteResponse(response: Response<JsonElement>): Resource<JsonElement>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
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

    fun setSendDataToClassifiedDetailsScreen(value: Int) {
        sendDataToClassifiedDetailsScreen.postValue(value)
    }

    fun setSendFavoriteDataToClassifiedDetails(value: Classified) {
        sendFavoriteDataToClassifiedDetails.postValue(value)
    }

    fun setNavigateToClassifiedDetailsScreen(value: Boolean, isMyClassifiedScreen: Boolean) {
        navigateToClassifiedDetail = value
        navigateToMyClassifiedScreen = isMyClassifiedScreen
    }

    fun setNavigateToAllClassified(value: Boolean) {
        navigateToAllClassified.postValue(value)
    }

    fun setFilterData(value: MutableList<String>) {
        filterSelectedDataList.postValue(value)
    }


    /* fun uploadImages(uploadImagesRequest: UploadImagesRequest) = viewModelScope.launch {
         uploadImagesData.postValue(Resource.Loading())
         val response = classifiedRepository.uploadImages(uploadImagesRequest)
         uploadImagesData.postValue(handleUploadImagesResponse(response))
     }

     private fun handleUploadImagesResponse(response: Response<UploadImagesResponse>): Resource<UploadImagesResponse>? {
         if (response.isSuccessful) {
             response.body()?.let {
                 return Resource.Success(it)
             }
         }
         return Resource.Error(response.message())
     }*/

    fun setMinValue(value: String) {
        minValueInFilterScreen.postValue(value)
    }

    fun setMaxValue(value: String) {
        maxValueInFilterScreen.postValue(value)
    }

    fun setZipCodeInFilterScreen(value: String) {
        zipCodeInFilterScreen.postValue(value)
    }

    fun setMinMaxValue(value1: String, value2: String) {
        minMaxValueInFilter.postValue("Range: \$$value1-\$$value2")
    }

    fun setIsMyLocationChecked(value: Boolean) {
        isMyLocationCheckedInFilterScreen.postValue(value)
    }

    fun setListOfUploadImagesUri(uploadedImagesList: MutableList<Uri>) {
        listOfImagesUri = uploadedImagesList
    }

    fun uploadClassifiedPics(files: MultipartBody.Part, addId: RequestBody, dellId: RequestBody) =
        viewModelScope.launch {
            uploadClassifiedPics.postValue(Resource.Loading())
            val response = classifiedRepository.uploadClassifiedPics(files, addId, dellId)
            uploadClassifiedPics.postValue(handleClassifiedPicUploadResponse(response))
        }

    private fun handleClassifiedPicUploadResponse(response: Response<ClassifiedUploadPicResponse>): Resource<ClassifiedUploadPicResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setClickedOnFilter(value: Boolean) {
        clickedOnFilter.postValue(value)
    }

    fun setClickOnClearAllFilter(value: Boolean) {
        clickOnClearAllFilter.postValue(value)
    }

    fun setSelectedClassifiedCategory(
        value: ClassifiedCategoryResponseItem,
    ) {
        selectedClassifiedCategory.postValue(value)
    }

    fun setClassifiedSubCategoryList(
        value: ClassifiedCategoryResponseItem,
    ) {
        selectedClassifiedSubCategoryList.postValue(value)
    }

    fun setClearSubCategory(value: Boolean) {
        clearSubCategory = value
    }

    fun setSelectedSubClassifiedCategory(value: ClassifiedSubcategoryX) {
        selectedSubClassifiedCategory.postValue(value)
    }

    fun setIsUpdateClassified(value: Boolean) {
        isUpdateClassified = value
    }

    fun setUpdateClassifiedId(value: Int) {
        updateClassifiedId = value
    }

    fun getClassifiedAdDetails(addId: Int) = viewModelScope.launch {
        classifiedAdDetailsData.postValue(Resource.Loading())
        val response = classifiedRepository.getClassifiedAddDetails(addId)
        classifiedAdDetailsData.postValue(handleClassifiedAdDetails(response))
    }

    private fun handleClassifiedAdDetails(response: Response<ClassifiedAdDetailsResponse>): Resource<ClassifiedAdDetailsResponse>? {
        if (response.isSuccessful)
            response.body()?.let {
                return Resource.Success(it)
            }
        return Resource.Error(response.message())
    }

    fun setIsNavigateBackToBasicDetails(value: Boolean) {
        isNavigateBackBasicDetails = value
    }

    fun setClassifiedCategoryWhileUpdating(value: String) {
        selectedClssifiedCategoryWhileUpdating = value
    }

    fun setClassifiedUploadedImagesIdList(value: MutableList<Int>) {
        classifiedUploadedImagesIdList = value
    }

}