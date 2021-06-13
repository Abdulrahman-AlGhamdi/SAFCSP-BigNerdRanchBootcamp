package com.ss.techmarket.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.techmarket.common.Repository
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    val repository = Repository.get()
    var detailImages: MutableList<String> = mutableListOf()
    val productDetailsLiveData = repository.productDetailsLiveData

    fun getProductDetails(id: Int) = viewModelScope.launch {
        repository.getProductDetails(id)
    }
}