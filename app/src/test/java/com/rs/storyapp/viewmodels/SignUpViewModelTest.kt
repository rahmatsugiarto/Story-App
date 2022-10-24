package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.data.Result
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SignUpViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var signUpViewModel: SignUpViewModel

    private val dummyRequestSignUp = DataDummy.generateDummyRequestSignUp()
    private val dummyMessageResponse = DataDummy.generateDummyMessageResponse()
    private val expectedStoriesWithLocation = MutableLiveData<Result<MessageResponse>>()


    @Test
    fun `when userSignUp Should Not Null and Return LiveData(Result(RequestSignUp))`() {
        //init value
        expectedStoriesWithLocation.value = Result.Loading

        Mockito.`when`(
            signUpViewModel.userSignUp(dummyRequestSignUp)
        ).thenReturn(expectedStoriesWithLocation)


        when (val actualUserSignUp = signUpViewModel.userSignUp(dummyRequestSignUp).getOrAwaitValue()) {
            is Result.Loading -> {
                expectedStoriesWithLocation.value = Result.Loading
                assertEquals(expectedStoriesWithLocation.value, actualUserSignUp)
            }
            is Result.Success -> {
                expectedStoriesWithLocation.value = Result.Success(dummyMessageResponse)
                assertNotNull(actualUserSignUp)
                assertEquals(expectedStoriesWithLocation.value, actualUserSignUp)
            }

            is Result.Error -> {
                expectedStoriesWithLocation.value = Result.Error("Error")
                assertNotNull(actualUserSignUp)
                assertEquals(expectedStoriesWithLocation.value, actualUserSignUp)

            }
        }
        Mockito.verify(signUpViewModel).userSignUp(dummyRequestSignUp)
    }

}