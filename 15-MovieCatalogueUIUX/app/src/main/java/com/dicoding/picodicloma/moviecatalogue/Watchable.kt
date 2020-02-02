package com.dicoding.picodicloma.moviecatalogue

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Watchable(
    var photo: Int,
    var title: String?,
    var year: String?,
    var director: String?,
    var description: String?
) : Parcelable