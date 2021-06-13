package com.ss.restaloca.recentSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecentSearchProviderFactory(private val repository: RecentSearchRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecentSearchViewModel(repository) as T
    }
}