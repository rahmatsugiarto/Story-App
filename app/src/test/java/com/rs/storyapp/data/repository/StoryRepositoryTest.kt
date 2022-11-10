package com.rs.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.local.database.StoryDatabase
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.FakeApiService
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var apiService: ApiService

    private val dummyToken = "token"
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyDescRequestBody()

    @Before
    fun setup() {
        apiService = FakeApiService()
    }

    @Test
    fun `when getStoriesTest Should Not Null`() = runTest {
        val expectedStory = DataDummy.generateDummyStoryResponse()
        val actualStory = apiService.getStories(dummyToken, size = 20, location = 1)
        assertNotNull(actualStory)
        assertEquals(expectedStory.listStory.size, actualStory.listStory.size)
    }

    @Test
    fun `when getStoriesWithLocation Should Not Null`() = runTest {
        val expectedStory = DataDummy.generateDummyStoryResponse()
        val actualStory = apiService.getStories(dummyToken, size = 20, page = 1)
        assertNotNull(actualStory)
        assertEquals(expectedStory.listStory.size, actualStory.listStory.size)
    }

    @Test
    fun `when uploadImage Should Not Null`() = runTest {
        val expectedStory = DataDummy.generateDummyMessageResponse()
        val actualStory = apiService.uploadImage(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        )
        assertNotNull(actualStory)
        assertEquals(expectedStory.message, actualStory.message)
    }
}