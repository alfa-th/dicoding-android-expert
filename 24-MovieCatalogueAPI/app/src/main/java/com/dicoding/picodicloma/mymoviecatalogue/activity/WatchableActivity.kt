package com.dicoding.picodicloma.mymoviecatalogue.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import kotlinx.android.synthetic.main.activity_watchable.*

class WatchableActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WATCHABLE = "extra_watchable"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchable)

        val watchable = intent.getParcelableExtra(EXTRA_WATCHABLE) as Watchable

        Glide.with(this)
            .load(watchable.poster)
            .apply(RequestOptions().override(100, 150))
            .into(img_photo)

        img_photo.contentDescription = resources.getString(R.string.img_cd_prestring) + watchable.title
        tv_title.text = watchable.title
        tv_popularity.text = watchable.popularity.toString()
        tv_release.text = watchable.release
        tv_overview.text = watchable.overview
    }
}
