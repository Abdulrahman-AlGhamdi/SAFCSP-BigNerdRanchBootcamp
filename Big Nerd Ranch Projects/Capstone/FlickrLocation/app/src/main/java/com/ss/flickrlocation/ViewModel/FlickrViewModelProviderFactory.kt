package com.ss.flickrlocation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ss.flickrlocation.Repository.FlickrRepository

class FlickrViewModelProviderFactory(private val repository: FlickrRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FlickrViewModel(repository) as T
    }
}