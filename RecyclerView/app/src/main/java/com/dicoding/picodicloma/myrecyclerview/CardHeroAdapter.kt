package com.dicoding.picodicloma.myrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_card_hero.view.*

class CardHeroAdapter (private val listHero: ArrayList<Hero>) : RecyclerView.Adapter<CardHeroAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hero: Hero) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(hero.photo)
                    .apply(RequestOptions().override(350,550))
                    .into(img_item_photo)

                tv_item_name.text = hero.name
                tv_item_description.text = hero.description

                btn_set_favorite.setOnClickListener{
                    Toast.makeText(it.context, "Favorite ${hero.name}", Toast.LENGTH_SHORT).show()
                }

                btn_set_share.setOnClickListener {
                    Toast.makeText(it.context, "Share ${hero.name}", Toast.LENGTH_SHORT).show()
                }

                itemView.setOnClickListener{
                    Toast.makeText(it.context, "Kamu memilih ${hero.name}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_hero, parent, false)

        return CardViewHolder(view)
    }

    override fun getItemCount(): Int = listHero.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(listHero[position])
    }

}