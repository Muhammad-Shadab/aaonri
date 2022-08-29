package com.aaonri.app.data.advertise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.model.*
import com.aaonri.app.data.advertise.repository.AdvertiseRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PostAdvertiseViewModel @Inject constructor(private val advertiseRepository: AdvertiseRepository) :
    ViewModel() {

    var navigationForStepper: MutableLiveData<String> = MutableLiveData()
        private set

    var stepViewLastTick: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var companyContactDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var companyBasicDetailsMap: MutableMap<String, String> = mutableMapOf()
        private set

    var selectedTemplatePageName: MutableLiveData<AdvertiseActivePageResponseItem> =
        MutableLiveData()
        private set

    var selectedTemplateLocation: MutableLiveData<AdvertisePageLocationResponseItem> =
        MutableLiveData()
        private set

    var isRenewAdvertise = false
        private set

    var isUpdateAdvertise = false
        private set

    var advertiseId = 0
        private set

    var vasList = mutableListOf<String>()

    val postedAdvertiseData: MutableLiveData<Resource<PostAdvertiseResponse>> = MutableLiveData()

    val renewAdvertiseData: MutableLiveData<Resource<String>> = MutableLiveData()

    val uploadAdvertiseImageData: MutableLiveData<Resource<String>> = MutableLiveData()

    val activeTemplateDataForSpinner: MutableLiveData<Resource<ActiveTemplateResponse>> =
        MutableLiveData()

    val advertiseActiveVasData: MutableLiveData<Resource<AdvertiseActiveVasResponse>> =
        MutableLiveData()

    val advertisePageData: MutableLiveData<Resource<AdvertiseActivePageResponse>> =
        MutableLiveData()

    val advertisePageLocationData: MutableLiveData<Resource<AdvertisePageLocationResponse>> =
        MutableLiveData()

    val advertiseImage: MutableLiveData<String> = MutableLiveData()

    val updateAdvertiseData: MutableLiveData<Resource<UpdateAdvertiseResponse>> = MutableLiveData()

    fun addCompanyContactDetails(
        companyName: String,
        location: String,
        phoneNumber: String,
        email: String,
        services: String,
        link: String,
        description: String,
    ) {
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_NAME] = companyName
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LOCATION] = location
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PHONE_NUMBER] = phoneNumber
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_EMAIL] = email
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_PRODUCT_SERVICES_DETAILS] = services
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_LINK] = link
        companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_DESCRIPTION] = description
    }

    fun addCompanyBasicDetailsMap(
        addTitle: String,
        templateName: String,
        advertiseValidity: String,
        planCharges: String,
        costOfValue: String,
        isEmailPromotional: Boolean,
        isFlashingAdvertisement: Boolean,
        templateCode: String,
        advertiseImageUri: String,
        description: String,
    ) {
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_ADD_TITLE] = addTitle
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_TEMPLATE] = templateName
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_ADD_VALIDITY] = advertiseValidity
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_PLAN_CHARGES] = planCharges
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_COST_OF_VALUE] = costOfValue
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_TEMPLATE_CODE] = templateCode
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_IMAGE_URI] = advertiseImageUri
        companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_AD_DESCRIPTION] = description
        if (isEmailPromotional) {
            if (!vasList.contains("")) {
                vasList.add("EPAD")
            }
        }
        if (isFlashingAdvertisement) {
            if (!vasList.contains("FLAD")) {
                vasList.add("FLAD")
            }
        }
    }

    fun getAllActiveAdvertisePage() = viewModelScope.launch {
        try {
            advertisePageData.postValue(Resource.Loading())
            val response = advertiseRepository.getAllActiveAdvertisePage()
            advertisePageData.postValue(handleActivePageResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            advertisePageData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleActivePageResponse(response: Response<AdvertiseActivePageResponse>): Resource<AdvertiseActivePageResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAdvertisePageLocationById(advertiseId: Int) = viewModelScope.launch {
        try {
            advertisePageLocationData.postValue(Resource.Loading())
            val response = advertiseRepository.getAdvertisePageLocationById(advertiseId)
            advertisePageLocationData.postValue(handleAdvertisePageLocationResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            advertisePageLocationData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleAdvertisePageLocationResponse(response: Response<AdvertisePageLocationResponse>): Resource<AdvertisePageLocationResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAdvertiseActiveVas(locationCode: String) = viewModelScope.launch {
        try {
            advertiseActiveVasData.postValue(Resource.Loading())
            val response = advertiseRepository.getAdvertiseActiveVas(locationCode)
            advertiseActiveVasData.postValue(handleActiveVasResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            advertiseActiveVasData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleActiveVasResponse(response: Response<AdvertiseActiveVasResponse>): Resource<AdvertiseActiveVasResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getActiveTemplateForSpinner() = viewModelScope.launch {
        try {
            activeTemplateDataForSpinner.postValue(Resource.Loading())
            val response = advertiseRepository.getActiveTemplateForSpinner()
            activeTemplateDataForSpinner.postValue(handleAdvertiseTemplateResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            activeTemplateDataForSpinner.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleAdvertiseTemplateResponse(response: Response<ActiveTemplateResponse>): Resource<ActiveTemplateResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun postAdvertise(postAdvertiseRequest: PostAdvertiseRequest) = viewModelScope.launch {
        try {
            postedAdvertiseData.postValue(Resource.Loading())
            val response = advertiseRepository.postAdvertise(postAdvertiseRequest)
            postedAdvertiseData.postValue(handlePostAdvertiseResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            postedAdvertiseData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handlePostAdvertiseResponse(response: Response<PostAdvertiseResponse>): Resource<PostAdvertiseResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun uploadAdvertiseImage(advertiseId: Int, file: MultipartBody.Part) =
        viewModelScope.launch {
            try {
                uploadAdvertiseImageData.postValue(Resource.Loading())
                val response = advertiseRepository.uploadAdvertiseImage(advertiseId, file)
                uploadAdvertiseImageData.postValue(handleUploadImageResponse(response))
            } catch (e: Exception) {
                e.printStackTrace()
                uploadAdvertiseImageData.postValue(e.message?.let { Resource.Error(it) })
            }
        }

    private fun handleUploadImageResponse(response: Response<String>): Resource<String>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun renewAdvertise(renewAdvertiseRequest: RenewAdvertiseRequest) = viewModelScope.launch {
        try {
            renewAdvertiseData.postValue(Resource.Loading())
            val response = advertiseRepository.renewAdvertise(renewAdvertiseRequest)
            renewAdvertiseData.postValue(handleRenewAdvertiseResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            renewAdvertiseData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleRenewAdvertiseResponse(response: Response<String>): Resource<String>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun updateAdvertise(updateAdvertiseRequest: UpdateAdvertiseRequest) = viewModelScope.launch {
        try {
            updateAdvertiseData.postValue(Resource.Loading())
            val response = advertiseRepository.updateAdvertise(updateAdvertiseRequest)
            updateAdvertiseData.postValue(handlerUpdateAdvertiseResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            updateAdvertiseData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handlerUpdateAdvertiseResponse(response: Response<UpdateAdvertiseResponse>): Resource<UpdateAdvertiseResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setTemplatePageName(value: AdvertiseActivePageResponseItem) {
        selectedTemplatePageName.postValue(value)
    }

    fun setTemplateLocation(value: AdvertisePageLocationResponseItem) {
        selectedTemplateLocation.postValue(value)
    }

    fun setStepViewLastTick(value: Boolean) {
        stepViewLastTick.postValue(value)
    }

    fun setNavigationForStepper(value: String) {
        navigationForStepper.postValue(value)
    }

    fun setIsUpdateOrRenewAdvertise(renewAdvertise: Boolean, updateAdvertise: Boolean) {
        isRenewAdvertise = renewAdvertise
        isUpdateAdvertise = updateAdvertise
    }

    fun setAdvertiseImage(value: String) {
        advertiseImage.postValue(value)
    }

    fun setAdvertiseId(value: Int) {
        advertiseId = value
    }


}