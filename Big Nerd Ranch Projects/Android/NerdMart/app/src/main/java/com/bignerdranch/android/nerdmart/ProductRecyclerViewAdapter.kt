package com.bignerdranch.android.nerdmart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nerdmart.databinding.ViewProductRowBinding
import com.bignerdranch.android.nerdmart.viewmodel.ProductViewModel
import com.bignerdranch.android.nerdmartservice.service.payload.Product

class ProductRecyclerViewAdapter(
        var products: List<Product>,
        private val context: Context,
        private val addProductClickEvent: (Product) -> Unit
) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val viewProductRowBinding = DataBindingUtil.inflate<ViewProductRowBinding>(
                layoutInflater, R.layout.view_product_row, parent, false)
        return ProductViewHolder(viewProductRowBinding)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindHolder(products[position], position)
    }

    inner class ProductViewHolder(private val binding: ViewProductRowBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bindHolder(product: Product, position: Int) {
            binding.buyButtonClickListener = View.OnClickListener {
                addProductClickEvent(product)
            }
            binding.productViewModel = ProductViewModel(context, product, position)
        }
    }
}