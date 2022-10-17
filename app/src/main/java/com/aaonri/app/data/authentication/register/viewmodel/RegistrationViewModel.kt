package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.login.model.Login
import com.aaonri.app.data.authentication.login.model.LoginResponse
import com.aaonri.app.data.authentication.login.model.ResendEmailResponse
import com.aaonri.app.data.authentication.register.model.UpdateProfileRequest
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerificationResponse
import com.aaonri.app.data.authentication.register.model.add_user.EmailVerifyRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.add_user.RegistrationResponse
import com.aaonri.app.data.authentication.register.model.services.ServicesResponse
import com.aaonri.app.data.authentication.register.repository.RegistrationRepository
import com.aaonri.app.data.classified.model.FindByEmailDetailResponse
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

    val findByEmailData: MutableLiveData<Resource<FindByEmailDetailResponse>> =
        MutableLiveData()

    val loginScreenEmail: MutableLiveData<String> = MutableLiveData()

    val loginScreenPassword: MutableLiveData<String> = MutableLiveData()

    val resendEmailVerificationData: MutableLiveData<Resource<ResendEmailResponse>> = MutableLiveData()

    val emailAlreadyRegisterData: MutableLiveData<Resource<EmailVerificationResponse>> =
        MutableLiveData()

    val loginData: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    val registerData: MutableLiveData<Resource<RegistrationResponse>> = MutableLiveData()

    val updateUserData: MutableLiveData<Resource<RegistrationResponse>> = MutableLiveData()

    fun getServices() = viewModelScope.launch {
        service.postValue(Resource.Loading())
        val response = repository.getServicesInterest()
        service.postValue(handleServiceResponse(response))
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

    fun isEmailAlreadyRegister(emailVerifyRequest: EmailVerifyRequest) = viewModelScope.launch {
        emailAlreadyRegisterData.postValue(Resource.Loading())
        val response = repository.isEmailAlreadyRegistered(emailVerifyRequest)
        emailAlreadyRegisterData.postValue(handleIsEmailVerifyResponse(response))
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
        registerData.postValue(Resource.Loading())
        val response = repository.registerUser(registerRequest)
        registerData.postValue(handleRegisterResponse(response))
    }

    fun updateProfile(updateProfileRequest: UpdateProfileRequest) = viewModelScope.launch {
        updateUserData.postValue(Resource.Loading())
        val response = repository.updateProfile(updateProfileRequest)
        updateUserData.postValue(handleRegisterResponse(response))
    }

    private fun handleRegisterResponse(response: Response<RegistrationResponse>): Resource<RegistrationResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun findByEmail(email: String) = viewModelScope.launch {
        findByEmailData.postValue(Resource.Loading())
        val response = repository.findByEmail(email)
        findByEmailData.postValue(handleClassifiedSellerNameResponse(response))
    }

    private fun handleClassifiedSellerNameResponse(response: Response<FindByEmailDetailResponse>): Resource<FindByEmailDetailResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun resendEmailVerification(email: String) = viewModelScope.launch {
        resendEmailVerificationData.postValue(Resource.Loading())
        val response = repository.resendEmailVerification(email)
        resendEmailVerificationData.postValue(handleResendEmailVerification(response))
    }

    private fun handleResendEmailVerification(response: Response<ResendEmailResponse>): Resource<ResendEmailResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


}