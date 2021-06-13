package com.ss.restaloca.details.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ss.restaloca.MainActivity
import com.ss.restaloca.databinding.FragmentWeatherPagerBinding
import com.ss.restaloca.details.DetailsViewModel

class WeatherPagerFragment : Fragment() {

    private var _binding: FragmentWeatherPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel
    private var day = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherPagerBinding.inflate(inflater, container, false)

        // Initialize views and variables
        init()
        chooseDay()
        showWeatherData(day)

        return binding.root
    }

    private fun init() {
        detailsViewModel = (activity as MainActivity).detailsViewModel
    }

    private fun chooseDay() {
        // Decrease Date
        binding.decrease.setOnClickListener {
            if (day > 0) {
                day--
                showWeatherData(day)
            }
        }

        // Increase Date
        binding.increase.setOnClickListener {
            if (day < 2) {
                day++
                showWeatherData(day)
            }
        }
    }

    private fun showWeatherData(day: Int) {
        // Bind Weather Data
        detailsViewModel.weatherLiveData.observe(viewLifecycleOwner, {
            // Date of weather
            binding.days.text = it.forecast.forecastday[day].date.substringBefore(" ")

            // Weather recycler view adapter
            val adapter = WeatherRecyclerAdapter()
            binding.detailsRecycler.layoutManager = GridLayoutManager(requireContext(), 4)
            adapter.differ.submitList(it.forecast.forecastday[day].hour)
            binding.detailsRecycler.adapter = adapter

            // Hide progress bar - Show Data
            binding.detailsRecycler.visibility = View.VISIBLE
            binding.date.visibility = View.VISIBLE
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}