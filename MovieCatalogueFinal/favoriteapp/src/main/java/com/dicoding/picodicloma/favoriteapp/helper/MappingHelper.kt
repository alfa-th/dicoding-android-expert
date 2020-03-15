package com.dicoding.picodicloma.favoriteapp.helper

import android.database.Cursor
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.dicoding.picodicloma.favoriteapp.model.Watchable

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Watchable> {
        val watchableList = ArrayList<Watchable>()

        cursor?.apply {
            while (moveToNext()) {
                val posterIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_POSTER)
                val titleIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_TITLE)
                val popularityIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_POPULARITY)
                val overviewIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_OVERVIEW)
                val releaseIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_RELEASE)
                val isMovieIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_IS_MOVIE)

                val poster = cursor.getStringOrNull(posterIndex)
                val title = cursor.getString(titleIndex)
                val popularity = cursor.getDoubleOrNull(popularityIndex)
                val overview = cursor.getStringOrNull(overviewIndex)
                val release = cursor.getStringOrNull(releaseIndex)
                val isMovie = cursor.getIntOrNull(isMovieIndex)!! > 0

                watchableList.add(
                    Watchable(
                        poster,
                        title,
                        popularity,
                        overview,
                        release,
                        isMovie
                    )
                )
            }
        }
        return watchableList
    }

    fun cursorToWatchable(cursor: Cursor): Watchable {
        val posterIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_POSTER)
        val titleIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_TITLE)
        val popularityIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_POPULARITY)
        val overviewIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_OVERVIEW)
        val releaseIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_RELEASE)
        val isMovieIndex = cursor.getColumnIndexOrThrow(Watchable.COLUMN_IS_MOVIE)

        val poster = cursor.getStringOrNull(posterIndex)
        val title = cursor.getString(titleIndex)
        val popularity = cursor.getDoubleOrNull(popularityIndex)
        val overview = cursor.getStringOrNull(overviewIndex)
        val release = cursor.getStringOrNull(releaseIndex)
        val isMovie = cursor.getIntOrNull(isMovieIndex)!! > 0

        return try {
            Watchable(
                poster,
                title,
                popularity,
                overview,
                release,
                isMovie
            )
        } catch (e: Exception) {
            throw e
        }
    }
}