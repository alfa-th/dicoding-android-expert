package com.dicoding.picodicloma.moviecatalogue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter internal constructor(private val context: Context) : BaseAdapter() {

    internal var movies = arrayListOf<Movie>()

    override fun getView(pos: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val movie = getItem(pos) as Movie
        viewHolder.bind(movie)

        return itemView
    }

    public inner class ViewHolder internal constructor(private val view: View) {
        internal fun bind(movie: Movie) {
            with(view) {
                img_photo.setImageResource(movie.photo)
                txt_title.text = movie.title
                txt_year.text = movie.year
                txt_director.text = movie.director
            }

        }
    }

    override fun getItem(i: Int): Any {
        return movies[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return movies.size
    }
}
