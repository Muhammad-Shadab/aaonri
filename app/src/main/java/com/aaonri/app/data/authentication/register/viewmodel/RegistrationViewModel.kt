package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.register.model.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
@Inject constructor(
    private val repository: RegistrationRepository
) : ViewModel() {

    private var mutableCommunities: MutableStateFlow<Resource<CommunitiesListResponse>> =
        MutableStateFlow(Resource.Empty())
    var communities: StateFlow<Resource<CommunitiesListResponse>> = mutableCommunities

    fun getCommunities() = viewModelScope.launch {
        mutableCommunities.value = Resource.Loading()
        repository.getCommunitiesList().catch { e ->
            mutableCommunities.value = Resource.Error(e.localizedMessage)
        }.collect { response ->
            mutableCommunities.value = Resource.Success(response)
        }
    }

}