package com.dicoding.picodicloma.moviecatalogue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie.view.*

class ListWatchableAdapter(private val listWatchable: ArrayList<Watchable>) :
    RecyclerView.Adapter<ListWatchableAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(watchable: Watchable) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(watchable.photo)
                    .into(img_photo)

                img_photo.contentDescription = resources.getString(R.string.img_cd_prestring) + watchable.title
                tv_title.text = watchable.title
                tv_year.text = watchable.year
                tv_director.text = watchable.director

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(watchable)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_movie, parent, false)

        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listWatchable.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listWatchable[position])
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Watchable)
    }

}