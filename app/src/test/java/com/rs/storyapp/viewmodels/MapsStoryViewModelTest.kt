package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.repository.StoryRepository
import com.rs.storyapp.model.response.StoryResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
 * Created by Rahmat Sugiarto on 22/10/2022
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mapsStoryViewModel: MapsStoryViewModel

    private val dummyToken = "token"
    private val expectedStoriesWithLocation = MutableLiveData<Result<StoryResponse>>()


    @Before
    fun setup() {
        mapsStoryViewModel = MapsStoryViewModel(storyRepository)
    }

    @Test
    fun `when getStories with location Should Not Null and Return Result(Success)`() {
        val dummyGetStoriesResponse = DataDummy.generateDummyStoryResponse()
        expectedStoriesWithLocation.value = Result.Success(dummyGetStoriesResponse)

        Mockito.`when`(mapsStoryViewModel.getStoriesWithLocation(dummyToken))
            .thenReturn(expectedStoriesWithLocation)

        val actualGetStories =
            mapsStoryViewModel.getStoriesWithLocation(dummyToken).getOrAwaitValue()

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
        expectedStoriesWithLocation.value = Result.Loading

        Mockito.`when`(mapsStoryViewModel.getStoriesWithLocation(dummyToken))
            .thenReturn(expectedStoriesWithLocation)

        val actualGetStories =
            mapsStoryViewModel.getStoriesWithLocation(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(dummyToken)
        assertNotNull(actualGetStories)
        Assert.assertTrue(actualGetStories is Result.Loading)
    }

    @Test
    fun `when getStories Should Not Null and Return Result(Error)`() {
        expectedStoriesWithLocation.value = Result.Error("throw exception")

        Mockito.`when`(mapsStoryViewModel.getStoriesWithLocation(dummyToken))
            .thenReturn(expectedStoriesWithLocation)

        val actualGetStories =
            mapsStoryViewModel.getStoriesWithLocation(dummyToken).getOrAwaitValue()

        Mockito.verify(storyRepository).getStoriesWithLocation(dummyToken)
        assertNotNull(actualGetStories)
        Assert.assertTrue(actualGetStories is Result.Error)
    }
}