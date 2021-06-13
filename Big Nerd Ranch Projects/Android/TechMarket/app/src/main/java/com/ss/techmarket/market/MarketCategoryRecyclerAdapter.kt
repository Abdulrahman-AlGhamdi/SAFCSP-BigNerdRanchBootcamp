package com.ss.techmarket.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.techmarket.common.Category
import com.ss.techmarket.databinding.RowMarketCategoryItemBinding

class MarketCategoryRecyclerAdapter(private val categoryList: List<Category>) :
    RecyclerView.Adapter<MarketCategoryRecyclerAdapter.MarketCategoryViewHolder>() {

    inner class MarketCategoryViewHolder(private val binding: RowMarketCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(category: Category) {
            binding.image.load(category.image)
            binding.name.text = category.title

            binding.root.setOnClickListener {
                val direction = MarketFragmentDirections
                val action = direction.actionMarketFragmentToResultFragment(category.id)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MarketCategoryViewHolder(
        RowMarketCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MarketCategoryViewHolder, position: Int) {
        holder.onBind(categoryList[position])
    }

    override fun getItemCount() = categoryList.size
}