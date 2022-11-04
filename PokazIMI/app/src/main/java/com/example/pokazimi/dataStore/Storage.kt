package com.example.pokazimi.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Storage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore("Storage")
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    val getToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_TOKEN] ?: ""
        }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }
}