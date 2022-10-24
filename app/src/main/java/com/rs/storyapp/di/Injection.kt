package com.rs.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.rs.storyapp.data.local.database.StoryDatabase
import com.rs.storyapp.data.local.preference.DataUserPreference
import com.rs.storyapp.data.remote.ApiConfig
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.data.repository.StoryRepository

/**
 * Created by Rahmat Sugiarto on 10/10/2022
 */
object Injection {

    fun provideAuthRepository(dataStore: DataStore<Preferences>): AuthRepository {
        val apiService = ApiConfig.getApiService()
        val pref = DataUserPreference.getInstance(dataStore)
        return AuthRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService,database)
    }

}