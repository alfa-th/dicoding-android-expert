package com.dicoding.picodicloma.favoriteapp.model

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Parcelable
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Watchable(
     var poster: String? = null,
     var title: String,
     var popularity: Double? = null,
     var overview: String? = null,
     var release: String? = null,
     var isMovie: Boolean = false
) : Parcelable {
    companion object {
        const val TABLE_NAME = "fav_watchable_table"
        const val COLUMN_POSTER = "poster"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POPULARITY = "popularity"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_RELEASE = "release"
        const val COLUMN_IS_MOVIE = "isMovie"

        private const val AUTHORITY = "com.dicoding.picodicloma.mymoviecatalogue"

        val URI_WATCHABLE = Uri.parse(
            "content://$AUTHORITY/$TABLE_NAME"
        )

        fun fromContentValues(values: ContentValues?): Watchable {
            lateinit var watchable: Watchable
            values?.let {
                if (it.containsKey(COLUMN_TITLE))
                    watchable = Watchable(title = it.getAsString(COLUMN_TITLE))
            }

            return watchable
        }

        fun cursorToWatchable(cursor: Cursor): Watchable {
            val posterIndex = cursor.getColumnIndexOrThrow(COLUMN_POSTER)
            val titleIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
            val popularityIndex = cursor.getColumnIndexOrThrow(COLUMN_POPULARITY)
            val overviewIndex = cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW)
            val releaseIndex = cursor.getColumnIndexOrThrow(COLUMN_RELEASE)
            val isMovieIndex = cursor.getColumnIndexOrThrow(COLUMN_IS_MOVIE)

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
}