package com.aaonri.app.data.classified.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.classified.model.AllUserAdsClassifiedResponse
import com.aaonri.app.data.classified.model.GetClassifiedsByUserRequest
import com.aaonri.app.data.classified.model.GetClassifiedsByUserResponse
import com.aaonri.app.data.classified.repository.ClassifiedRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ClassifiedViewModel @Inject constructor(private val classifiedRepository: ClassifiedRepository) :
    ViewModel() {

    var allUserAdsClassifiedData: MutableLiveData<Resource<AllUserAdsClassifiedResponse>> =
        MutableLiveData()

    var classifiedByUserData: MutableLiveData<Resource<GetClassifiedsByUserResponse>> =
        MutableLiveData()

    fun getAllUserAdsClassified(email: String) = viewModelScope.launch {
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
    }

    fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedsByUserRequest) =
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

}