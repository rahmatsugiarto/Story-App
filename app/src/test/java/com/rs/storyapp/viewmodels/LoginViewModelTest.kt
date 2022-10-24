package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.model.response.LoginResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var loginViewModelMock: LoginViewModel

    private lateinit var loginViewModel: LoginViewModel

    private val dummyToken = "token"
    private val dummyRequestLogin = DataDummy.generateDummyRequestLogin()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val expectedUserLogin = MutableLiveData<Result<LoginResponse>>()


    @Before
    fun setup() {
        loginViewModel = LoginViewModel(authRepository)
    }

    @Test
    fun `when saveToken successfully`() = runTest {
        loginViewModel.saveToken(dummyToken)
        Mockito.verify(authRepository).saveToken(dummyToken)
    }

    @Test
    fun `when userLogin Should Not Null and Return LiveData(Result(LoginResponse))`() {
        //init value
        expectedUserLogin.value = Result.Loading

        Mockito.`when`(
            loginViewModelMock.userLogin(dummyRequestLogin)
        ).thenReturn(expectedUserLogin)


        when (val actualUserLogin = loginViewModelMock.userLogin(dummyRequestLogin).getOrAwaitValue()) {
            is Result.Loading -> {
                expectedUserLogin.value = Result.Loading
                Assert.assertEquals(expectedUserLogin.value, actualUserLogin)
            }
            is Result.Success -> {
                expectedUserLogin.value = Result.Success(dummyLoginResponse)
                Assert.assertNotNull(actualUserLogin)
                Assert.assertEquals(expectedUserLogin.value, actualUserLogin)
            }

            is Result.Error -> {
                expectedUserLogin.value = Result.Error("Error")
                Assert.assertNotNull(actualUserLogin)
                Assert.assertEquals(expectedUserLogin.value, actualUserLogin)

            }
        }
        Mockito.verify(loginViewModelMock).userLogin(dummyRequestLogin)
    }
}