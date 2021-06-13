package com.ss.techmarket.market

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.ss.techmarket.common.Repository
import kotlinx.coroutines.launch

class MarketViewModel : ViewModel() {

    val repository = Repository.get()
    val allCategoriesLiveData = repository.categoriesListLiveData
    val allTAgsLiveData = repository.tagsListLiveData
    val promotionImageList: MutableList<Int> = mutableListOf()
    var count = 0
    var handler: Handler = Handler(Looper.getMainLooper())
    var checkFirst = true
    lateinit var handlerCallback: Runnable

    fun handlerCallback(promotions: ViewPager2) {
        handlerCallback = object : Runnable {
            override fun run() {
                Log.d("TAG", "///////")
                if (count != promotions.currentItem)
                    count = promotions.currentItem
                count++
                if (checkFirst) {
                    count = 0
                    checkFirst = false
                }
                count %= promotionImageList.size
                promotions.currentItem = count

                handler.postDelayed(this, 2500)
            }
        }
        handler.post(handlerCallback)
    }

    fun removeCallback() {
        handler.removeCallbacks(handlerCallback)
    }

    init {
        getAllCategories()
        getAllTags()
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            repository.getAllCategories()
        }
    }

    private fun getAllTags() {
        viewModelScope.launch {
            repository.getAllTags()
        }
    }
}