package com.rs.storyapp.data

/**
 * Created by Rahmat Sugiarto on 10/10/2022
 */
sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}