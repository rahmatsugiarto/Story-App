package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashScreenViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var splashScreenViewModel: SplashScreenViewModel

    private val dummyToken = "token"
    private val expectedGetToken = MutableLiveData<String>()

    @Test
    fun `when getToken successfully`() {
        expectedGetToken.value = dummyToken

        Mockito.`when`(splashScreenViewModel.getToken()).thenReturn(expectedGetToken)
        val actualToken = splashScreenViewModel.getToken().getOrAwaitValue()

        Mockito.verify(splashScreenViewModel).getToken()
        assertNotNull(actualToken)
        assertEquals(dummyToken, actualToken)
    }
}