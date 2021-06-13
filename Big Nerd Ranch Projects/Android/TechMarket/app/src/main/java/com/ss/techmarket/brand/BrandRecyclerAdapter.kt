package com.ss.techmarket.brand

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ss.techmarket.common.Category
import com.ss.techmarket.common.Product
import com.ss.techmarket.common.ProductResponse
import com.ss.techmarket.databinding.RowBrandTagsItemBinding
import java.util.*

class BrandRecyclerAdapter(
    private val context: Context,
    private val categoryList: MutableList<Pair<String, ProductResponse>>
) :
    RecyclerView.Adapter<BrandRecyclerAdapter.BrandViewHolder>() {

    inner class BrandViewHolder(private val binding: RowBrandTagsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(responsePair: Pair<String, ProductResponse>) {
            binding.recycler.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.recycler.adapter = TagRecyclerAdapter(responsePair.second.data)
            binding.name.text = responsePair.first.capitalize(Locale.ROOT)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BrandViewHolder(
        RowBrandTagsItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.onBind(categoryList[position])
    }

    override fun getItemCount() = categoryList.size
}