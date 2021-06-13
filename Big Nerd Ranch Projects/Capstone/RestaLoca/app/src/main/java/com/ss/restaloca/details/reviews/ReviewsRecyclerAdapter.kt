package com.ss.restaloca.details.reviews

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.restaloca.R
import com.ss.restaloca.databinding.RowReviewsItemBinding

class ReviewsRecyclerAdapter : RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(private val binding: RowReviewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.username.text = review.user.name
            if (review.user.image_url == null)
                binding.image.load(R.drawable.no_account_image)
            else
                binding.image.load(review.user.image_url)
            binding.rating.rating = review.rating.toFloat()
            binding.review.text = review.text
            binding.readMore.setOnClickListener {
                it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(review.url)))
            }
        }
    }

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        RowReviewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}