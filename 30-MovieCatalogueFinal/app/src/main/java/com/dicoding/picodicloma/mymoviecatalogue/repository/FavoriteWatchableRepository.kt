package com.dicoding.picodicloma.mymoviecatalogue.repository

import android.appwidget.AppWidgetManager
import androidx.lifecycle.LiveData
import com.dicoding.picodicloma.mymoviecatalogue.dao.FavoriteWatchableDao
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.viewmodel.FavoriteWatchableViewModel
import com.dicoding.picodicloma.mymoviecatalogue.widget.FavoriteWatchableBannerWidget
import com.dicoding.picodicloma.mymoviecatalogue.widget.StackRemoteViewsFactory

class FavoriteWatchableRepository(private val favoriteWatchableDao: FavoriteWatchableDao) {

    val allFavoriteWatchables: LiveData<List<Watchable>> =
        favoriteWatchableDao.getOrderedFavoriteWatchable()


    val allFavoriteMovies: LiveData<List<Watchable>> =
        favoriteWatchableDao.getOrderedFavoriteMovies()

    val allFavoriteTvShows : LiveData<List<Watchable>> =
        favoriteWatchableDao.getOrderedFavoriteTvShows()

    suspend fun insert(watchable: Watchable) {
        favoriteWatchableDao.insert(watchable)
    }

    suspend fun delete(watchable: Watchable) {
        favoriteWatchableDao.deleteByObject(watchable)
    }

    fun isFavoriteByTitle(title: String): LiveData<Boolean> {
        return favoriteWatchableDao.isFavoriteByTitle(title)
    }

    fun allFavoriteWatchablesNonLiveData() : List<Watchable> =
        favoriteWatchableDao.getOrderedFavoriteWatchableNonLiveData()
}