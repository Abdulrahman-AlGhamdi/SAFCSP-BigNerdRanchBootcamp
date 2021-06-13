package com.ss.restaloca.reservation

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReservationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReservation(reservation: ReservationModel)

    @Delete
    suspend fun deleteReservation(reservation: ReservationModel)

    @Query("SELECT * FROM reservation_table ORDER BY businessId DESC")
    fun getAllReservation(): LiveData<List<ReservationModel>>
}