package com.ss.techmarket.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.techmarket.common.Product
import com.ss.techmarket.databinding.RowResultItemBinding

class ResultRecyclerAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ResultRecyclerAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: RowResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(product: Product) {
            binding.title.text = product.title
            binding.image.load(product.images.first())
            binding.description.text = product.description
            binding.price.text = "${product.price} Riyal"

            binding.root.setOnClickListener {
                val direction = ResultFragmentDirections
                val action = direction.actionResultFragmentToDetailsFragment(product.id)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        RowResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.onBind(productList[position])
    }

    override fun getItemCount() = productList.size
}