package com.rs.storyapp.viewmodels

import androidx.lifecycle.*
import com.rs.storyapp.R
import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.data.remote.ApiConfig
import com.rs.storyapp.model.response.ResponseMessage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Rahmat Sugiarto on 02/10/2022
 */
class AddStoryViewModel(private val pref: DataUserPreference) : ViewModel() {

    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message

    private val _messageWhenFailure = MutableLiveData<String>()
    val messageWhenFailure: LiveData<String> = _messageWhenFailure

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun addStory(file: MultipartBody.Part, desc: RequestBody, token: String) {
        _isLoading.value = true
        _isSuccess.value = false
        val service = ApiConfig.getApiService().uploadImage(
            file, desc, "Bearer $token"
        )

        service.enqueue(object : Callback<ResponseMessage> {
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {
                when (response.code()) {
                    201 -> {
                        _isSuccess.value = true
                    }
                    408 -> {
                        _isSuccess.value = false
                        _message.value = R.string.timeout
                    }
                    else -> {
                        _isSuccess.value = false
                        _messageWhenFailure.value = "Error ${response.message()}"
                    }

                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                _isLoading.value = false
                _messageWhenFailure.value = t.message.toString()
            }
        })
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}

class AddStoryViewModelFactory(private val pref: DataUserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}