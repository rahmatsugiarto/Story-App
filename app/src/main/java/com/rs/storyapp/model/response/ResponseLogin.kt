package com.rs.storyapp.model.response


import com.google.gson.annotations.SerializedName

/**
 * Created by Rahmat Sugiarto on 26/09/2022
 */
data class ResponseLogin(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("loginResult")
    val loginResult: LoginResult,
    @SerializedName("message")
    val message: String
)

data class LoginResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: String
)