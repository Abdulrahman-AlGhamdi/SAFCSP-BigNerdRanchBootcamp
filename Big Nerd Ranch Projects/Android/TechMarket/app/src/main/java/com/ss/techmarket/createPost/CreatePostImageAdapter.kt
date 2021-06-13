package com.ss.techmarket.createPost

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ss.techmarket.databinding.RowCreateImageItemBinding
import java.lang.Exception

class CreatePostImageAdapter(private val ImageList: List<Uri>) :
    RecyclerView.Adapter<CreatePostImageAdapter.CreatePostImageAdapter>() {

    inner class CreatePostImageAdapter(private val binding: RowCreateImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(image: Uri) {

            try {
                val bitmap: Bitmap =
                    MediaStore.Images.Media.getBitmap(itemView.context.contentResolver, image)
                        ?: return
                binding.image.load(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.root.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CreatePostImageAdapter(
        RowCreateImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CreatePostImageAdapter, position: Int) {
        holder.onBind(ImageList[position])
    }

    override fun getItemCount() = ImageList.size
}