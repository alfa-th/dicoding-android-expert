package com.dicoding.picodicloma.mymoviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.repository.FavoriteWatchableRepository
import com.dicoding.picodicloma.mymoviecatalogue.room.FavoriteWatchableRoomDatabase


internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    companion object {
        private val TAG = StackRemoteViewsFactory::class.java.simpleName
    }

    private lateinit var mData: List<Watchable>
    private val mWidgetItems = ArrayList<Bitmap>()
    private val repository: FavoriteWatchableRepository

    init {
        val favoriteWatchableDao =
            FavoriteWatchableRoomDatabase.getDatabase(mContext, null)
                .favoriteWatchableDao()
        repository = FavoriteWatchableRepository(favoriteWatchableDao)
    }

    override fun onDataSetChanged() {
        mWidgetItems.clear()

        val identityToken = Binder.clearCallingIdentity()
        mData = repository.allFavoriteWatchablesNonLiveData()

        Log.d(TAG, "onDataSetChanged: mData : $mData")

        for (watchable in mData) {
            Glide.with(mContext)
                .asBitmap()
                .load(watchable.poster)
                .submit()
                .get().let { image ->
                    if (image != null)
                        mWidgetItems.add(image)
                    else
                        BitmapFactory.decodeResource(
                            mContext.resources,
                            R.drawable.ic_movie_black_80dp
                        ).let { mWidgetItems.add(it) }

                    Log.d(TAG, "onDataSetChanged: image: $image")
                }
        }

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(
            mContext.packageName,
            R.layout.widget_item
        )

        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = Bundle().apply {
            putInt(FavoriteWatchableBannerWidget.EXTRA_ITEM, position)
        }

        val fillInIntent = Intent().apply {
            putExtras(extras)
        }

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)

        return rv
    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mData.size
    }
}