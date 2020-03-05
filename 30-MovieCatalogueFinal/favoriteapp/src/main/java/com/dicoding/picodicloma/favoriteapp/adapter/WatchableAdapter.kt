package com.dicoding.picodicloma.favoriteapp.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodicloma.favoriteapp.R
import com.dicoding.picodicloma.favoriteapp.model.Watchable
import kotlinx.android.synthetic.main.item_watchable.view.*

class WatchableAdapter(private val activity: Activity) :
    RecyclerView.Adapter<WatchableAdapter.WatchableViewHolder>() {

    companion object {
        private val TAG = WatchableAdapter::class.java.simpleName
    }

    var listWatchables = ArrayList<Watchable>()
        set(listWatchables) {
            this.listWatchables.clear()
            this.listWatchables.addAll(listWatchables)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchableViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_watchable, parent, false)
        return WatchableViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchableViewHolder, position: Int) {
        holder.bind(listWatchables[position])
    }

    override fun getItemCount(): Int {
        return this.listWatchables.size
    }

    inner class WatchableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(watchable: Watchable) {
            Log.d(TAG, "WatchableViewHolder: bind: $watchable")

            with(itemView) {
                Glide.with(itemView.context)
                    .load(watchable.poster)
                    .apply(RequestOptions().override(100, 150))
                    .into(img_photo)

                tv_title.text = watchable.title
                tv_popularity.text = watchable.popularity.toString()
                tv_release.text = watchable.release

            }
        }
    }
}
