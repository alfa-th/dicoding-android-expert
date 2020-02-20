package com.dicoding.picodicloma.mymoviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Watchable(
    var poster: String? = null,
    var title: String? = null,
    var popularity: Double? = null,
    var overview: String? = null,
    var release: String? = null
) : Parcelable