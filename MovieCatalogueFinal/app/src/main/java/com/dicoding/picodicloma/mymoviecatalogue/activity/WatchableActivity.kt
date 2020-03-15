package com.dicoding.picodicloma.mymoviecatalogue.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import kotlinx.android.synthetic.main.activity_watchable.*

class WatchableActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WATCHABLE = "extra_watchable"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_WATCHABLE_REPLY = "extra_watchable_reply`"
        const val EXTRA_TYPE_REPLY = "extra_type_reply"
        const val EXTRA_CHANGED_REPLY = "extra_favorite_changed"

        private val TAG = WatchableActivity::class.java.simpleName
    }

    private lateinit var watchable: Watchable
    private var isFavorite: Boolean = false
    private var initialFavorite: Boolean = false
    private var favoriteMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchable)

        watchable = intent.getParcelableExtra(EXTRA_WATCHABLE) as Watchable
        initialFavorite = intent.getBooleanExtra(EXTRA_TYPE, false)
        isFavorite = initialFavorite

        Glide.with(this)
            .load(watchable.poster)
            .apply(RequestOptions().override(100, 150))
            .into(img_photo)

        img_photo.contentDescription =
            resources.getString(R.string.img_cd_prestring) + watchable.title
        tv_title.text = watchable.title
        tv_popularity.text = watchable.popularity.toString()
        tv_release.text = watchable.release
        tv_overview.text = watchable.overview
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_watchable, menu)

        favoriteMenuItem = menu?.findItem(R.id.action_favorite)
        favoriteMenuItem?.isChecked = isFavorite

        if(isFavorite)
            favoriteMenuItem?.setIcon(R.drawable.ic_favorite_white_24dp)
        else
            favoriteMenuItem?.setIcon(R.drawable.ic_unfavorite_white_24dp)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorite -> {
                item.isChecked = !item.isChecked
                isFavorite = item.isChecked

                if(isFavorite)
                    favoriteMenuItem?.setIcon(R.drawable.ic_favorite_white_24dp)
                else
                    favoriteMenuItem?.setIcon(R.drawable.ic_unfavorite_white_24dp)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        val isFavoriteStateChanged: Boolean = initialFavorite != isFavorite
        val replyIntent = Intent()

        Log.d(TAG, "initialFavorite State : $initialFavorite")
        Log.d(TAG, "isFavorite State : $isFavorite")
        Log.d(TAG, "isFavorite State Changed: $isFavoriteStateChanged")

        replyIntent.putExtra(EXTRA_WATCHABLE_REPLY, watchable)
        replyIntent.putExtra(EXTRA_TYPE_REPLY, isFavorite)
        replyIntent.putExtra(EXTRA_CHANGED_REPLY, isFavoriteStateChanged)

        setResult(RESULT_OK, replyIntent)

        super.onBackPressed()
    }

    private fun toastShowText(str: String){
        Toast.makeText(
            this,
            str,
            Toast.LENGTH_SHORT
        )
            .show()
    }
}
