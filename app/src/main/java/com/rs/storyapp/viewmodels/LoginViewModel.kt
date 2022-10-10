package com.rs.storyapp.viewmodels

import androidx.lifecycle.*
import com.rs.storyapp.R
import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.data.remote.ApiConfig
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.response.ResponseLogin
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class LoginViewModel(private val pref: DataUserPreference) : ViewModel() {
    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message

    private val _messageWhenFailure = MutableLiveData<String>()
    val messageWhenFailure: LiveData<String> = _messageWhenFailure

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessLogin = MutableLiveData<Boolean>()
    val isSuccessLogin: LiveData<Boolean> = _isSuccessLogin

    fun login(requestLogin: RequestLogin) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getUserLogin(requestLogin)
        api.enqueue(object : retrofit2.Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                val responseBody = response.body()

                when (response.code()) {
                    200 -> {
                        _isSuccessLogin.value = true
                        if (responseBody != null) {
                            saveToken(responseBody.loginResult.token)
                            saveIsLogin()
                        } else {
                            _isSuccessLogin.value = false
                            _message.value = R.string.went_something_wrong
                        }

                    }
                    401 -> {
                        _isSuccessLogin.value = false
                        _message.value = R.string.invalid_email_pass
                    }
                    408 -> {
                        _isSuccessLogin.value = false
                        _message.value = R.string.timeout
                    }
                    else -> {
                        _isSuccessLogin.value = false
                        _messageWhenFailure.value = "Error ${response.message()}"
                    }

                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                _isSuccessLogin.value = false
                _isLoading.value = false
                _messageWhenFailure.value = t.message.toString()
            }
        })
    }

    private fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    private fun saveIsLogin() {
        viewModelScope.launch {
            pref.saveIsLogin(true)
        }
    }
}

class LoginViewModelFactory(private val pref: DataUserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
