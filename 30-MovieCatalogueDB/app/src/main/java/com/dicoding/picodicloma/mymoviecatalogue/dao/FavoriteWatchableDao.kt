package com.dicoding.picodicloma.mymoviecatalogue.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable

@Dao
interface FavoriteWatchableDao {
    @Query("SELECT * FROM fav_watchable_table ORDER BY title ASC")
    fun getOrderedFavoriteWatchable(): LiveData<List<Watchable>>

    @Query("SELECT * FROM fav_watchable_table WHERE isMovie = 1 ORDER BY title ASC")
    fun getOrderedFavoriteMovies(): LiveData<List<Watchable>>

    @Query("SELECT * FROM fav_watchable_table WHERE isMovie = 0 ORDER BY title ASC")
    fun getOrderedFavoriteTvShows(): LiveData<List<Watchable>>

    @Query("SELECT COUNT(title) FROM fav_watchable_table WHERE title= :title")
    fun isFavoriteByTitle(title: String): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(watchable: Watchable)

    @Delete
    suspend fun deleteByObject(watchable: Watchable)
    
    @Query("DELETE FROM fav_watchable_table")
    suspend fun deleteAll()
}