package com.ss.restaloca.details.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.restaloca.databinding.RowWeatherItemBinding

class WeatherRecyclerAdapter : RecyclerView.Adapter<WeatherRecyclerAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(private val binding: RowWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind (weather: Hour) {

                // Binding Data
                binding.icon.load("https:${weather.condition.icon}")
                binding.time.text = weather.time.substringAfter(" ")
            }
    }

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Hour>() {
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WeatherViewHolder(
        RowWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}