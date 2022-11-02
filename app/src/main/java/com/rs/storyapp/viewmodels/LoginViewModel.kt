package com.rs.storyapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.model.request.RequestLogin
import kotlinx.coroutines.launch

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun userLogin(requestLogin: RequestLogin) = authRepository.userLogin(requestLogin)

    fun saveToken(token: String) = viewModelScope.launch {
        authRepository.saveToken(token)
    }
}
