package com.dicoding.picodicloma.mymoviecatalogue.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable

@Dao
interface FavoriteWatchableDao {
    @Query("SELECT * FROM fav_watchable_table ORDER BY title ASC")
    fun getOrderedFavoriteWatchable(): LiveData<List<Watchable>>

    @Query("SELECT * FROM fav_watchable_table ORDER BY title ASC")
    fun getOrderedFavoriteWatchableNonLiveData(): List<Watchable>

    @Query("SELECT * FROM fav_watchable_table WHERE isMovie = 1 ORDER BY title ASC")
    fun getOrderedFavoriteMovies(): LiveData<List<Watchable>>

    @Query("SELECT * FROM fav_watchable_table WHERE isMovie = 0 ORDER BY title ASC")
    fun getOrderedFavoriteTvShows(): LiveData<List<Watchable>>

    @Query("SELECT COUNT(title) FROM fav_watchable_table WHERE title= :title")
    fun isFavoriteByTitle(title: String): LiveData<Boolean>

    @Query("SELECT * FROM ${Watchable.TABLE_NAME}")
    fun selectAll(): Cursor

    @Query("SELECT * FROM ${Watchable.TABLE_NAME} WHERE ${Watchable.COLUMN_TITLE}= :title")
    fun selectByTitle(title: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForResult(watchable: Watchable): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(watchable: Watchable)

    @Delete
    suspend fun deleteByObject(watchable: Watchable)

    @Query("DELETE FROM ${Watchable.TABLE_NAME} WHERE ${Watchable.COLUMN_TITLE}= :title")
    fun deleteByTitle(title: String): Int

    @Query("DELETE FROM fav_watchable_table")
    suspend fun deleteAll()

    @Update
    fun update(watchable: Watchable): Int
}