package com.ss.techmarket.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.techmarket.common.Repository
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel() {

    val repository = Repository.get()
    val productLiveData = repository.productListLiveData

    fun searchProduct(page: Int, categoryId: Int) = viewModelScope.launch {
        repository.searchProduct(page, category = categoryId)
    }
}