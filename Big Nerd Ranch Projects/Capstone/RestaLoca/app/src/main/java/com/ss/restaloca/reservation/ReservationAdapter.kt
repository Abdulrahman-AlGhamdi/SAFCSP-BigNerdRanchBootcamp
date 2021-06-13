package com.ss.restaloca.reservation

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.GrayscaleTransformation
import com.ss.restaloca.R
import com.ss.restaloca.databinding.RowReservationItemBinding
import java.util.*

class ReservationAdapter : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(private val binding: RowReservationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reservation: ReservationModel) {

            // Data configuration and binding
            binding.name.text = reservation.businessName
            binding.description.text = reservation.reservationDescription
            binding.root.setCardBackgroundColor(reservation.reservationColor)
            binding.colorPicked.setBackgroundColor(reservation.reservationColor)

            binding.image.load(reservation.businessImageUrl) {
                error(R.drawable.place_holder)
                transformations(GrayscaleTransformation())
            }

            binding.note.apply {
                var newText = ""
                reservation.reservationNote.split(" ").forEach {
                    newText += it.capitalize(Locale.ROOT) + " "
                }
                text = newText.trimEnd()
                setTextColor(reservation.reservationColor)
            }

            binding.date.apply {
                text = reservation.reservationDate
                setTextColor(reservation.reservationColor)
            }

            binding.ratingCount.apply {
                text = reservation.businessReviewCount.toString()
                setTextColor(reservation.reservationColor)
            }

            binding.rating.apply {
                rating = reservation.businessRating.toFloat()
                progressDrawable.setColorFilter(
                    reservation.reservationColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            }

            binding.phoneIcon.setColorFilter(reservation.reservationColor)
            binding.phone.setOnClickListener {
                startActivity(
                    binding.root.context,
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + reservation.businessPhone)),
                    null
                )
            }

            binding.locationIcon.setColorFilter(reservation.reservationColor)
            binding.location.setOnClickListener {
                val location = "${reservation.businessLatitude},${reservation.businessLongitude}"
                val gmmIntentUri = Uri.parse("http://maps.google.com/maps?q=loc:$location")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(binding.root.context, mapIntent, null)
            }

            binding.root.setOnClickListener {
                reservation.reservationItemClicked = !reservation.reservationItemClicked
                val isClicked = reservation.reservationItemClicked
                if (isClicked)
                    binding.more.visibility = View.VISIBLE
                else
                    binding.more.visibility = View.GONE
            }

            binding.root.setOnLongClickListener {
                try {
                    val direction = ReservationFragmentDirections
                    val action = direction.actionReservationFragmentToReserveFragment(reservation)
                    itemView.findNavController().navigate(action)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                true
            }
        }
    }

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<ReservationModel>() {
        override fun areItemsTheSame(
            oldItem: ReservationModel,
            newItem: ReservationModel
        ): Boolean {
            return oldItem.businessId == newItem.businessId
        }

        override fun areContentsTheSame(
            oldItem: ReservationModel,
            newItem: ReservationModel
        ): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReservationViewHolder(
        RowReservationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}