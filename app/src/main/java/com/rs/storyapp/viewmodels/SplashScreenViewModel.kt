package com.rs.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.rs.storyapp.data.local.DataUserPreference

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class SplashScreenViewModel(private val pref: DataUserPreference): ViewModel() {

    fun getIsLogin(): LiveData<Boolean> {
        return pref.getIsLogin().asLiveData()
    }
}

class SplashScreenViewModelFactory(private val pref: DataUserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            return SplashScreenViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}