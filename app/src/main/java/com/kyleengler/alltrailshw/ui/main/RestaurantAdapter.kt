package com.kyleengler.alltrailshw.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kyleengler.alltrailshw.R
import com.kyleengler.alltrailshw.databinding.ListViewBinding
import com.kyleengler.alltrailshw.model.RestaurantModel
import com.squareup.picasso.Picasso

class RestaurantAdapter : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    var restaurants: List<RestaurantModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size

    class ViewHolder(private val binding: ListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: RestaurantModel) {
            binding.restaurant.name.text = restaurant.name
            val priceLevel = restaurant.formatPriceLevel
            val type = restaurant.type
            val supportText = when {
                priceLevel != null && type != null -> "$priceLevel · $type"
                priceLevel != null -> priceLevel
                type != null -> type
                else -> null
            }
            binding.restaurant.supportText.text = supportText
            binding.restaurant.ratingBar.rating = restaurant.rating.toFloat()
            binding.restaurant.ratingCount.text = "(${restaurant.userRatingsTotal})"

            val key = binding.restaurant.root.context.getString(R.string.places_api_key)
            val url = restaurant.getPhotoUrl(key)
            if (url != null) {
                Picasso.get().load(url).into(binding.restaurant.image)
            }
        }
    }
}