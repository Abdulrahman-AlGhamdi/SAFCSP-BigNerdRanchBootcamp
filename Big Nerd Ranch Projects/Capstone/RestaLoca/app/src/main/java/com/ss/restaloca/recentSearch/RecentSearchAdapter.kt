package com.ss.restaloca.recentSearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.BlurTransformation
import com.ss.restaloca.R
import com.ss.restaloca.common.Businesse
import com.ss.restaloca.databinding.RowRecentSearchItemBinding

class RecentSearchAdapter :
    RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {

    inner class RecentSearchViewHolder(private val binding: RowRecentSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(business: Businesse) {
            // Bind data
            binding.name.text = business.name
            binding.image.load(business.image_url) {
                error(R.drawable.place_holder)
                crossfade(true)
                crossfade(500)
            }
            binding.rating.rating = business.rating.toFloat()
            binding.type.text = business.categories.first().title
            binding.ratingCount.text = business.review_count.toString()
            when {
                business.price == null -> binding.price.let { textView ->
                    textView.text = itemView.context.getString(R.string.not_available)
                    textView.setTextColor(itemView.context.resources.getColor(R.color.red, null))
                }
                business.price.length == 1 -> binding.price.let { textView ->
                    textView.text = itemView.context.getString(R.string.inexpensive) + " " + business.price
                }
                business.price.length == 2 -> binding.price.let { textView ->
                    textView.text = itemView.context.getString(R.string.moderately_priced) + " " + business.price
                }
                business.price.length == 3 -> binding.price.let { textView ->
                    textView.text = itemView.context.getString(R.string.expensive) + " " + business.price
                }
                business.price.length == 4 -> binding.price.let { textView ->
                    textView.text = itemView.context.getString(R.string.highly_expensive) + " " + business.price
                }
            }

            // On item click
            binding.root.setOnClickListener {
                val direction = RecentSearchFragmentDirections
                val action = direction.actionRecentSearchFragmentToDetailsFragment(business)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecentSearchViewHolder(
        RowRecentSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}