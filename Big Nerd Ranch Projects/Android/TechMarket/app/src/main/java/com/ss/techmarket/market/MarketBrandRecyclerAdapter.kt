package com.ss.techmarket.market

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.ss.techmarket.brand.BrandRecyclerAdapter
import com.ss.techmarket.common.Product
import com.ss.techmarket.common.Tag
import com.ss.techmarket.databinding.RowMarketBrandItemBinding
import com.ss.techmarket.databinding.RowResultItemBinding
import com.ss.techmarket.result.ResultFragmentDirections

class MarketBrandRecyclerAdapter(private val tagsList: List<Tag>) :
    RecyclerView.Adapter<MarketBrandRecyclerAdapter.MarketBrandViewHolder>() {

    inner class MarketBrandViewHolder(private val binding: RowMarketBrandItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(tag: Tag) {
            binding.image.load(tag.image)

            binding.root.setOnClickListener {
                val action = MarketFragmentDirections.actionMarketFragmentToBrandFragment(tag.id)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MarketBrandViewHolder(
        RowMarketBrandItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MarketBrandViewHolder, position: Int) {
        holder.onBind(tagsList[position])
    }

    override fun getItemCount() = tagsList.size
}