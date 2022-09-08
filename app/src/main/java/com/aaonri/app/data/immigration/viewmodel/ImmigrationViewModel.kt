package com.aaonri.app.data.immigration.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.immigration.model.*
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

    val myImmigrationDiscussionListData: MutableLiveData<Resource<GetAllDiscussionResponse>> =
        MutableLiveData()

    val allDiscussionCategoryIsClicked: MutableLiveData<Boolean> = MutableLiveData()

    val ImmigrationCenterCategoryIsClicked: MutableLiveData<Boolean> = MutableLiveData()

    val myDiscussionCategoryIsClicked: MutableLiveData<Boolean> = MutableLiveData()

    var immigrationCenterDesc: MutableLiveData<Category> = MutableLiveData()

    val selectedAllDiscussionScreenCategory: MutableLiveData<DiscussionCategoryResponseItem> =
        MutableLiveData()

    val selectedDiscussionItem: MutableLiveData<Discussion> = MutableLiveData()

    val selectedImmigrationCenterItem: MutableLiveData<ImmigrationCenterModelItem> = MutableLiveData()

    val navigateFromAllImmigrationToDetailScreen: MutableLiveData<Boolean> = MutableLiveData()

    val navigateFromImmigrationCenterToCenterDetailScreen: MutableLiveData<Boolean> = MutableLiveData()

    val navigateFromMyImmigrationToDetailScreen: MutableLiveData<Boolean> = MutableLiveData()

    val selectedMyDiscussionScreenCategory: MutableLiveData<DiscussionCategoryResponseItem> =
        MutableLiveData()

    val selectedPostingDiscussionScreenCategory: MutableLiveData<DiscussionCategoryResponseItem> =
        MutableLiveData()

    val discussionDetailsData: MutableLiveData<Resource<DiscussionDetailsResponse>> =
        MutableLiveData()

    var keyImmigrationKeyboardListener: MutableLiveData<Boolean> = MutableLiveData()
        private set

    var setCategoryForFirstIndexForOnce = true
        private set

    var isNavigateBackFromAllImmigrationDetailScreen = false
        private set

    var isNavigateBackFromMyImmigrationDetailScreen = false
        private set

    val immigrationSearchQuery: MutableLiveData<String> = MutableLiveData()

    val immigrationFilterData: MutableLiveData<ImmigrationFilterModel> = MutableLiveData()

    val replyDiscussionData: MutableLiveData<Resource<ReplyDiscussionResponse>> = MutableLiveData()

    val postDiscussionData: MutableLiveData<Resource<PostDiscussionResponse>> = MutableLiveData()

    var immigrationCenterList: MutableLiveData<MutableList<Category>> = MutableLiveData()

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

    fun getMyImmigrationDiscussion(getAllImmigrationRequest: GetAllImmigrationRequest) =
        viewModelScope.launch {
            myImmigrationDiscussionListData.postValue(Resource.Loading())
            val response =
                immigrationRepository.getAllImmigrationDiscussion(getAllImmigrationRequest)
            myImmigrationDiscussionListData.postValue(
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

    fun setImmigrationcenterCategoryIsClicked(value: Boolean) {
        ImmigrationCenterCategoryIsClicked.postValue(value)
    }
    fun setSelectedAllDiscussionCategory(value: DiscussionCategoryResponseItem) {
        selectedAllDiscussionScreenCategory.postValue(value)
    }

    fun setSelectedMyDiscussionScreenCategory(value: DiscussionCategoryResponseItem) {
        selectedMyDiscussionScreenCategory.postValue(value)
    }

    fun setSelectedPostingDiscussionScreenCategory(value: DiscussionCategoryResponseItem) {
        selectedPostingDiscussionScreenCategory.postValue(value)
    }

    fun setSelectedDiscussionItem(value: Discussion) {
        selectedDiscussionItem.postValue(value)
    }

    fun setSelectedImmigrationCenterItem(value: ImmigrationCenterModelItem) {
        selectedImmigrationCenterItem.postValue(value)
    }

    fun setNavigateFromAllImmigrationToDetailScreen(value: Boolean) {
        navigateFromAllImmigrationToDetailScreen.postValue(value)
    }

    fun setNavigateFromMyImmigrationToDetailScreen(value: Boolean) {
        navigateFromMyImmigrationToDetailScreen.postValue(value)
    }

    fun setNavigateFromImmigrationCenterToCenterDetailScreen(value: Boolean) {
        navigateFromImmigrationCenterToCenterDetailScreen.postValue(value)
    }
    fun setImmigrationList(value:  MutableList<Category>) {
        immigrationCenterList.value = value
    }


    fun getDiscussionDetailsById(discussionId: String) = viewModelScope.launch {
        discussionDetailsData.postValue(Resource.Loading())
        val response = immigrationRepository.getDiscussionDetailsById(discussionId)
        discussionDetailsData.postValue(handleDiscussionDetailsResponse(response))
    }



    private fun handleDiscussionDetailsResponse(response: Response<DiscussionDetailsResponse>): Resource<DiscussionDetailsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setCategoryFirstIndexForOnce(value: Boolean) {
        setCategoryForFirstIndexForOnce = value
    }

    fun setIsNavigateBackFromAllImmigrationDetailScreen(value: Boolean) {
        isNavigateBackFromAllImmigrationDetailScreen = value
    }

    fun setIsNavigateBackFromMyImmigrationDetailScreen(value: Boolean) {
        isNavigateBackFromMyImmigrationDetailScreen = value
    }

    fun setKeyClassifiedKeyboardListener(value: Boolean) {
        keyImmigrationKeyboardListener.postValue(value)
    }

    fun setSearchQuery(value: String) {
        immigrationSearchQuery.postValue(value)
    }

    fun setFilterData(value: ImmigrationFilterModel) {
        immigrationFilterData.postValue(value)
    }

    fun replyDiscussion(replyDiscussionRequest: ReplyDiscussionRequest) = viewModelScope.launch {
        replyDiscussionData.postValue(Resource.Loading())
        val response = immigrationRepository.replyDiscussion(replyDiscussionRequest)
        replyDiscussionData.postValue(handleReplyDiscussionResponse(response))
    }

    private fun handleReplyDiscussionResponse(response: Response<ReplyDiscussionResponse>): Resource<ReplyDiscussionResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun postDiscussion(postDiscussionRequest: PostDiscussionRequest) = viewModelScope.launch {
        postDiscussionData.postValue(Resource.Loading())
        val response = immigrationRepository.postDiscussion(postDiscussionRequest)
        postDiscussionData.postValue(handlePostDiscussionResponse(response))
    }

    private fun handlePostDiscussionResponse(response: Response<PostDiscussionResponse>): Resource<PostDiscussionResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun setImmigrationCenterDesc(item: Category) {
            immigrationCenterDesc.value = item
    }


}