package com.rs.storyapp.viewmodels

import androidx.lifecycle.ViewModel
import com.rs.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class SplashScreenViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun getToken(): Flow<String?> = authRepository.getToken()
}