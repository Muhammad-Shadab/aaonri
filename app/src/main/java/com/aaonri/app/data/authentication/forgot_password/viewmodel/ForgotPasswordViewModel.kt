package com.aaonri.app.data.authentication.forgot_password.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaonri.app.data.authentication.forgot_password.model.NewPasswordRequest
import com.aaonri.app.data.authentication.forgot_password.model.ResetPassLinkResponse
import com.aaonri.app.data.authentication.forgot_password.repository.ForgotPasswordRepository
import com.aaonri.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel
@Inject constructor(private val forgotPasswordRepository: ForgotPasswordRepository) : ViewModel() {

    val forgotPasswordData: MutableLiveData<Resource<ResetPassLinkResponse>> =
        MutableLiveData()

    val verifyPassword: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()

    val newPasswordData: MutableLiveData<Resource<ResponseBody>> = MutableLiveData()

    fun sendForgotPasswordLink(userEmail: String) = viewModelScope.launch {
        try {
            forgotPasswordData.postValue(Resource.Loading())
            val response = forgotPasswordRepository.sendResetPasswordLink(userEmail)
            forgotPasswordData.postValue(handleSendForgotPassResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleSendForgotPassResponse(response: Response<ResetPassLinkResponse>): Resource<ResetPassLinkResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun verifyPassword(code: String, email: String, id: String) = viewModelScope.launch {
        try {
            verifyPassword.postValue(Resource.Loading())
            val response = forgotPasswordRepository.verifyPassword(code, email, id)
            verifyPassword.postValue(handleVerifyPasswordResponse(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleVerifyPasswordResponse(response: Response<ResponseBody>): Resource<ResponseBody>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun newPasswordRequest(newPasswordRequest: NewPasswordRequest) = viewModelScope.launch {
        try {
            newPasswordData.postValue(Resource.Loading())
            val response = forgotPasswordRepository.newPasswordRequest(newPasswordRequest)
            newPasswordData.postValue(handleNewPasswordRequest(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleNewPasswordRequest(response: Response<ResponseBody>): Resource<ResponseBody>? {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}
