package com.rs.storyapp.data.local
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences

class DataUserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getIsLogin(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[IS_LOGIN] ?: false
        }
    }

    suspend fun saveIsLogin(isLogin: Boolean) {
        dataStore.edit { pref ->
            pref[IS_LOGIN] = isLogin
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[USER_TOKEN] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { pref ->
            pref[USER_TOKEN] = token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataUserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataUserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = DataUserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val IS_LOGIN = booleanPreferencesKey("is_login")
        private val USER_TOKEN = stringPreferencesKey("user_token")

    }
}