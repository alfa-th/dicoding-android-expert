package com.dicoding.picodicloma.mymoviecatalogue.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "fav_watchable_table")
@Parcelize
data class Watchable(
    @ColumnInfo(name = "poster") var poster: String? = null,
    @PrimaryKey
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "popularity") var popularity: Double? = null,
    @ColumnInfo(name = "overview") var overview: String? = null,
    @ColumnInfo(name = "release") var release: String? = null,
    @ColumnInfo(name = "isMovie") var isMovie: Boolean = false
) : Parcelable