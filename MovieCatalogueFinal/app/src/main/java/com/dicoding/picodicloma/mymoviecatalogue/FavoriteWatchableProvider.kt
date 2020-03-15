package com.dicoding.picodicloma.mymoviecatalogue

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.dicoding.picodicloma.mymoviecatalogue.dao.FavoriteWatchableDao
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.room.FavoriteWatchableRoomDatabase


class FavoriteWatchableProvider : ContentProvider() {

    companion object {
        private val TAG = FavoriteWatchableProvider::class.java.simpleName
        private const val AUTHORITY = "com.dicoding.picodicloma.mymoviecatalogue"

        val URI_WATCHABLE = Uri.parse(
            "content://" + AUTHORITY + "/" + Watchable.TABLE_NAME
        )

        const val CODE_WATCHABLE_DIR = 1
        const val CODE_WATCHABLE_ITEM = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        // content://com.dicoding.picodicloma.mymoviecatalogue/fav_watchable_table
        MATCHER.addURI(AUTHORITY, Watchable.TABLE_NAME, CODE_WATCHABLE_DIR)

        // content://com.dicoding.picodicloma.mymoviecatalogue/favorites/string
        MATCHER.addURI(AUTHORITY, Watchable.TABLE_NAME + "/*", CODE_WATCHABLE_ITEM)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String?>?, selection: String?,
        selectionArgs: Array<String?>?, sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        return if (code == CODE_WATCHABLE_DIR || code == CODE_WATCHABLE_ITEM) {
            val context = context ?: return null
            val watchableDao: FavoriteWatchableDao =
                FavoriteWatchableRoomDatabase
                    .getDatabase(context, null)
                    .favoriteWatchableDao()
            val cursor: Cursor = if (code == CODE_WATCHABLE_DIR) {
                watchableDao.selectAll()
            } else {
                watchableDao.selectByTitle(ContentUris.parseId(uri).toString())
            }
            cursor.setNotificationUri(context.contentResolver, uri)

            cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (MATCHER.match(uri)) {
            CODE_WATCHABLE_DIR -> "vnd.android.cursor.dir/" + AUTHORITY + "." + Watchable.TABLE_NAME
            CODE_WATCHABLE_ITEM -> "vnd.android.cursor.item/" + AUTHORITY + "." + Watchable.TABLE_NAME
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (MATCHER.match(uri)) {
            CODE_WATCHABLE_DIR -> {
                val context = context ?: return null
                val newObjectId: Long =
                    FavoriteWatchableRoomDatabase
                        .getDatabase(context, null)
                        .favoriteWatchableDao()
                        .insertForResult(Watchable.fromContentValues(values))

                context.contentResolver.notifyChange(uri, null)

                ContentUris.withAppendedId(uri, newObjectId)
            }
            CODE_WATCHABLE_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with title: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return when (MATCHER.match(uri)) {
            CODE_WATCHABLE_DIR -> throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            CODE_WATCHABLE_ITEM -> {
                val context = context ?: return 0
                val watchable: Watchable = Watchable.fromContentValues(values)
                watchable.title = ContentUris.parseId(uri).toString()
                val count: Int =
                    FavoriteWatchableRoomDatabase
                        .getDatabase(context, null)
                        .favoriteWatchableDao()
                        .update(watchable)
                context.contentResolver.notifyChange(uri, null)
                count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (MATCHER.match(uri)) {
            CODE_WATCHABLE_DIR -> throw java.lang.IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            CODE_WATCHABLE_ITEM -> {
                val context = context ?: return 0
                val count: Int =FavoriteWatchableRoomDatabase
                    .getDatabase(context, null)
                    .favoriteWatchableDao()
                    .deleteByTitle(ContentUris.parseId(uri).toString())
                context.contentResolver.notifyChange(uri, null)
                count
            }
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }
}
