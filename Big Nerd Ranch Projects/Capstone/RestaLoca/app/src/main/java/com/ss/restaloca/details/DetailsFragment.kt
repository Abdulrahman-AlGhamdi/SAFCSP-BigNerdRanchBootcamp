package com.ss.restaloca.details

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.ss.restaloca.onBoarding.ViewPagerAdapter
import com.ss.restaloca.details.reviews.ReviewsPagerFragment
import com.ss.restaloca.details.information.InformationPagerFragment
import com.ss.restaloca.details.weather.WeatherPagerFragment
import com.ss.restaloca.MainActivity
import com.ss.restaloca.R
import com.ss.restaloca.databinding.FragmentDetailsBinding

private var checkLiveData = ""

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val argument by navArgs<DetailsFragmentArgs>()
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        init()
        checkConnection()

        return binding.root
    }

    private fun checkConnection() {
        val connectionManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectionManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            // if connected Initialize views and variables
            showData()
        } else {
            // if not connected
            binding.group.visibility = View.INVISIBLE
            binding.loading.visibility = View.VISIBLE
            binding.checkConnection.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    checkConnection()
                }
            }
        }
    }

    private fun init() {
        // Business view model instance
        detailsViewModel = (activity as MainActivity).detailsViewModel

        // View pager adapter configuration
        val fragment =
            arrayListOf(InformationPagerFragment(), ReviewsPagerFragment(), WeatherPagerFragment())
        val adapter =
            ViewPagerAdapter(fragment, requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        // View pager tabs name
        TabLayoutMediator(binding.detailsTab, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.info)
                1 -> tab.text = getString(R.string.reviews)
                2 -> tab.text = getString(R.string.weather)
            }
        }.attach()

        // on click go to reserve page
        binding.reserve.setOnClickListener {
            try {
                val action = DetailsFragmentDirections.actionDetailsFragmentToReserveFragment(
                    null
                )
                findNavController().navigate(action)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    private fun showData() {
        // Hide check connection
        binding.checkConnection.visibility = View.INVISIBLE

        // Business ID for Information API
        detailsViewModel.getInformation(argument.selectedBusiness.id)

        // Business ID for reviews API
        detailsViewModel.getReviews(argument.selectedBusiness.id)

        // Business coordinates for weather API
        val coordinates = argument.selectedBusiness.coordinates
        detailsViewModel.getWeather("${coordinates.latitude} ${coordinates.longitude}")

        detailsViewModel.informationLiveData.observe(viewLifecycleOwner, {
            if (checkLiveData != it.id) {
                // Bind Business Data
                binding.name.text = argument.selectedBusiness.name
                binding.image.load(argument.selectedBusiness.image_url)
                binding.rating.rating = argument.selectedBusiness.rating.toFloat()
                binding.ratingCount.text = argument.selectedBusiness.review_count.toString()
                binding.businessType.text = argument.selectedBusiness.categories.first().title

                // Show data - Hide loading bar
                binding.loading.visibility = View.GONE
                binding.group.visibility = View.VISIBLE

                checkLiveData = it.id
            } else {
                checkLiveData = ""
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}