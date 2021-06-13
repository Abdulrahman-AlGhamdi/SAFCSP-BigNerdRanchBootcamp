package com.ss.restaloca.reservation

import com.ss.restaloca.database.RestaLocaDatabase

class ReservationRepository(private var database: RestaLocaDatabase) {

    suspend fun addReservation(reservation: ReservationModel) =
        database.reservationDao().addReservation(reservation)

    fun getAllReservation() =
        database.reservationDao().getAllReservation()

    suspend fun deleteReservation(reservation: ReservationModel) =
        database.reservationDao().deleteReservation(reservation)
}