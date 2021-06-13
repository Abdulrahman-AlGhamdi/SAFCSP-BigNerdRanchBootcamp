package com.ss.restaloca.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ss.restaloca.TestUtils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MapViewModelTest {

    private lateinit var mapViewModel: MapViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mapViewModel = MapViewModel()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    // Testing yelp search by location API
    @Test
    fun `insert valid location and business, should return result`() {
        mapViewModel.getBusinessesByLocation("london", "restaurant")
        val value = mapViewModel.businessLiveData.getOrAwaitValue()
        print(value)
        value.businesses?.first()?.name?.isNotBlank()?.let { assert(it) }
    }

    @Test
    fun `insert empty location and business, should return null`() {
        mapViewModel.getBusinessesByLocation("", "")
        val value = mapViewModel.businessLiveData.getOrAwaitValue()
        print(value)
        assert(value == null)
    }

    @Test
    fun `insert valid location and empty business, should return result`() {
        mapViewModel.getBusinessesByLocation("london", "")
        val value = mapViewModel.businessLiveData.getOrAwaitValue()
        print(value)
        value.businesses?.first()?.name?.isNotBlank()?.let { assert(it) }
    }

    @Test
    fun `insert empty location and valid business, should return null`() {
        mapViewModel.getBusinessesByLocation("", "restaurant")
        val value = mapViewModel.businessLiveData.getOrAwaitValue()
        print(value)
        assert(value == null)
    }
}