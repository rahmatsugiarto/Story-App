package com.rs.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.local.preference.DataUserPreference
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.response.LoginResponse
import com.rs.storyapp.model.response.MessageResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.FakeApiService
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Mock
    private lateinit var preferencesDataSource: DataUserPreference

    private lateinit var apiService: ApiService

    @Mock
    private lateinit var authRepository: AuthRepository


    private val dummyToken = "token"
    private val dummyRequestLogin = DataDummy.generateDummyRequestLogin()
    private val dummyRequestSignUp = DataDummy.generateDummyRequestSignUp()
    private val expectedLogin = MutableLiveData<Result<LoginResponse>>()
    private val expectedSignUp = MutableLiveData<Result<MessageResponse>>()

    @Before
    fun setup() {
        apiService = FakeApiService()
    }

    @Test
    fun `when userLogin Should Not Null and Return Result(Success)`() {
        val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
        expectedLogin.value = Result.Success(dummyLoginResponse)

        `when`(authRepository.userLogin(dummyRequestLogin)).thenReturn(expectedLogin)

        val actualLogin = authRepository.userLogin(dummyRequestLogin).getOrAwaitValue()

        verify(authRepository).userLogin(dummyRequestLogin)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Success)
        Assert.assertEquals(
            dummyLoginResponse.message,
            (actualLogin as Result.Success).data.message
        )
    }

    @Test
    fun `when userLogin Should Not Null and Return Result(Loading)`() {
        expectedLogin.value = Result.Loading

        `when`(authRepository.userLogin(dummyRequestLogin)).thenReturn(expectedLogin)

        val actualLogin = authRepository.userLogin(dummyRequestLogin).getOrAwaitValue()

        verify(authRepository).userLogin(dummyRequestLogin)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Loading)
    }



    @Test
    fun `when userLogin when userLogin Should Not Null and Return Result(Error)`() {
        expectedLogin.value = Result.Error("throw exception")

        `when`(authRepository.userLogin(dummyRequestLogin)).thenReturn(expectedLogin)

        val actualLogin = authRepository.userLogin(dummyRequestLogin).getOrAwaitValue()

        verify(authRepository).userLogin(dummyRequestLogin)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Error)
    }


    @Test
    fun `when userSignUp Should Not Null and Return Result(Success)`() {
        val dummySignUpResponse = DataDummy.generateDummyMessageResponse()
        expectedSignUp.value = Result.Success(dummySignUpResponse)

        `when`(authRepository.userSignUp(dummyRequestSignUp)).thenReturn(expectedSignUp)

        val actualSignUp = authRepository.userSignUp(dummyRequestSignUp).getOrAwaitValue()

        verify(authRepository).userSignUp(dummyRequestSignUp)
        Assert.assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Success)
        Assert.assertEquals(
            dummySignUpResponse.message,
            (actualSignUp as Result.Success).data.message
        )
    }

    @Test
    fun `when userSignUp Should Not Null and Return Result(Loading)`() {
        expectedSignUp.value = Result.Loading

        `when`(authRepository.userSignUp(dummyRequestSignUp)).thenReturn(expectedSignUp)

        val actualSignUp = authRepository.userSignUp(dummyRequestSignUp).getOrAwaitValue()

        verify(authRepository).userSignUp(dummyRequestSignUp)
        Assert.assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Loading)
    }

    @Test
    fun `when userSignUp Should Not Null and Return Result(Error)`() {
        expectedSignUp.value = Result.Error("throw exception")

        `when`(authRepository.userSignUp(dummyRequestSignUp)).thenReturn(expectedSignUp)

        val actualSignUp = authRepository.userSignUp(dummyRequestSignUp).getOrAwaitValue()

        verify(authRepository).userSignUp(dummyRequestSignUp)
        Assert.assertNotNull(actualSignUp)
        Assert.assertTrue(actualSignUp is Result.Error)
    }

    @Test
    fun `when saveToken successfully`() = runTest {
        authRepository = AuthRepository(apiService, preferencesDataSource)
        authRepository.saveToken(dummyToken)
        verify(preferencesDataSource).saveToken(dummyToken)
    }

    @Test
    fun `when getToken successfully`() = runTest {
        authRepository = AuthRepository(apiService, preferencesDataSource)
        val expectedToken = flowOf(dummyToken)

        `when`(preferencesDataSource.getToken()).thenReturn(expectedToken)

        authRepository.getToken().collect { actualToken ->
            Assert.assertNotNull(actualToken)
            Assert.assertEquals(dummyToken, actualToken)
        }

        verify(preferencesDataSource).getToken()
    }
}