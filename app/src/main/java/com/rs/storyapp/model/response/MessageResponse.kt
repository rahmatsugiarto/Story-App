package com.rs.storyapp.model.response


import com.google.gson.annotations.SerializedName

/**
 * Created by Rahmat Sugiarto on 26/09/2022
 */
data class ResponseMessage(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)