package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.model.community.CommunitiesListResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
@Inject constructor(
    private val repository: RegistrationRepository,
) : ViewModel() {

    private var mutableCommunities: MutableStateFlow<Resource<CommunitiesListResponse>> =
        MutableStateFlow(Resource.Empty())
    val communities: StateFlow<Resource<CommunitiesListResponse>> = mutableCommunities

    private var mutableCountries: MutableStateFlow<Resource<CountriesResponse>> =
        MutableStateFlow(Resource.Empty())
    val countriesList: StateFlow<Resource<CountriesResponse>> = mutableCountries

    val loginData: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    fun getCommunities() = viewModelScope.launch {
        mutableCommunities.value = Resource.Loading()
        repository.getCommunitiesList().catch { e ->
            mutableCommunities.value = Resource.Error(e.localizedMessage)
        }.collect { response ->
            mutableCommunities.value = Resource.Success(response)
        }
    }

    fun getCountriesList() = viewModelScope.launch {
        mutableCountries.value = Resource.Loading()
        repository.getCountriesList().catch { e ->
            mutableCountries.value = Resource.Error(e.localizedMessage)
        }.collect { response ->
            mutableCountries.value = Resource.Success(response)
        }
    }


    fun loginUser(login: Login) = viewModelScope.launch {
        loginData.postValue(Resource.Loading())
        val response = repository.loginUser(login)
        loginData.postValue(handleLoginResponse(response))
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): Resource<LoginResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}