package com.example.pokazimi.data.remote.viewModel

import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.example.pokazimi.data.remote.repository.DataStoreRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataStoreRepository): ViewModel() {

    val readFromDataStore = repository.readFromDataStore.asLiveData()

    fun saveToDataStore(loggedIn: Boolean){
        viewModelScope.launch { repository.saveToDataStore(loggedIn) }
    }
}

val UserState = compositionLocalOf<MainViewModel> { error("User State Context Not Found!") }


class MainViewModelFactory(
    private val repository: DataStoreRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
