package com.rs.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.rs.storyapp.common.util.wrapEspressoIdlingResource
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.StoryRemoteMediator
import com.rs.storyapp.data.local.database.StoryDatabase
import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.model.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Rahmat Sugiarto on 10/10/2022
 */
class StoryRepository(private val apiService: ApiService, private val database: StoryDatabase) {

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String): LiveData<PagingData<StoryEntity>> {
        wrapEspressoIdlingResource {
            return Pager(
                config = PagingConfig(pageSize = 5),
                remoteMediator = StoryRemoteMediator(token, database, apiService),
                pagingSourceFactory = {
                    database.storyDao().getAllStories()
                }).liveData
        }
    }

    fun getStoriesWithLocation(
        token: String
    ): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val response = apiService.getStories("Bearer $token", size = 20, location = 1)
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.toString()))
            }
        }
    }


    fun uploadImage(
        file: MultipartBody.Part,
        desc: RequestBody,
        token: String,
        latitude: RequestBody? = null,
        longitude: RequestBody? = null
    ): LiveData<Result<MessageResponse>> = liveData {
        emit(Result.Loading)
        wrapEspressoIdlingResource {
            try {
                val response =
                    apiService.uploadImage(
                        file = file,
                        description = desc,
                        token = "Bearer $token",
                        latitude,
                        longitude
                    )
                emit(Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e.toString()))
            }
        }
    }


    companion object {
        private var INSTANCE: StoryRepository? = null

        fun getInstance(apiService: ApiService, database: StoryDatabase): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                StoryRepository(apiService, database)
                    .also { INSTANCE = it }
            }
        }
    }
}