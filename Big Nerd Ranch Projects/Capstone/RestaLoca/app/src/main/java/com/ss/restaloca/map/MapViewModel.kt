package com.ss.restaloca.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.restaloca.common.BusinessModel
import com.ss.restaloca.common.Repository
import kotlinx.coroutines.launch

class MapViewModel: ViewModel() {

    private val repository = Repository()
    var querySuggestionSearch: MutableLiveData<String>? = MutableLiveData()
    var businessLiveData: MutableLiveData<BusinessModel> = MutableLiveData()

    fun getBusinessesByLocation(location: String, business: String) = viewModelScope.launch {
        val response = repository.getBusinessesByLocation(location, business)
        if (response.isSuccessful)
            businessLiveData.postValue(response.body())
    }

    fun getBusinessesByLatLng(latitude: String, longitude: String, business: String) = viewModelScope.launch {
        val response = repository.getBusinessesByLatLng(latitude, longitude, business)
        if (response.isSuccessful)
            businessLiveData.postValue(response.body())
    }
}