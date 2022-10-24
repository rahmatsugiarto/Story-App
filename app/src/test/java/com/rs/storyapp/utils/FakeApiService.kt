package com.rs.storyapp.data

import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.LoginResponse
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.model.response.StoryResponse
import com.rs.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
class FakeApiService : ApiService {
    private val dummyMessageResponse = DataDummy.generateDummyMessageResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()

    override suspend fun userSignUp(requestRegister: RequestSignUp): MessageResponse {
        return dummyMessageResponse;
    }

    override suspend fun userLogin(requestLogin: RequestLogin): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): StoryResponse {
        return dummyStoryResponse
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): MessageResponse {
        return dummyMessageResponse;
    }
}