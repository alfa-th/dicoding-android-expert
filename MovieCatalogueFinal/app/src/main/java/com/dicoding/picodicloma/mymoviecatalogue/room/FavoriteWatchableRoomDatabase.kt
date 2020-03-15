package com.dicoding.picodicloma.mymoviecatalogue.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.picodicloma.mymoviecatalogue.dao.FavoriteWatchableDao
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(Watchable::class), version = 1, exportSchema = false)
abstract class FavoriteWatchableRoomDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: FavoriteWatchableRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope?
        ): FavoriteWatchableRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteWatchableRoomDatabase::class.java,
                    "fav_watchable_database"
                )
                    .build()

                INSTANCE = instance
                
                return instance
            }
        }
    }
    
    abstract fun favoriteWatchableDao(): FavoriteWatchableDao
}