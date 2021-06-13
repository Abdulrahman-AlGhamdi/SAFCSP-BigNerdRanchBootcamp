package com.ss.techmarket.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.techmarket.databinding.RowDetailsItemBinding

class DetailsPagerAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<DetailsPagerAdapter.PagerHolder>() {

    inner class PagerHolder(private val binding: RowDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(image: String) {
            binding.root.load(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerHolder(
        RowDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PagerHolder, position: Int) {
        holder.onBind(imageList[position])
    }

    override fun getItemCount() = imageList.size
}