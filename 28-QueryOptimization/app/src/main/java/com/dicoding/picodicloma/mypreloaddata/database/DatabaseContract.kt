package com.dicoding.picodicloma.mypreloaddata.database

import android.net.Uri
import android.provider.BaseColumns

internal object DatabaseContract {

    const val AUTHORITHY = "com.dicoding.picodicloma.mynotesapp"
    const val SCHEME = "content"
    var TABLE_NAME = "table_mahasiswa"

    internal class MahasiswaColumsn : BaseColumns {
        companion object {
            const val _ID = "_id"
            const val NAMA = "nama"
            const val NIM = "nim"
        }

        val CONTENT_URI = Uri
            .Builder()
            .scheme(SCHEME)
            .authority(AUTHORITHY)
            .appendPath(TABLE_NAME)
            .build()
    }
}