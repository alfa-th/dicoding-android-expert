package com.dicoding.picodicloma.mymoviecatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import kotlinx.android.synthetic.main.item_watchable.view.*

class WatchableAdapter : RecyclerView.Adapter<WatchableAdapter.WatchableViewHolder>() {

    private val mData = ArrayList<Watchable>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<Watchable>) {
        mData.clear()
        mData.addAll(items)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchableViewHolder {
        val mView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_watchable, parent, false)

        return WatchableViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: WatchableViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class WatchableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(watchable: Watchable) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(watchable.poster)
                    .apply(RequestOptions().override(100, 150))
                    .into(img_photo)

                tv_title.text = watchable.title
                tv_popularity.text = watchable.popularity.toString()
                tv_release.text = watchable.release

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(watchable)
                }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Watchable)
    }

}