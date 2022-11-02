package com.rs.storyapp.viewmodels

import androidx.lifecycle.ViewModel
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.model.request.RequestSignUp

/**
 * Created by Rahmat Sugiarto on 26/09/2022
 */
class SignUpViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun userSignUp(requestRegister: RequestSignUp) = authRepository.userSignUp(requestRegister)
}