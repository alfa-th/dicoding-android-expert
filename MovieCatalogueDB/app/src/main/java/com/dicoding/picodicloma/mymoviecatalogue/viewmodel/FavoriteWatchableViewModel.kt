package com.dicoding.picodicloma.mymoviecatalogue.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.repository.FavoriteWatchableRepository
import com.dicoding.picodicloma.mymoviecatalogue.room.FavoriteWatchableRoomDatabase
import kotlinx.coroutines.launch


class FavoriteWatchableViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        val TAG = this::class.java.simpleName

        const val TYPE_MOVIE = 0
        const val TYPE_TV = 1
    }

    val repository: FavoriteWatchableRepository
    var viewModelType: Int = 0
    val allFavoriteWatchables: LiveData<List<Watchable>>

    init {
        val favoriteWatchableDao =
            FavoriteWatchableRoomDatabase.getDatabase(application, viewModelScope)
                .favoriteWatchableDao()
        repository = FavoriteWatchableRepository(favoriteWatchableDao)
        allFavoriteWatchables = repository.allFavoriteWatchables
    }

    fun setType(watchableType: Int) {
        viewModelType = watchableType
    }

    fun insert(watchable: Watchable) = viewModelScope.launch {
        repository.insert(watchable)
    }

    fun delete(watchable: Watchable) = viewModelScope.launch {
        repository.delete(watchable)

    }

    fun isFavoriteByTitle(title: String): LiveData<Boolean> {
        return repository.isFavoriteByTitle(title)
    }

}
