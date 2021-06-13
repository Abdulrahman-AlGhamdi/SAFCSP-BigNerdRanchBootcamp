package com.ss.restaloca.details.information

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ss.restaloca.R
import com.ss.restaloca.databinding.RowInformationItemBinding
import com.ss.restaloca.details.reviews.Review

class InformationRecyclerAdapter :
    RecyclerView.Adapter<InformationRecyclerAdapter.InformationViewHolder>() {

    class InformationViewHolder(private val binding: RowInformationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(open: Open) {
            when (open.day) {
                0 -> binding.day.text = itemView.context.getString(R.string.monday)
                1 -> binding.day.text = itemView.context.getString(R.string.tuesday)
                2 -> binding.day.text = itemView.context.getString(R.string.wednesday)
                3 -> binding.day.text = itemView.context.getString(R.string.thursday)
                4 -> binding.day.text = itemView.context.getString(R.string.friday)
                5 -> binding.day.text = itemView.context.getString(R.string.saturday)
                6 -> binding.day.text = itemView.context.getString(R.string.sunday)
            }
            when {
                open.start.toInt() < 1200 -> binding.timeOpen.text =
                    "Open ${open.start.substring(0, 2)}:${open.start.substring(2)} AM"
                open.start.toInt() == 1200 -> binding.timeOpen.text =
                    "Open ${open.start.substring(0, 2)}:${open.start.substring(2)} PM"
                open.start.toInt() > 1200 -> binding.timeOpen.text =
                    "Open ${(open.start.substring(0, 2).toInt()-12)}:${open.start.substring(2)} PM"
            }
            when {
                open.end.toInt() < 1200 -> binding.timeClose.text =
                    "Close ${open.end.substring(0, 2)}:${open.end.substring(2)} AM"
                open.end.toInt() == 1200 -> binding.timeClose.text =
                    "Close ${open.end.substring(0, 2)}:${open.end.substring(2)} PM"
                open.end.toInt() > 1200 -> binding.timeClose.text =
                    "Close ${(open.end.substring(0, 2).toInt()-12)}:${open.end.substring(2)} PM"
            }
        }
    }

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Open>() {
        override fun areItemsTheSame(oldItem: Open, newItem: Open): Boolean {
            return oldItem.day == newItem.day
        }

        override fun areContentsTheSame(oldItem: Open, newItem: Open): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InformationViewHolder(
        RowInformationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: InformationViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}