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
}