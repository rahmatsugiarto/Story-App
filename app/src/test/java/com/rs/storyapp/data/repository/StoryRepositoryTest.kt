package com.rs.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.rs.storyapp.adapter.StoryAdapter
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.model.response.StoryResponse
import com.rs.storyapp.utils.*
import com.rs.storyapp.viewmodels.ListStoryViewModel
import kotlinx.coroutines.Dispatchers
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

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var storyRepository: StoryRepository

    private val dummyToken = "token"
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val expectedGetStories = MutableLiveData<Result<StoryResponse>>()
    private val expectedUploadImage = MutableLiveData<Result<MessageResponse>>()

    @Before
    fun setup() {
        apiService = FakeApiService()
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyListStoryEntity()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories("token")).thenReturn(expectedStory)

        val listStoryViewModel = ListStoryViewModel(authRepository, storyRepository)
        val actualStory: PagingData<StoryEntity> =
            listStoryViewModel.getStories("token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }


    @Test
    fun `when getStories with location Should Not Null and Return Result(Success)`() {
        val dummyGetStoriesResponse = DataDummy.generateDummyStoryResponse()
        expectedGetStories.value = Result.Success(dummyGetStoriesResponse)

        Mockito.`when`(storyRepository.getStoriesWithLocation(dummyToken))
            .thenReturn(expectedGetStories)

        val actualGetStories = storyRepository.getStoriesWithLocation(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(dummyToken)
        assertNotNull(actualGetStories)
        Assert.assertTrue(actualGetStories is Result.Success)
        assertEquals(
            dummyGetStoriesResponse.message,
            (actualGetStories as Result.Success).data.message
        )
    }

    @Test
    fun `when getStories Should Not Null and Return Result(Loading)`() {
        expectedGetStories.value = Result.Loading

        Mockito.`when`(storyRepository.getStoriesWithLocation(dummyToken))
            .thenReturn(expectedGetStories)

        val actualGetStories = storyRepository.getStoriesWithLocation(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(dummyToken)
        assertNotNull(actualGetStories)
        Assert.assertTrue(actualGetStories is Result.Loading)
    }

    @Test
    fun `when getStories Should Not Null and Return Result(Error)`() {
        expectedGetStories.value = Result.Error("throw exception")

        Mockito.`when`(storyRepository.getStoriesWithLocation(dummyToken))
            .thenReturn(expectedGetStories)

        val actualGetStories = storyRepository.getStoriesWithLocation(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(dummyToken)
        assertNotNull(actualGetStories)
        Assert.assertTrue(actualGetStories is Result.Error)
    }

    @Test
    fun `when uploadImage Should Not Null and Return Result(Success)`() {
        val dummyUploadImageResponse = DataDummy.generateDummyMessageResponse()
        expectedUploadImage.value = Result.Success(dummyUploadImageResponse)

        Mockito.`when`(
            storyRepository.uploadImage(
                dummyMultipart,
                dummyDescription,
                dummyToken,
                null,
                null
            )
        )
            .thenReturn(expectedUploadImage)
        val actualUploadImage =
            storyRepository.uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
                .getOrAwaitValue()

        Mockito.verify(storyRepository)
            .uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
        assertNotNull(actualUploadImage)
        Assert.assertTrue(actualUploadImage is Result.Success)
        assertEquals(
            dummyUploadImageResponse.message,
            (actualUploadImage as Result.Success).data.message
        )
    }

    @Test
    fun `when uploadImage Should Not Null and Return Result(Loading)`() {
        expectedUploadImage.value = Result.Loading
        Mockito.`when`(
            storyRepository.uploadImage(
                dummyMultipart,
                dummyDescription,
                dummyToken,
                null,
                null
            )
        )
            .thenReturn(expectedUploadImage)
        val actualUploadImage =
            storyRepository.uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
                .getOrAwaitValue()

        Mockito.verify(storyRepository)
            .uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
        assertNotNull(actualUploadImage)
        Assert.assertTrue(actualUploadImage is Result.Loading)
    }

    @Test
    fun `when uploadImage Should Not Null and Return Result(Error)`() {
        expectedUploadImage.value = Result.Error("throw exception")

        Mockito.`when`(
            storyRepository.uploadImage(
                dummyMultipart,
                dummyDescription,
                dummyToken,
                null,
                null
            )
        )
            .thenReturn(expectedUploadImage)
        val actualUploadImage =
            storyRepository.uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
                .getOrAwaitValue()

        Mockito.verify(storyRepository)
            .uploadImage(dummyMultipart, dummyDescription, dummyToken, null, null)
        assertNotNull(actualUploadImage)
        Assert.assertTrue(actualUploadImage is Result.Error)
    }


    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}