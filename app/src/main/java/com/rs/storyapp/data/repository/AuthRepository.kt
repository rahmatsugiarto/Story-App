package com.rs.storyapp.data

import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.ResponseLogin
import com.rs.storyapp.model.response.ResponseMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Rahmat Sugiarto on 10/10/2022
 */
class AuthRepository(
    private val apiService: ApiService,
    private val preference: DataUserPreference,
) {

    suspend fun userSignUp(
        requestRegister: RequestSignUp
    ): Flow<Result<ResponseMessage>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.signUp(requestRegister)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun userLogin(requestLogin: RequestLogin): Flow<Result<ResponseLogin>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.userLogin(requestLogin)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun saveAuthToken(token: String) {
        preference.saveToken(token)
    }

    suspend fun saveIsLogin() {
        preference.saveIsLogin(false)
    }

    fun getAuthToken(): Flow<String?> = preference.getToken()

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