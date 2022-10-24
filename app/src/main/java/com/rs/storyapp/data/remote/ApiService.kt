package com.rs.storyapp.data.remote

import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.LoginResponse
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.model.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by Rahmat Sugiarto on 26/09/2022
 */
interface ApiService {
    @POST("register")
    suspend fun userSignUp(
        @Body requestRegister: RequestSignUp
    ): MessageResponse

    @POST("login")
    suspend fun userLogin(@Body requestLogin: RequestLogin): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String,
        @Part("lat") latitude: RequestBody?,
        @Part("lon") longitude: RequestBody?,
    ): MessageResponse
}