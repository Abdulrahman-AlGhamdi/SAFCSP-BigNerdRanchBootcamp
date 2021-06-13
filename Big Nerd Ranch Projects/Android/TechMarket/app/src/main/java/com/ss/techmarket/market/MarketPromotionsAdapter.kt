package com.ss.techmarket.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.techmarket.databinding.RowDetailsItemBinding

class MarketPromotionsAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<MarketPromotionsAdapter.PromotionsPagerHolder>() {

    inner class PromotionsPagerHolder(private val binding: RowDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(image: Int) {
            binding.root.load(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PromotionsPagerHolder(
        RowDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PromotionsPagerHolder, position: Int) {
        holder.onBind(imageList[position])
    }

    override fun getItemCount() = imageList.size
}