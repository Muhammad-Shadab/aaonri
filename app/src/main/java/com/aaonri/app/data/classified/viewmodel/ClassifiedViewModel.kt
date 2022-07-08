package com.aaonri.app.data.classified.viewmodel

import android.content.pm.ResolveInfo
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

    val favoriteClassifiedData: MutableLiveData<Resource<FavoriteClassifiedResponse>> =
        MutableLiveData()

    val classifiedByUserData: MutableLiveData<Resource<GetClassifiedsByUserResponse>> =
        MutableLiveData()

    val likeDislikeClassifiedData: MutableLiveData<Resource<LikeDislikeClassifiedResponse>> =
        MutableLiveData()

    val classifiedSellerNameData: MutableLiveData<Resource<GetClassifiedSellerResponse>> =
        MutableLiveData()

    val classifiedAdDetailsData : MutableLiveData<Resource<ClassifiedAdDetailsResponse>> = MutableLiveData()

    val classifiedLikeDislikeInfoData: MutableLiveData<Resource<String>> =
        MutableLiveData()

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
        favoriteClassifiedData.postValue(Resource.Loading())
        val response = classifiedRepository.getFavoriteClassified(userEmail)
        favoriteClassifiedData.postValue(handleFavoriteClassifiedResponse(response))
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
            classifiedByUserData.postValue(Resource.Loading())
            val response = classifiedRepository.getClassifiedByUser(getClassifiedsByUserRequest)
            classifiedByUserData.postValue(handleGetClassifiedUserResponse(response))
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
            likeDislikeClassifiedData.postValue(Resource.Loading())
            val response = classifiedRepository.likeDislikeClassified(likeDislikeClassifiedRequest)
            likeDislikeClassifiedData.postValue(handleLikeDislikeClassifiedResponse(response))
        }

    private fun handleLikeDislikeClassifiedResponse(response: Response<LikeDislikeClassifiedResponse>): Resource<LikeDislikeClassifiedResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getClassifiedSellerName(email: String) = viewModelScope.launch {
        classifiedSellerNameData.postValue(Resource.Loading())
        val response = classifiedRepository.getClassifiedSellerName(email)
        classifiedSellerNameData.postValue(handleClassifiedSellerNameResponse(response))
    }

    private fun handleClassifiedSellerNameResponse(response: Response<GetClassifiedSellerResponse>): Resource<GetClassifiedSellerResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    fun getClassifiedAdDetails(addId : Int) = viewModelScope.launch {
        classifiedAdDetailsData.postValue(Resource.Loading())
        val response = classifiedRepository.getClassifiedAddDetails(addId)
        classifiedAdDetailsData.postValue(handleClassifiedAdDetails(response))
    }

    private fun handleClassifiedAdDetails(response: Response<ClassifiedAdDetailsResponse>): Resource<ClassifiedAdDetailsResponse>? {
      if(response.isSuccessful)
          response.body()?.let {
              return Resource.Success(it)
          }
        return Resource.Error(response.message())
    }

    fun getClassifiedLikeDislikeInfo(email: String, addId: Int, service: String) =
        viewModelScope.launch {
            classifiedLikeDislikeInfoData.postValue(Resource.Loading())
            val response = classifiedRepository.getClassifiedLikeDislikeInfo(email, addId, service)
            classifiedLikeDislikeInfoData.postValue(handleClassifiedLikeDislikeInfoResponse(response))
        }

    private fun handleClassifiedLikeDislikeInfoResponse(response: String): Resource<String>? {
        if (response.isNotEmpty()) {
            return Resource.Success(response)
        }
        return Resource.Error("Empty")
    }


}