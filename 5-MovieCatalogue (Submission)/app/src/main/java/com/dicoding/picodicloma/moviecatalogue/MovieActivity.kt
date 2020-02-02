package com.dicoding.picodicloma.moviecatalogue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.item_movie.img_photo
import kotlinx.android.synthetic.main.item_movie.txt_director
import kotlinx.android.synthetic.main.item_movie.txt_title
import kotlinx.android.synthetic.main.item_movie.txt_year

class MovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as Movie

        img_photo.setImageResource(movie.photo)
        txt_title.text = movie.title
        txt_year.text = movie.year
        txt_director.text = movie.director
        txt_description.text = movie.description
    }
}
