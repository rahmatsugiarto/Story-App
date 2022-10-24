package com.rs.storyapp.viewmodels

import androidx.lifecycle.ViewModel
import com.rs.storyapp.data.repository.StoryRepository

/**
 * Created by Rahmat Sugiarto on 17/10/2022
 */
class MapsStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation(token: String) = storyRepository.getStoriesWithLocation(token)
}