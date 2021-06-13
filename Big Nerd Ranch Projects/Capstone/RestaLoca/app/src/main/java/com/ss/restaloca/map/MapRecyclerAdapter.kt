package com.ss.restaloca.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.restaloca.R
import com.ss.restaloca.common.Businesse
import com.ss.restaloca.databinding.RowMapItemBinding

class MapRecyclerAdapter : RecyclerView.Adapter<MapRecyclerAdapter.MapViewHolder>() {

    inner class MapViewHolder(private val binding: RowMapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(business: Businesse) {
            // Bind data
            binding.itemBusinessName.text = business.name
            binding.itemBusinessImage.load(business.image_url) {
                error(R.drawable.place_holder)
            }
            binding.itemBusinessRating.rating = business.rating.toFloat()
            binding.itemBusinessRatingCount.text = business.review_count.toString()

            // On item click
            binding.root.setOnClickListener {
                val direction = MapsFragmentDirections
                val action = direction.actionMapsFragmentToDetailsFragment(business)
                itemView.findNavController().navigate(action)
            }
        }
    }

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Businesse>() {
        override fun areItemsTheSame(oldItem: Businesse, newItem: Businesse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Businesse, newItem: Businesse): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MapViewHolder(
        RowMapItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}