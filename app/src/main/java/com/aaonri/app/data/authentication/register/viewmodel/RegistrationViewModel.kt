package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerificationResponse
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterationResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.aaonri.app.data.classified.model.GetClassifiedSellerResponse
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
@Inject constructor(
    private val repository: RegistrationRepository,
) : ViewModel() {

    var service: MutableLiveData<Resource<ServicesResponse>> = MutableLiveData()

    val findByEmailData: MutableLiveData<Resource<GetClassifiedSellerResponse>> =
        MutableLiveData()

    val emailAlreadyRegisterData: MutableLiveData<Resource<EmailVerificationResponse>> =
        MutableLiveData()

    val loginData: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    val registerData: MutableLiveData<Resource<RegisterationResponse>> = MutableLiveData()

    fun getServices() = viewModelScope.launch {
        try {
            service.postValue(Resource.Loading())
            val response = repository.getServicesInterest()
            service.postValue(handleServiceResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            service.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleServiceResponse(response: Response<ServicesResponse>): Resource<ServicesResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    fun loginUser(login: Login) = viewModelScope.launch {
        try {
            loginData.postValue(Resource.Loading())
            val response = repository.loginUser(login)
            loginData.postValue(handleLoginResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            loginData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): Resource<LoginResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun isEmailAlreadyRegister(emailVerifyRequest: EmailVerifyRequest) = viewModelScope.launch {
        try {
            emailAlreadyRegisterData.postValue(Resource.Loading())
            val response = repository.isEmailAlreadyRegistered(emailVerifyRequest)
            emailAlreadyRegisterData.postValue(handleIsEmailVerifyResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emailAlreadyRegisterData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleIsEmailVerifyResponse(response: Response<EmailVerificationResponse>): Resource<EmailVerificationResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun registerUser(registerRequest: RegisterRequest) = viewModelScope.launch {
        try {
            registerData.postValue(Resource.Loading())
            val response = repository.registerUser(registerRequest)
            registerData.postValue(handleRegisterResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            registerData.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun handleRegisterResponse(response: Response<RegisterationResponse>): Resource<RegisterationResponse>? {
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
            val response = repository.findByEmail(email)
            findByEmailData.postValue(handleClassifiedSellerNameResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
            findByEmailData.postValue(e.message?.let { Resource.Error(it) })
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


}