package com.rs.storyapp.data.remote

import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.ResponseLogin
import com.rs.storyapp.model.response.ResponseMessage
import com.rs.storyapp.model.response.ResponseStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Rahmat Sugiarto on 26/09/2022
 */
interface ApiService {
    @POST("register")
    fun signUp(
        @Body requestRegister: RequestSignUp
    ): Call<ResponseMessage>

    @POST("login")
    fun getUserLogin(@Body requestLogin: RequestLogin): Call<ResponseLogin>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String
    ): Call<ResponseStory>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseMessage>
}