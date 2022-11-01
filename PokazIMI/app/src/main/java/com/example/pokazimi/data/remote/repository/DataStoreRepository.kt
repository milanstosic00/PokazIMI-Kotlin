package com.example.pokazimi.data.remote.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val PREFERENCE_NAME = "logged_in"

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val loggedIn = booleanPreferencesKey("loggedIn")
    }

    suspend fun saveToDataStore(loggedIn: Boolean)
    {
        dataStore.edit { preference ->
            preference[PreferencesKeys.loggedIn] = loggedIn
        }
    }

    val readFromDataStore: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map { preference ->
            val loggedIn = preference[PreferencesKeys.loggedIn] ?: false
            loggedIn
        }
}