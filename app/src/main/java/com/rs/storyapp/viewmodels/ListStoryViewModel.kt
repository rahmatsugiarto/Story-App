package com.rs.storyapp.viewmodels

import androidx.lifecycle.*
import com.rs.storyapp.R
import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.data.remote.ApiConfig
import com.rs.storyapp.model.response.ResponseStory
import com.rs.storyapp.model.response.Story
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class ListStoryViewModel(private val pref: DataUserPreference) : ViewModel() {
    var listStories: List<Story> = listOf()

    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message

    private val _messageWhenFailure = MutableLiveData<String>()
    val messageWhenFailure: LiveData<String> = _messageWhenFailure

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess


    fun getStories(token: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getStories("Bearer $token")
        api.enqueue(object : retrofit2.Callback<ResponseStory> {

            override fun onResponse(call: Call<ResponseStory>, response: Response<ResponseStory>) {
                val responseBody = response.body()
                when (response.code()) {
                    200 -> {
                        if (responseBody != null) {
                            listStories = responseBody.listStory
                        }
                        _isSuccess.value = true
                    }
                    408 -> {
                        _isSuccess.value = false
                        _message.value = R.string.timeout
                    }
                    else -> {
                        _isSuccess.value = false
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseStory>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                _messageWhenFailure.value = t.message.toString()
            }

        })
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun resetDataUser() {
        viewModelScope.launch {
            pref.saveIsLogin(false)
            pref.saveToken("")
        }
    }
}

class ListStoryViewModelFactory(private val pref: DataUserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListStoryViewModel::class.java)) {
            return ListStoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}