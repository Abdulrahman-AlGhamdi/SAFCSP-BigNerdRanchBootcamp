package com.ss.techmarket.brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ss.techmarket.common.Category
import com.ss.techmarket.common.Product
import com.ss.techmarket.common.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BrandViewModel : ViewModel() {

    val repository = Repository.get()
    val allCategoryLiveDate = repository.categoriesListLiveData
    var showBrandsJob: Job? = null
}