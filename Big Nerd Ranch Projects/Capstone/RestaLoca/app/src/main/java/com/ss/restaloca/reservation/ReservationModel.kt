package com.ss.restaloca.reservation

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ss.restaloca.common.Constants.RESERVATION_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = RESERVATION_TABLE_NAME)
data class ReservationModel(
    @PrimaryKey
    val businessId: String,
    val businessCategory: String,
    val businessLatitude: Double,
    val businessLongitude: Double,
    val businessImageUrl: String,
    val businessAddress: String,
    val businessName: String,
    val businessPhone: String,
    val businessRating: Double,
    val businessReviewCount: Int,
    val reservationNote: String,
    val reservationDescription: String?,
    val reservationDate: String,
    val reservationColor: Int,
    var reservationAdded: Boolean,
    var reservationItemClicked: Boolean = false
) : Parcelable