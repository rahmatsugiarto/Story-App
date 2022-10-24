package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.data.Result
import com.rs.storyapp.model.response.LoginResponse
import com.rs.storyapp.model.response.StoryResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.*
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
class MapsStoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var mapsStoryViewModel: MapsStoryViewModel

    private val dummyToken = "token"
    private val dummyStoryResponse = DataDummy.generateDummyStoryResponse()
    private val expectedStoriesWithLocation = MutableLiveData<Result<StoryResponse>>()


    @Test
    fun `when getStoriesWithLocation Should Not Null and Return LiveData(Result(StoryResponse))`() {
        //init value
        expectedStoriesWithLocation.value = Result.Loading

        Mockito.`when`(
            mapsStoryViewModel.getStoriesWithLocation(dummyToken)
        ).thenReturn(expectedStoriesWithLocation)


        when (val actualUserLogin = mapsStoryViewModel.getStoriesWithLocation(dummyToken).getOrAwaitValue()) {
            is Result.Loading -> {
                expectedStoriesWithLocation.value = Result.Loading
                assertEquals(expectedStoriesWithLocation.value, actualUserLogin)
            }
            is Result.Success -> {
                expectedStoriesWithLocation.value = Result.Success(dummyStoryResponse)
                assertNotNull(actualUserLogin)
                assertEquals(expectedStoriesWithLocation.value, actualUserLogin)
            }

            is Result.Error -> {
                expectedStoriesWithLocation.value = Result.Error("Error")
                assertNotNull(actualUserLogin)
                assertEquals(expectedStoriesWithLocation.value, actualUserLogin)

            }
        }
        Mockito.verify(mapsStoryViewModel).getStoriesWithLocation(dummyToken)
    }

}