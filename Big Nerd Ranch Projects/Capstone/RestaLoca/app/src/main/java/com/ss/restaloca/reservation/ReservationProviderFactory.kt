package com.ss.restaloca.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReservationProviderFactory(private val repository: ReservationRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReservationViewModel(repository) as T
    }
}