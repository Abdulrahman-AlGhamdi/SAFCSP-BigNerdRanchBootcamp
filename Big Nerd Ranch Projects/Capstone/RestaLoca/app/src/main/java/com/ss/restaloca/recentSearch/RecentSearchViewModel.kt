package com.ss.restaloca.recentSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.restaloca.common.Businesse
import kotlinx.coroutines.launch

class RecentSearchViewModel(private val repository: RecentSearchRepository): ViewModel() {

    fun addRecentSearch(business: Businesse) = viewModelScope.launch {
        repository.addRecentSearch(business)
    }

    fun deleteAllRecentSearch() = viewModelScope.launch {
        repository.deleteAllRecentSearch()
    }

    fun getAllRecentSearchById() = repository.getAllRecentSearchById()

    fun getAllRecentSearchByRating() = repository.getAllRecentSearchByRating()

    fun getAllRecentSearchByPrice() = repository.getAllRecentSearchByPrice()

    fun getAllRecentSearchByReviewCount() = repository.getAllRecentSearchReviewCount()
}