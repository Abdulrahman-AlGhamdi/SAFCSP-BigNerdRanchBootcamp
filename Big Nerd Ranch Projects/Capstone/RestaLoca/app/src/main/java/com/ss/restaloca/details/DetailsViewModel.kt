package com.ss.restaloca.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.restaloca.common.Repository
import com.ss.restaloca.details.information.InformationModel
import com.ss.restaloca.details.reviews.ReviewModel
import com.ss.restaloca.details.weather.WeatherModel
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private val repository = Repository()
    var informationLiveData: MutableLiveData<InformationModel> = MutableLiveData()
    var reviewsLiveData: MutableLiveData<ReviewModel> = MutableLiveData()
    var weatherLiveData: MutableLiveData<WeatherModel> = MutableLiveData()

    fun getInformation(id: String) = viewModelScope.launch {
        val response = repository.getInformation(id)
        if (response.isSuccessful)
            informationLiveData.postValue(response.body())
    }

    fun getReviews(id: String) = viewModelScope.launch {
        val response = repository.getReviews(id)
        if (response.isSuccessful)
            reviewsLiveData.postValue(response.body())
    }

    fun getWeather(location: String) = viewModelScope.launch {
        val response = repository.getWeather(location)
        if (response.isSuccessful)
            weatherLiveData.postValue(response.body())
    }
}