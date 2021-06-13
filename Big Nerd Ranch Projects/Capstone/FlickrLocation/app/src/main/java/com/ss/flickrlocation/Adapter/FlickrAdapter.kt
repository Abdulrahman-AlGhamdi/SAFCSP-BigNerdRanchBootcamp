package com.ss.flickrlocation.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.flickrlocation.Model.Image.Photo
import com.ss.flickrlocation.R
import com.ss.flickrlocation.UI.Fragments.ExploreFragment
import com.ss.flickrlocation.UI.Fragments.ExploreFragmentDirections
import com.ss.flickrlocation.UI.Fragments.FavoriteFragment
import com.ss.flickrlocation.UI.Fragments.FavoriteFragmentDirections

class FlickrAdapter(private val fragment: String) :
    RecyclerView.Adapter<FlickrAdapter.FlickrViewHolder>() {

    inner class FlickrViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        private val itemDate: TextView = itemView.findViewById(R.id.item_date)

        fun bindData(image: Photo) {

            itemImage.load("https://live.staticflickr.com/${image.server}/${image.id}_${image.secret}.jpg")
            itemDate.text = image.datetaken
        }

        init {
            itemView.setOnClickListener {
                val image = differ.currentList[adapterPosition]
//                try {
                    when (fragment) {
                        ExploreFragment::class.java.name ->
                            itemView.findNavController().navigate(
                                ExploreFragmentDirections.actionExploreFragmentToDetailsFragment(
                                    image
                                )
                            )
                        FavoriteFragment::class.java.name ->
                            itemView.findNavController().navigate(
                                FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(
                                    image
                                )
                            )
                    }
//                } catch (exception: Exception) {
//                    exception.printStackTrace()
//                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FlickrViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.images_row_item, parent, false)
    )

    override fun onBindViewHolder(holderFlickr: FlickrViewHolder, position: Int) {
        val image = differ.currentList[position]
        holderFlickr.bindData(image)
    }

    override fun getItemCount() = differ.currentList.size
}