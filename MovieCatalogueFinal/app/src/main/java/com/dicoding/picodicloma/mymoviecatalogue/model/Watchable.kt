package com.dicoding.picodicloma.mymoviecatalogue.model

import android.content.ContentValues
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = Watchable.TABLE_NAME)
@Parcelize
data class Watchable(
    @ColumnInfo(name = "poster") var poster: String? = null,
    @PrimaryKey
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "popularity") var popularity: Double? = null,
    @ColumnInfo(name = "overview") var overview: String? = null,
    @ColumnInfo(name = "release") var release: String? = null,
    @ColumnInfo(name = "isMovie") var isMovie: Boolean = false
) : Parcelable {
    companion object {
        const val TABLE_NAME = "fav_watchable_table"
        const val COLUMN_POSTER = "poster"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POPULARITY = "popularity"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_RELEASE = "release"
        const val COLUMN_IS_MOVIE = "isMovie"

        fun fromContentValues(values: ContentValues?): Watchable {
            lateinit var watchable: Watchable
            values?.let {
                if (it.containsKey(COLUMN_TITLE))
                    watchable = Watchable(title = it.getAsString(COLUMN_TITLE))
            }

            return watchable
        }
    }
}