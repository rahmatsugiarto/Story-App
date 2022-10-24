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

        assertNotNull(actualToken)
        assertEquals(dummyToken, actualToken)
    }


    @Test
    fun `when addStory Should Not Null and Return LiveData(Result(MessageResponse))`() {
        //init value
        expectedAddStory.value = Result.Loading

        Mockito.`when`(
            addStoryViewModelMock.addStory(
                dummyMultipart,
                dummyDescription,
                dummyToken,
                null,
                null
            )
        ).thenReturn(expectedAddStory)


        val actualAddStory = addStoryViewModelMock.addStory(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        ).getOrAwaitValue()

        when (actualAddStory) {
            is Result.Loading -> {
                expectedAddStory.value = Result.Loading
                assertEquals(expectedAddStory.value, actualAddStory)
            }
            is Result.Success -> {
                expectedAddStory.value = Result.Success(DataDummy.generateDummyMessageResponse())
                assertNotNull(actualAddStory)
                assertEquals(expectedAddStory.value, actualAddStory)
            }

            is Result.Error -> {
                expectedAddStory.value = Result.Error("Error")
                assertNotNull(actualAddStory)
                assertEquals(expectedAddStory.value, actualAddStory)

            }
        }

        Mockito.verify(addStoryViewModelMock).addStory(
            dummyMultipart,
            dummyDescription,
            dummyToken,
            null,
            null
        )
    }
}