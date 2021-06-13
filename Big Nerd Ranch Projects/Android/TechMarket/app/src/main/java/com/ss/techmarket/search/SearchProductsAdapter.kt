package com.ss.techmarket.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.techmarket.common.Product
import com.ss.techmarket.databinding.RowResultItemBinding
import com.ss.techmarket.databinding.RowTagItemBinding

class SearchProductsAdapter(private val tagsList: List<Product>) :
    RecyclerView.Adapter<SearchProductsAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: RowResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(product: Product) {
            binding.image.load(product.images.first())
            binding.title.text = product.title
            binding.price.text = product.price.toString()
            binding.description.text = product.description

            binding.root.setOnClickListener {
                val direction = SearchFragmentDirections
                val action = direction.actionSearchFragmentToDetailsFragment(product.id)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(
        RowResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.onBind(tagsList[position])
    }

    override fun getItemCount() = tagsList.size
}