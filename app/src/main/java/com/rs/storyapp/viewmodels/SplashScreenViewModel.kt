package com.rs.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rs.storyapp.data.repository.AuthRepository

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class SplashScreenViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun getToken(): LiveData<String> {
        return authRepository.getToken().asLiveData()
    }
}