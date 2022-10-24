package com.rs.storyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.model.request.RequestLogin
import kotlinx.coroutines.launch

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun userLogin(requestLogin: RequestLogin) = authRepository.userLogin(requestLogin)

    fun saveToken(token: String) = viewModelScope.launch {
        authRepository.saveToken(token)
    }


//    fun login(requestLogin: RequestLogin) {
//        _isLoading.value = true
//        val api = ApiConfig.getApiService().userLogin(requestLogin)
//        api.enqueue(object : retrofit2.Callback<ResponseLogin> {
//            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
//                val responseBody = response.body()
//
//                when (response.code()) {
//                    200 -> {
//                        _isSuccessLogin.value = true
//                        if (responseBody != null) {
//                            saveToken(responseBody.loginResult.token)
//                            saveIsLogin()
//                        } else {
//                            _isSuccessLogin.value = false
//                            _message.value = R.string.went_something_wrong
//                        }
//
//                    }
//                    401 -> {
//                        _isSuccessLogin.value = false
//                        _message.value = R.string.invalid_email_pass
//                    }
//                    408 -> {
//                        _isSuccessLogin.value = false
//                        _message.value = R.string.timeout
//                    }
//                    else -> {
//                        _isSuccessLogin.value = false
//                        _messageWhenFailure.value = "Error ${response.message()}"
//                    }
//
//                }
//                _isLoading.value = false
//            }
//
//            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
//                _isSuccessLogin.value = false
//                _isLoading.value = false
//                _messageWhenFailure.value = t.message.toString()
//            }
//        })
//    }
//
//    private fun saveToken(token: String) {
//        viewModelScope.launch {
//            pref.saveToken(token)
//        }
//    }
//
//    private fun saveIsLogin() {
//        viewModelScope.launch {
//            pref.saveIsLogin(true)
//        }
//    }
}
