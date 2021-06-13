package com.ss.flickrlocation.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.flickrlocation.Model.Image.Photo
import com.ss.flickrlocation.Repository.FlickrRepository
import kotlinx.coroutines.launch

class FlickrViewModel(private var repository: FlickrRepository) : ViewModel() {

    val imagesLiveData: MutableLiveData<List<Photo>> = MutableLiveData()
    val imageDetailsLiveData: MutableLiveData<com.ss.flickrlocation.Model.Details.Photo> = MutableLiveData()

    fun getImages(lat: String, lon: String) {
        try {
            viewModelScope.launch {
                val response = repository.getImages(lat, lon)
                if (response.isSuccessful)
                    imagesLiveData.postValue(response.body()?.photos?.photo)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun getImageDetails(id: String) {
        try {
            viewModelScope.launch {
                val response = repository.getImageDetails(id)
                if (response.isSuccessful)
                    imageDetailsLiveData.postValue(response.body()?.photo)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun addFavoriteAndUpdate(photo: Photo) = viewModelScope.launch {
        repository.addFavoriteAndUpdate(photo)
    }

    fun deleteFavorite(photo: Photo) = viewModelScope.launch {
        repository.deleteFavorite(photo)
    }

    fun deleteAllFavorites() = viewModelScope.launch {
        repository.deleteAllFavorites()
    }

    fun getAllFavorites() = repository.getAllFavorites()
}