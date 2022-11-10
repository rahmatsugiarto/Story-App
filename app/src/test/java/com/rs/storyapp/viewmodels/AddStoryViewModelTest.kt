package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.data.repository.StoryRepository
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
 * Created by Rahmat Sugiarto on 22/10/2022
 */
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var addStoryViewModel: AddStoryViewModel

    private val dummyToken = "token"
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyDescRequestBody()
    private val expectedAddStory = MutableLiveData<Result<MessageResponse>>()

    @Before
    fun setup() {
        addStoryViewModel = AddStoryViewModel(authRepository, storyRepository)
    }


    @Test
    fun `when getToken successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)

        Mockito.`when`(authRepository.getToken()).thenReturn(expectedToken)

        addStoryViewModel.getToken().collect { actualToken ->
            assertNotNull(actualToken)
            assertEquals(dummyToken, actualToken)
        }

        Mockito.verify(authRepository).getToken()
    }


    @Test
    fun `when addStory Should Not Null and Return Result(Success)`() {
        val dummyAddStoryResponse = DataDummy.generateDummyMessageResponse()
        expectedAddStory.value = Result.Success(dummyAddStoryResponse)

        checkExpectedAddStoryFromFunctionAddStory(expectedAddStory)
        val actualAddStory = actualAddStory()

        Mockito.verify(storyRepository).uploadImage(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        )
        assertNotNull(actualAddStory)
        Assert.assertTrue(actualAddStory is Result.Success)
        assertEquals(
            dummyAddStoryResponse.message,
            (actualAddStory as Result.Success).data.message
        )
    }


    @Test
    fun `when addStory Should Not Null and Return Result(Loading)`() {
        expectedAddStory.value = Result.Loading

        checkExpectedAddStoryFromFunctionAddStory(expectedAddStory)
        val actualAddStory = actualAddStory()

        Mockito.verify(storyRepository).uploadImage(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        )
        assertNotNull(actualAddStory)
        Assert.assertTrue(actualAddStory is Result.Loading)
    }


    @Test
    fun `when addStory when userLogin Should Not Null and Return Result(Error)`() {
        expectedAddStory.value = Result.Error("Error")

        checkExpectedAddStoryFromFunctionAddStory(expectedAddStory)
        val actualAddStory = actualAddStory()

        Mockito.verify(storyRepository).uploadImage(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        )
        assertNotNull(actualAddStory)
        Assert.assertTrue(actualAddStory is Result.Error)
    }

    private fun actualAddStory(): Result<MessageResponse> {
        return addStoryViewModel.addStory(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        ).getOrAwaitValue()
    }

    private fun checkExpectedAddStoryFromFunctionAddStory(expectedAddStory: MutableLiveData<Result<MessageResponse>>) {
        Mockito.`when`(
            storyRepository.uploadImage(
                dummyMultipart,
                dummyDescription,
                dummyToken,
                null,
                null
            )
        ).thenReturn(expectedAddStory)
    }
}