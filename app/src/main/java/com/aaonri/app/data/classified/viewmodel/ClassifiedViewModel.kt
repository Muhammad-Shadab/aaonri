package com.aaonri.app.data.classified.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.model.*
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ClassifiedViewModel @Inject constructor(private val classifiedRepository: ClassifiedRepository) :
    ViewModel() {

    /* var allUserAdsClassifiedData: MutableLiveData<Resource<AllUserAdsClassifiedResponse>> =
         MutableLiveData()*/

    val callClassifiedDetailsApiAfterUpdating: MutableLiveData<Boolean> = MutableLiveData()

    val callClassifiedApiAfterDelete: MutableLiveData<Boolean> = MutableLiveData()

    val favoriteClassifiedData: MutableLiveData<Resource<FavoriteClassifiedResponse>> =
        MutableLiveData()

    val classifiedByUserData: MutableLiveData<Resource<GetClassifiedsByUserResponse>> =
        MutableLiveData()

    val myClassified: MutableLiveData<Resource<GetClassifiedsByUserResponse>> =
        MutableLiveData()

    val likeDislikeClassifiedData: MutableLiveData<Resource<LikeDislikeClassifiedResponse>> =
        MutableLiveData()

    val classifiedSellerNameData: MutableLiveData<Resource<GetClassifiedSellerResponse>> =
        MutableLiveData()

    val findByEmailData: MutableLiveData<Resource<GetClassifiedSellerResponse>> =
        MutableLiveData()

    val classifiedLikeDislikeInfoData: MutableLiveData<Resource<String>> =
        MutableLiveData()

    var allClassifiedList = mutableListOf<UserAds>()
        private set

    var isLikedButtonClicked: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var selectedServiceRow: String = ""
        private set

    /*fun getAllUserAdsClassified(email: String) = viewModelScope.launch {
        allUserAdsClassifiedData.postValue(Resource.Loading())
        val response = classifiedRepository.getAllUserAdsClassified(email)
        allUserAdsClassifiedData.postValue(handleAllPopularClassifiedResponse(response))
    }

    private fun handleAllPopularClassifiedResponse(response: Response<AllUserAdsClassifiedResponse>): Resource<AllUserAdsClassifiedResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }*/

    fun getFavoriteClassified(userEmail: String) = viewModelScope.launch {
        try {
            favoriteClassifiedData.postValue(Resource.Loading())
            val response = classifiedRepository.getFavoriteClassified(userEmail)
            favoriteClassifiedData.postValue(handleFavoriteClassifiedResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleFavoriteClassifiedResponse(response: Response<FavoriteClassifiedResponse>): Resource<FavoriteClassifiedResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        viewModelScope.launch {
            try {
                classifiedByUserData.postValue(Resource.Loading())
                val response = classifiedRepository.getClassifiedByUser(getClassifiedsByUserRequest)
                classifiedByUserData.postValue(handleGetClassifiedUserResponse(response))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun getMyClassified(getClassifiedRequest: GetClassifiedByUserRequest) = viewModelScope.launch {
        try {
            myClassified.postValue((Resource.Loading()))
            val response = classifiedRepository.getClassifiedByUser(getClassifiedRequest)
            myClassified.postValue(handleGetClassifiedUserResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun handleGetClassifiedUserResponse(response: Response<GetClassifiedsByUserResponse>): Resource<GetClassifiedsByUserResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun likeDislikeClassified(likeDislikeClassifiedRequest: LikeDislikeClassifiedRequest) =
        viewModelScope.launch {
            try {
                likeDislikeClassifiedData.postValue(Resource.Loading())
                val response =
                    classifiedRepository.likeDislikeClassified(likeDislikeClassifiedRequest)
                likeDislikeClassifiedData.postValue(handleLikeDislikeClassifiedResponse(response))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun handleLikeDislikeClassifiedResponse(response: Response<LikeDislikeClassifiedResponse>): Resource<LikeDislikeClassifiedResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findByEmail(email: String) = viewModelScope.launch {
        try {
            findByEmailData.postValue(Resource.Loading())
            val response = classifiedRepository.getClassifiedSellerName(email)
            findByEmailData.postValue(handleClassifiedSellerNameResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getClassifiedSellerName(email: String) = viewModelScope.launch {
        try {
            classifiedSellerNameData.postValue(Resource.Loading())
            val response = classifiedRepository.getClassifiedSellerName(email)
            classifiedSellerNameData.postValue(handleClassifiedSellerNameResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleClassifiedSellerNameResponse(response: Response<GetClassifiedSellerResponse>): Resource<GetClassifiedSellerResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun getClassifiedLikeDislikeInfo(email: String, addId: Int, service: String) =
        viewModelScope.launch {
            try {
                classifiedLikeDislikeInfoData.postValue(Resource.Loading())
                val response =
                    classifiedRepository.getClassifiedLikeDislikeInfo(email, addId, service)
                classifiedLikeDislikeInfoData.postValue(
                    handleClassifiedLikeDislikeInfoResponse(
                        response
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private fun handleClassifiedLikeDislikeInfoResponse(response: String): Resource<String>? {
        if (response.isNotEmpty()) {
            return Resource.Success(response)
        }
        return Resource.Error("Empty")
    }

    fun setClassifiedForHomeScreen(value: List<UserAds>) {
        allClassifiedList = value as MutableList<UserAds>
    }

    fun setIsLikedButtonClicked(value: Boolean) {
        isLikedButtonClicked.postValue(value)
    }

    fun setCallClassifiedDetailsApiAfterUpdating(value: Boolean) {
        callClassifiedDetailsApiAfterUpdating.postValue(value)
    }

    fun setCallClassifiedApiAfterDelete(value: Boolean) {
        callClassifiedApiAfterDelete.postValue(value)
    }

    fun setSelectedServiceRow(value: String) {
        selectedServiceRow = value
    }

}