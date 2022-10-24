package com.rs.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Rahmat Sugiarto on 02/10/2022
 */
class AddStoryViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun addStory(
        file: MultipartBody.Part,
        desc: RequestBody,
        token: String,
        latitude: RequestBody?,
        longitude: RequestBody?
    ) =
        storyRepository.uploadImage(file, desc, token, latitude, longitude)

    fun getToken(): LiveData<String> {
        return authRepository.getToken().asLiveData()
    }

//    private val _message = MutableLiveData<Int>()
//    val message: LiveData<Int> = _message
//
//    private val _messageWhenFailure = MutableLiveData<String>()
//    val messageWhenFailure: LiveData<String> = _messageWhenFailure
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _isSuccess = MutableLiveData<Boolean>()
//    val isSuccess: LiveData<Boolean> = _isSuccess
//
//    fun addStory(file: MultipartBody.Part, desc: RequestBody, token: String) {
//        _isLoading.value = true
//        _isSuccess.value = false
//        val service = ApiConfig.getApiService().uploadImage(
//            file, desc, "Bearer $token"
//        )
//
//        service.enqueue(object : Callback<ResponseMessage> {
//            override fun onResponse(
//                call: Call<ResponseMessage>,
//                response: Response<ResponseMessage>
//            ) {
//                when (response.code()) {
//                    201 -> {
//                        _isSuccess.value = true
//                    }
//                    408 -> {
//                        _isSuccess.value = false
//                        _message.value = R.string.timeout
//                    }
//                    else -> {
//                        _isSuccess.value = false
//                        _messageWhenFailure.value = "Error ${response.message()}"
//                    }
//
//                }
//                _isLoading.value = false
//            }
//
//            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
//                _isLoading.value = false
//                _messageWhenFailure.value = t.message.toString()
//            }
//        })
//    }
//
//    fun getToken(): LiveData<String> {
//        return pref.getToken().asLiveData()
//    }
}