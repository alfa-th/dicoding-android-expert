package com.dicoding.picodicloma.moviecatalogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
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
            .load(watchable.photo)
            .into(img_photo)

        img_photo.contentDescription = resources.getString(R.string.img_cd_prestring) + watchable.title
        tv_title.text = watchable.title
        tv_year.text = watchable.year
        tv_director.text = watchable.director
        tv_description.text = watchable.description
    }
}
