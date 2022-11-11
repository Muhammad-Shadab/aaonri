package com.aaonri.app.data.classified.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.model.*
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import com.aaonri.app.utils.Resource
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

    var selectedClassifiedCategoryWhileUpdating = ""

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

    var clearAllFilter: MutableLiveData<Boolean> = MutableLiveData()
        private set

    /*var clearAllFilterBtn: MutableLiveData<Boolean> = MutableLiveData()
        private set*/

    var navigateToAllClassified: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var selectedClassifiedCategory: MutableLiveData<String> =
        MutableLiveData()
        private set

    var selectedClassifiedSubCategoryList: MutableLiveData<ClassifiedCategoryResponseItem> =
        MutableLiveData()
        private set

    var selectedSubClassifiedCategory: MutableLiveData<String> =
        MutableLiveData()
        private set

    val classifiedAdDetailsData: MutableLiveData<Resource<ClassifiedAdDetailsResponse>> =
        MutableLiveData()

    val postClassifiedData: MutableLiveData<Resource<PostClassifiedRequest>> = MutableLiveData()

    val updateClassifiedData: MutableLiveData<Resource<PostClassifiedRequest>> = MutableLiveData()

    //val uploadImagesData: MutableLiveData<Resource<UploadImagesResponse>> = MutableLiveData()

    var listOfImagesUri = mutableListOf<Uri>()
        private set

    var clickedOnFilter: MutableLiveData<Boolean> = MutableLiveData()

    var uploadClassifiedPics: MutableLiveData<Resource<ClassifiedUploadPicResponse>> =
        MutableLiveData()

    var deleteClassifiedPics: MutableLiveData<Resource<ClassifiedUploadPicResponse>> =
        MutableLiveData()

    var classifiedUploadedImagesIdList = mutableListOf<Int>()
        private set

    var imageIdGoingToRemove = mutableListOf<Int>()
        private set

    var minValueInFilterScreen = ""
        private set

    var maxValueInFilterScreen = ""
        private set

    var zipCodeInFilterScreen = ""
        private set

    var categoryFilter = ""
        private set

    var subCategoryFilter = ""
        private set

    var isFilterEnable = false
        private set

    var isMyLocationCheckedInFilterScreen = false
        private set

    var searchQueryFilter = ""
        private set

    var changeSortTriplet = Triple<Boolean, Boolean, Boolean>(false, false, false)
        private set

    val classifiedDeleteData: MutableLiveData<Resource<String>> =
        MutableLiveData()

    var keyClassifiedKeyboardListener: MutableLiveData<Boolean> = MutableLiveData()
        private set

    val classifiedFilterModel: MutableLiveData<ClassifiedFilterModel> = MutableLiveData()

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
        updateClassifiedData.postValue(handleUpdateClassifiedResponse(response))
    }

    private fun handleUpdateClassifiedResponse(response: Response<PostClassifiedRequest>): Resource<PostClassifiedRequest>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun deleteClassified(classifiedId: Int) = viewModelScope.launch {
        classifiedDeleteData.postValue(Resource.Loading())
        val response = classifiedRepository.deleteClassified(classifiedId)
        classifiedDeleteData.postValue(handleClassifiedDeleteResponse(response))
    }

    private fun handleClassifiedDeleteResponse(response: Response<String>): Resource<String>? {
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
        minValueInFilterScreen = value
    }

    fun setCategoryFilter(value: String) {
        categoryFilter = value
    }

    fun setSubCategoryFilter(value: String) {
        subCategoryFilter = value
    }

    fun setMaxValue(value: String) {
        maxValueInFilterScreen = value
    }

    fun setZipCodeInFilterScreen(value: String) {
        zipCodeInFilterScreen = value
    }

    fun setIsMyLocationChecked(value: Boolean) {
        isMyLocationCheckedInFilterScreen = value
    }

    fun setSearchQuery(value: String) {
        searchQueryFilter = value
    }

    fun setListOfUploadImagesUri(uploadedImagesList: MutableList<Uri>) {
        listOfImagesUri = uploadedImagesList
    }

    fun uploadClassifiedPics(files: List<MultipartBody.Part>, addId: RequestBody, dellId: RequestBody) =
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

    fun deleteClassifiedPics(
        files: MultipartBody.Part,
        addId: RequestBody,
        dellId: RequestBody
    ) = viewModelScope.launch {
        deleteClassifiedPics.postValue(Resource.Loading())
        val response = classifiedRepository.deleteClassifiedPics(files, addId, dellId)
        deleteClassifiedPics.postValue(handleClassifiedPicDeleteResponse(response))
    }

    private fun handleClassifiedPicDeleteResponse(response: Response<ClassifiedUploadPicResponse>): Resource<ClassifiedUploadPicResponse>? {
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

    fun setSelectedClassifiedCategory(
        value: String,
    ) {
        selectedClassifiedCategory.postValue(value)
    }

    fun setSelectedSubClassifiedCategory(value: String) {
        selectedSubClassifiedCategory.postValue(value)
    }

    fun setClassifiedSubCategoryList(
        value: ClassifiedCategoryResponseItem,
    ) {
        selectedClassifiedSubCategoryList.postValue(value)
    }

    fun setClearSubCategory(value: Boolean) {
        clearSubCategory = value
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
        selectedClassifiedCategoryWhileUpdating = value
    }

    fun setClassifiedUploadedImagesIdList(value: MutableList<Int>) {
        classifiedUploadedImagesIdList = value
    }

    fun setClearAllFilter(value: Boolean) {
        clearAllFilter.postValue(value)
    }

    fun setIsFilterEnable(value: Boolean) {
        isFilterEnable = value
    }

    fun setChangeSortTripletFilter(
        datePublished: Boolean,
        priceLowToHigh: Boolean,
        priceHighToLow: Boolean
    ) {
        changeSortTriplet = Triple(datePublished, priceLowToHigh, priceHighToLow)
    }

    //This is also used in different case
    /*fun setClickOnClearAllFilterBtn(value: Boolean) {
        clearAllFilterBtn.postValue(value)
    }*/

    fun setKeyClassifiedKeyboardListener(value: Boolean) {
        keyClassifiedKeyboardListener.postValue(value)
    }

}