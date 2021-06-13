package com.ss.restaloca.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ss.restaloca.TestUtils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    private lateinit var detailsViewModel: DetailsViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        detailsViewModel = DetailsViewModel()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    // Test yelp information API
    @Test
    fun `insert valid information id, should return result`() {
        detailsViewModel.getInformation("WavvLdfdP6g8aZTtbBQHTw")
        val value = detailsViewModel.informationLiveData.getOrAwaitValue()
        print(value)
        assert(value.name.isNotBlank())
    }

    @Test
    fun `insert invalid information id, should return null`() {
        detailsViewModel.getInformation("iuefhcnso38")
        val value = detailsViewModel.informationLiveData.getOrAwaitValue()
        print(value)
        assert(value == null)
    }

    @Test
    fun `insert empty information id, should return null`() {
        detailsViewModel.getInformation("")
        val value = detailsViewModel.informationLiveData.getOrAwaitValue()
        print(value)
        assert(value == null)
    }

    // Test yelp reviews API
    @Test
    fun `insert valid review id, should return result`() {
        detailsViewModel.getReviews("WavvLdfdP6g8aZTtbBQHTw")
        val value = detailsViewModel.reviewsLiveData.getOrAwaitValue()
        print(value)
        assert(value.reviews[0].text.isNotBlank())
    }

    @Test
    fun `insert invalid review id, should return null`() {
        detailsViewModel.getReviews("iuefhcnso38")
        val value = detailsViewModel.reviewsLiveData.getOrAwaitValue()
        print(value)
        assert(value == null)
    }

    @Test
    fun `insert empty review id, should return null`() {
        detailsViewModel.getReviews("")
        val value = detailsViewModel.reviewsLiveData.getOrAwaitValue()
        print(value)
        assert(value == null)
    }

    // Test weather API
    @Test
    fun `insert valid weather lat and lon, should return result`() {
        detailsViewModel.getWeather("-12.234 44.00")
        val value = detailsViewModel.weatherLiveData.getOrAwaitValue()
        print(value)
        assert(value.location.country.isNotBlank())
    }

    @Test
    fun `insert empty weather lat and lon, should return null`() {
        detailsViewModel.getWeather("")
        val value = detailsViewModel.weatherLiveData.getOrAwaitValue()
        print(value)
        assert(value == null)
    }
}