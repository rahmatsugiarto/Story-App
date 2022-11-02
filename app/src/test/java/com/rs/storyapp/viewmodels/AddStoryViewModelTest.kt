package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import com.rs.storyapp.data.Result
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
    private lateinit var addStoryViewModelMock: AddStoryViewModel

    private val dummyToken = "token"
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val expectedGetToken = MutableLiveData<String>()
    private val expectedAddStory = MutableLiveData<Result<MessageResponse>>()


    @Test
    fun `when getToken successfully`() {
        expectedGetToken.value = dummyToken

        Mockito.`when`(addStoryViewModelMock.getToken()).thenReturn(expectedGetToken)
        val actualToken = addStoryViewModelMock.getToken().getOrAwaitValue()

        Mockito.verify(addStoryViewModelMock).getToken()
        assertNotNull(actualToken)
        assertEquals(dummyToken, actualToken)
    }

    @Test
    fun `when addStory Should Not Null and Return Result(Success)`() {
        val dummyAddStoryResponse = DataDummy.generateDummyMessageResponse()
        expectedAddStory.value = Result.Success(dummyAddStoryResponse)

        checkExpectedAddStoryFromFunctionAddStory(expectedAddStory)
        val actualAddStory = actualAddStory()

        Mockito.verify(addStoryViewModelMock).addStory(
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

        Mockito.verify(addStoryViewModelMock).addStory(
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

        Mockito.verify(addStoryViewModelMock).addStory(
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
        return addStoryViewModelMock.addStory(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        ).getOrAwaitValue()
    }

    private fun checkExpectedAddStoryFromFunctionAddStory(expectedAddStory: MutableLiveData<Result<MessageResponse>>) {
        Mockito.`when`(
            addStoryViewModelMock.addStory(
                dummyMultipart,
                dummyDescription,
                dummyToken,
                null,
                null
            )
        ).thenReturn(expectedAddStory)
    }
}