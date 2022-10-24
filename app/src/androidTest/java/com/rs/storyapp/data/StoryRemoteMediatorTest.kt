package com.rs.storyapp.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rs.storyapp.data.local.database.StoryDatabase
import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.*
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {
    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        val remoteMediator = StoryRemoteMediator(
            "token",
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override suspend fun userSignUp(requestRegister: RequestSignUp): MessageResponse {
        return MessageResponse(error = false, message = "success")
    }

    override suspend fun userLogin(requestLogin: RequestLogin): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-0DySULI_mKrYwe1R",
            name = "rahmatdev",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBEeVNVTElfbUtyWXdlMVIiLCJpYXQiOjE2NjYzNDA2ODB9.4qPa0XWqlAKc0Yte2QJ9v9smSxsWAJi97rp7HytNAK8"
        )

        return LoginResponse(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }

    override suspend fun getStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): StoryResponse {
        val items: MutableList<Story> = arrayListOf()

        for (i in 0..100) {
            val story = Story(
                createdAt = "2022-10-21T08:22:39.243Z",
                description = "galpot",
                id = "story-n2o969c3NJARQEp3",
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = "rev",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666339533827_MAvCeeoa.jpg",
            )
            items.add(story)
        }

        return StoryResponse(false, items, "Stories fetched successfully")
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        token: String,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): MessageResponse {
        return MessageResponse(error = false, message = "success")
    }
}