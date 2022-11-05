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
@RunWith(MockitoJUnitRunner.Silent::class)
class StoryRepositoryTest {

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

//    @Mock
//    private lateinit var apiServiceMock: ApiService

    private lateinit var apiService: ApiService

//    @Mock
//    private lateinit var database: StoryDatabase
//
//    private lateinit var storyRepository: StoryRepository

    private val dummyToken = "token"
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyDescRequestBody()
//    private val expectedGetStories = DataDummy.generateDummyStoryResponse()
//    private val expectedUploadImage = DataDummy.generateDummyMessageResponse()

    @Before
    fun setup() {
//        storyRepository = StoryRepository(apiServiceMock, database)
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
//
//
//    @Test
//    fun `when getStories with location Should Not Null and Return Result(Success)`() = runTest {
//
//        `when`(apiServiceMock.getStories(token = dummyToken, size = 20, location = 1))
//            .thenReturn(expectedGetStories)
//
//        val actualGetStories = storyRepository.getStoriesWithLocation(dummyToken).getOrAwaitValue()
//
//        if (actualGetStories is Result.Success) {
//            Mockito.verify(storyRepository).getStoriesWithLocation(dummyToken)
//            assertNotNull(actualGetStories)
//            Assert.assertTrue(true)
//            assertEquals(
//                expectedGetStories.message,
//                actualGetStories.data.message
//            )
//        }
//    }
//
//    @Test
//    fun `when getStories Should Not Null and Return Result(Loading)`() = runTest {
//        `when`(apiServiceMock.getStories(token = dummyToken, size = 20, location = 1))
//            .thenReturn(expectedGetStories)
//
//        val actualGetStories = storyRepository.getStoriesWithLocation(dummyToken).getOrAwaitValue()
//
//        if (actualGetStories is Result.Loading) {
//            assertNotNull(actualGetStories)
//            Assert.assertTrue(true)
//        }
//    }
//
//    @Test
//    fun `when getStories Should Not Null and Return Result(Error)`() = runTest {
//        `when`(apiServiceMock.getStories(token = dummyToken, size = 20, location = 1))
//            .thenReturn(expectedGetStories)
//
//        val actualGetStories = storyRepository.getStoriesWithLocation(dummyToken).getOrAwaitValue()
//
//        if (actualGetStories is Result.Error) {
//            Mockito.verify(storyRepository).getStoriesWithLocation(dummyToken)
//            assertNotNull(actualGetStories)
//            Assert.assertTrue(true)
//        }
//    }
//
//    @Test
//    fun `when uploadImage Should Not Null and Return Result(Success)`() = runTest {
//        `when`(
//            apiServiceMock.uploadImage(
//                dummyMultipart,
//                dummyDescription,
//                dummyToken,
//                null,
//                null
//            )
//        )
//            .thenReturn(expectedUploadImage)
//
//        val actualUploadImage =
//            storyRepository.uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
//                .getOrAwaitValue()
//
//
//        if (actualUploadImage is Result.Success) {
//            Mockito.verify(storyRepository)
//                .uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
//            assertNotNull(actualUploadImage)
//            Assert.assertTrue(true)
//            assertEquals(
//                expectedUploadImage.message,
//                actualUploadImage.data.message
//            )
//        }
//    }
//
//    @Test
//    fun `when uploadImage Should Not Null and Return Result(Loading)`() = runTest {
//        `when`(
//            apiServiceMock.uploadImage(
//                dummyMultipart,
//                dummyDescription,
//                dummyToken,
//                null,
//                null
//            )
//        )
//            .thenReturn(expectedUploadImage)
//
//        val actualUploadImage =
//            storyRepository.uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
//                .getOrAwaitValue()
//
//        if (actualUploadImage is Result.Loading) {
//            assertNotNull(actualUploadImage)
//            Assert.assertTrue(true)
//        }
//    }
//
//    @Test
//    fun `when uploadImage Should Not Null and Return Result(Error)`() = runTest {
//        `when`(
//            apiServiceMock.uploadImage(
//                dummyMultipart,
//                dummyDescription,
//                dummyToken,
//                null,
//                null
//            )
//        )
//            .thenReturn(expectedUploadImage)
//
//        val actualUploadImage =
//            storyRepository.uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
//                .getOrAwaitValue()
//        if (actualUploadImage is Result.Error) {
//            Mockito.verify(storyRepository)
//                .uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
//            assertNotNull(actualUploadImage)
//            Assert.assertTrue(true)
//        }
//    }


//    private val noopListUpdateCallback = object : ListUpdateCallback {
//        override fun onInserted(position: Int, count: Int) {}
//        override fun onRemoved(position: Int, count: Int) {}
//        override fun onMoved(fromPosition: Int, toPosition: Int) {}
//        override fun onChanged(position: Int, count: Int, payload: Any?) {}
//    }
}