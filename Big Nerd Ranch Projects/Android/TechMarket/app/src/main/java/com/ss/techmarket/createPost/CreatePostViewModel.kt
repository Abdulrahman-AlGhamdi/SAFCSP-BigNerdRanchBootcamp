package com.ss.techmarket.createPost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.techmarket.common.Repository
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class CreatePostViewModel : ViewModel() {

    val repository = Repository.get()
    var promotionList: MutableList<Uri> = mutableListOf()

    fun createPost(requestBody: RequestBody) {
        viewModelScope.launch {
            repository.createPost(requestBody)
        }
    }
}