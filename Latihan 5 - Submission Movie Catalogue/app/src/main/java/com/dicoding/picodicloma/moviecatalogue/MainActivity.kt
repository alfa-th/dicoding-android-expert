package com.dicoding.picodicloma.moviecatalogue

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var dataPhoto: TypedArray
    private lateinit var dataTitle: Array<String>
    private lateinit var dataYear: Array<String>
    private lateinit var dataDirector: Array<String>
    private lateinit var dataDescription: Array<String>

    private var movies = arrayListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MovieAdapter(this)
        lv_list.adapter = adapter

        prepare()
        addItem()

        lv_list.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            val movieClicked = movies[position]

            val moveWithObjectIntent = Intent(this@MainActivity, MovieActivity::class.java)
            moveWithObjectIntent.putExtra(MovieActivity.EXTRA_MOVIE, movieClicked)

            Toast.makeText(this@MainActivity, movies[position].title, Toast.LENGTH_SHORT).show()

            startActivity(moveWithObjectIntent)
        }
    }

    private fun prepare() {
        dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        dataTitle = resources.getStringArray(R.array.data_title)
        dataYear = resources.getStringArray(R.array.data_year)
        dataDirector = resources.getStringArray(R.array.data_director)
        dataDescription = resources.getStringArray(R.array.data_description)
    }

    private fun addItem() {
        for (position in dataTitle.indices) {
            val movie = Movie(
                dataPhoto.getResourceId(position, -1),
                dataTitle[position],
                "(" + dataYear[position] + ")",
                dataDirector[position],
                dataDescription[position]
            )

            movies.add(movie)
        }
        adapter.movies = movies
    }
}
