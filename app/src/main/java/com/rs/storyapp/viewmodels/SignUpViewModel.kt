package com.rs.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rs.storyapp.R
import com.rs.storyapp.data.remote.ApiConfig
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.ResponseMessage
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Rahmat Sugiarto on 26/09/2022
 */
class SignUpViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message

    private val _messageWhenFailure = MutableLiveData<String>()
    val messageWhenFailure: LiveData<String> = _messageWhenFailure

    private val _isSuccessCreated = MutableLiveData<Boolean>()
    val isSuccessCreated: LiveData<Boolean> = _isSuccessCreated

    fun getResponseRegister(requestSignUp: RequestSignUp) {
        _isLoading.value = true

        val api = ApiConfig.getApiService().signUp(requestSignUp)
        api.enqueue(object : retrofit2.Callback<ResponseMessage> {

            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {

                when (response.code()) {
                    201 -> {
                        _isSuccessCreated.value = true
                        _message.value = R.string.success_create_account
                    }
                    400 -> {
                        _isSuccessCreated.value = false
                        _message.value = R.string.email_already_exists
                    }
                    408 -> {
                        _isSuccessCreated.value = false
                        _message.value = R.string.timeout
                    }
                    else -> {
                        _isSuccessCreated.value = false
                        _messageWhenFailure.value = "Error ${response.message()}"
                    }

                }
                _isLoading.value = false

            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                _isSuccessCreated.value = false
                _messageWhenFailure.value = t.message
                _isLoading.value = false
            }
        })
    }
}