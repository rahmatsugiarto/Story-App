package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.model.response.MessageResponse
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
class SignUpViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var signUpViewModel: SignUpViewModel

    private val dummyRequestSignUp = DataDummy.generateDummyRequestSignUp()
    private val expectedSignUp = MutableLiveData<Result<MessageResponse>>()

    @Before
    fun setup() {
        signUpViewModel = SignUpViewModel(authRepository)
    }

    @Test
    fun `when userSignUp Should Not Null and Return Result(Success)`() {
        val dummySignUpResponse = DataDummy.generateDummyMessageResponse()
        expectedSignUp.value = Result.Success(dummySignUpResponse)

        Mockito.`when`(authRepository.userSignUp(dummyRequestSignUp)).thenReturn(expectedSignUp)

        val actualSignUp = signUpViewModel.userSignUp(dummyRequestSignUp).getOrAwaitValue()

        Mockito.verify(authRepository).userSignUp(dummyRequestSignUp)
        assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Success)
        assertEquals(
            dummySignUpResponse.message,
            (actualSignUp as Result.Success).data.message
        )
    }

    @Test
    fun `when userSignUp Should Not Null and Return Result(Loading)`() {
        expectedSignUp.value = Result.Loading
        Mockito.`when`(authRepository.userSignUp(dummyRequestSignUp)).thenReturn(expectedSignUp)

        val actualSignUp = signUpViewModel.userSignUp(dummyRequestSignUp).getOrAwaitValue()

        Mockito.verify(authRepository).userSignUp(dummyRequestSignUp)
        assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Loading)
    }

    @Test
    fun `when userSignUp Should Not Null and Return Result(Error)`() {
        expectedSignUp.value = Result.Error("throw exception")
        Mockito.`when`(authRepository.userSignUp(dummyRequestSignUp)).thenReturn(expectedSignUp)

        val actualSignUp = signUpViewModel.userSignUp(dummyRequestSignUp).getOrAwaitValue()

        Mockito.verify(authRepository).userSignUp(dummyRequestSignUp)
        assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Error)
    }

}