package com.rs.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.data.Result
import com.rs.storyapp.data.local.preference.DataUserPreference
import com.rs.storyapp.data.remote.ApiService
import com.rs.storyapp.model.response.LoginResponse
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.FakeApiService
import com.rs.storyapp.utils.MainDispatcherRule
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
    private lateinit var apiServiceMock: ApiService

    private lateinit var authRepository: AuthRepository


    private val dummyToken = "token"
    private val dummyRequestLogin = DataDummy.generateDummyRequestLogin()
    private val dummyRequestSignUp = DataDummy.generateDummyRequestSignUp()

    @Before
    fun setup() {
        authRepository = AuthRepository(apiServiceMock, preferencesDataSource)
        apiService = FakeApiService()
    }

    @Test
    fun `when userLogin Should Not Null`() = runTest {
        val expectedStory = DataDummy.generateDummyLoginResponse()
        val actualLogin = apiService.userLogin(dummyRequestLogin)

        Assert.assertNotNull(actualLogin)
        Assert.assertEquals(expectedStory.message, actualLogin.message)
    }

    @Test
    fun `when userSignUp Should Not Null`() = runTest {
        val expectedStory = DataDummy.generateDummyLoginResponse()
        val actualLogin = apiService.userSignUp(dummyRequestSignUp)

        Assert.assertNotNull(actualLogin)
        Assert.assertEquals(expectedStory.message, actualLogin.message)
    }

    @Test
    fun `when saveToken successfully`() = runTest {
        authRepository.saveToken(dummyToken)
        verify(preferencesDataSource).saveToken(dummyToken)
    }

    @Test
    fun `when getToken successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)

        `when`(preferencesDataSource.getToken()).thenReturn(expectedToken)

        authRepository.getToken().collect { actualToken ->
            Assert.assertNotNull(actualToken)
            Assert.assertEquals(dummyToken, actualToken)
        }

        verify(preferencesDataSource).getToken()
    }
}