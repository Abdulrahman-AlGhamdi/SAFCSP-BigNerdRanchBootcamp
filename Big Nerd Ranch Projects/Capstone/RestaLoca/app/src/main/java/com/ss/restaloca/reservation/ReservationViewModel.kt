package com.ss.restaloca.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ReservationViewModel(private val repository: ReservationRepository): ViewModel() {

    fun addReservation(reservation: ReservationModel) = viewModelScope.launch {
        repository.addReservation(reservation)
    }

    fun deleteReservation(reservation: ReservationModel) = viewModelScope.launch {
        repository.deleteReservation(reservation)
    }

    fun getAllReservation() = repository.getAllReservation()
}