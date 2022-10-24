package com.rs.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.rs.storyapp.common.util.wrapEspressoIdlingResource
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.local.preference.DataUserPreference
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.LoginResponse
import com.rs.storyapp.model.response.MessageResponse
import kotlinx.coroutines.flow.Flow


/**
 * Created by Rahmat Sugiarto on 10/10/2022
 */
class AuthRepository(
    private val apiService: ApiService,
    private val preference: DataUserPreference,
) {

    fun userSignUp(
        requestRegister: RequestSignUp
    ): LiveData<Result<MessageResponse>> = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val response = apiService.userSignUp(requestRegister)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.message.toString()))
            }
        }
    }


    fun userLogin(requestLogin: RequestLogin): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val response = apiService.userLogin(requestLogin)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }


    suspend fun saveToken(token: String) = preference.saveToken(token)

    fun getToken(): Flow<String> = preference.getToken()

    suspend fun deleteUser() = preference.deleteUser()


    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            preference: DataUserPreference,
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, preference)
            }.also { instance = it }
    }

}