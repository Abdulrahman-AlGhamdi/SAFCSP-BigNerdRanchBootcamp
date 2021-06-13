package com.ss.techmarket.brand

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.techmarket.common.Product
import com.ss.techmarket.databinding.RowTagItemBinding

class TagRecyclerAdapter(private val tagsList: List<Product>) :
    RecyclerView.Adapter<TagRecyclerAdapter.TagsViewHolder>() {

    inner class TagsViewHolder(private val binding: RowTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(product: Product) {
            binding.image.load(product.images.first())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TagsViewHolder(
        RowTagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.onBind(tagsList[position])
    }

    override fun getItemCount() = tagsList.size
}