package com.rs.storyapp.viewmodels

import androidx.lifecycle.ViewModel
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
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

    fun getToken(): Flow<String?> = authRepository.getToken()
}