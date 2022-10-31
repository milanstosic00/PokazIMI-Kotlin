package com.example.pokazimi

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.pokazimi.data.remote.repository.DataStoreRepository
import kotlinx.coroutines.launch

class MainViewModel(private val dataStore: DataStore<Preferences>): ViewModel() {
    private val repository = DataStoreRepository(dataStore)

    val readFromDataStore = repository.readFromDataStore.asLiveData()

    fun saveToDataStore(loggedIn: Boolean)
    {
        viewModelScope.launch { repository.saveToDataStore(loggedIn) }
    }
}