package com.rs.storyapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class ListStoryViewModel(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository,
) : ViewModel() {

    fun getStories(token: String): LiveData<PagingData<StoryEntity>> =
        storyRepository.getStories(token).cachedIn(viewModelScope)

    fun resetUserData() = viewModelScope.launch {
        authRepository.deleteUser()
    }
}