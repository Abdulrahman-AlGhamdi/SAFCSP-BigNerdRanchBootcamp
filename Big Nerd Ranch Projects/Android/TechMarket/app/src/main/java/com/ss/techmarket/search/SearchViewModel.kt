package com.ss.techmarket.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.techmarket.common.ProductResponse
import com.ss.techmarket.common.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private var job: Job? = null
    val repository = Repository.get()
    val tagListLiveData = repository.tagsListLiveData

    val productsSuggestions = mutableMapOf<String, Int>()
    var tagsSuggestions = mutableMapOf<String, Int>()

    val productsListLiveData = MutableLiveData<ProductResponse>()
    var tagProductListLiveData = MutableLiveData<ProductResponse>()

    fun getSearchProducts(query: String) {
        job?.cancel()
        job = viewModelScope.launch {
            val response = repository.getProducts(1, title = query)
            if (response != null)
                productsListLiveData.postValue(response)
        }
    }

    fun getSearchProductsByTag(tag: Int) {
        viewModelScope.launch {
            val response = repository.getProducts(1, tag = tag)
            if (response != null)
                tagProductListLiveData.postValue(response)
        }
    }
}