package com.aaonri.app.data.immigration.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponse
import com.aaonri.app.data.immigration.model.DiscussionCategoryResponseItem
import com.aaonri.app.data.immigration.model.GetAllDiscussionResponse
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import com.aaonri.app.data.immigration.repository.ImmigrationRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ImmigrationViewModel @Inject constructor(private val immigrationRepository: ImmigrationRepository) :
    ViewModel() {

    val discussionCategoryData: MutableLiveData<Resource<DiscussionCategoryResponse>> =
        MutableLiveData()

    val allImmigrationDiscussionListData: MutableLiveData<Resource<GetAllDiscussionResponse>> =
        MutableLiveData()

    val allDiscussionCategoryIsClicked: MutableLiveData<Boolean> = MutableLiveData()

    val myDiscussionCategoryIsClicked: MutableLiveData<Boolean> = MutableLiveData()

    val selectedAllDiscussionScreenCategory: MutableLiveData<DiscussionCategoryResponseItem> =
        MutableLiveData()

    val selectedMyDiscussionScreenCategory: MutableLiveData<DiscussionCategoryResponseItem> =
        MutableLiveData()

    fun getDiscussionCategory() = viewModelScope.launch {
        discussionCategoryData.postValue(Resource.Loading())
        val response = immigrationRepository.getDiscussionCategory()
        discussionCategoryData.postValue(handleDiscussionCategoryResponse(response))
    }

    private fun handleDiscussionCategoryResponse(response: Response<DiscussionCategoryResponse>): Resource<DiscussionCategoryResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getAllImmigrationDiscussion(getAllImmigrationRequest: GetAllImmigrationRequest) =
        viewModelScope.launch {
            allImmigrationDiscussionListData.postValue(Resource.Loading())
            val response =
                immigrationRepository.getAllImmigrationDiscussion(getAllImmigrationRequest)
            allImmigrationDiscussionListData.postValue(
                handleAllImmigrationDiscussionResponse(
                    response
                )
            )
        }

    private fun handleAllImmigrationDiscussionResponse(response: Response<GetAllDiscussionResponse>): Resource<GetAllDiscussionResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setOnAllDiscussionCategoryIsClicked(value: Boolean) {
        allDiscussionCategoryIsClicked.postValue(value)
    }

    fun setOnMyDiscussionCategoryIsClicked(value: Boolean) {
        myDiscussionCategoryIsClicked.postValue(value)
    }

    fun setSelectedAllDiscussionCategory(value: DiscussionCategoryResponseItem) {
        selectedAllDiscussionScreenCategory.postValue(value)
    }

    fun setSelectedMyDiscussionScreenCategory(value: DiscussionCategoryResponseItem) {
        selectedMyDiscussionScreenCategory.postValue(value)
    }

}