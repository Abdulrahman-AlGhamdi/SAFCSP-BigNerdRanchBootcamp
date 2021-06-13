package com.ss.flickrlocation.UI.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.ss.flickrlocation.Adapter.FlickrAdapter
import com.ss.flickrlocation.Database.FlickrDatabase
import com.ss.flickrlocation.R
import com.ss.flickrlocation.Repository.FlickrRepository
import com.ss.flickrlocation.ViewModel.FlickrViewModel
import com.ss.flickrlocation.ViewModel.FlickrViewModelProviderFactory
import com.ss.flickrlocation.Utils.Constant.PERMISSION_REQUEST_CODE
import com.ss.flickrlocation.databinding.FragmentExploreBinding

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FlickrViewModel
    private lateinit var exploreAdapter: FlickrAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        init()
        checkConnection()

        return binding.root
    }

    private fun init() {
        val repository = FlickrRepository(FlickrDatabase(requireContext()))
        val viewModelProviderFactory = FlickrViewModelProviderFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(FlickrViewModel::class.java)
        exploreAdapter = FlickrAdapter(ExploreFragment::class.java.name)
        binding.exploreRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun checkConnection() {
        val connectionManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectionManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            binding.exploreCheckConnection.visibility = View.GONE
            getUserLocationPermission()
        } else {
            binding.exploreProgressBar.visibility = View.VISIBLE
            binding.exploreCheckConnection.visibility = View.VISIBLE
            binding.exploreCheckConnection.setOnClickListener {
                checkConnection()
            }
        }
    }

    private fun getUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.getImages(it.latitude.toString(), it.longitude.toString())
            showImages()
        }
    }

    private fun showImages() {
        binding.exploreProgressBar.visibility = View.VISIBLE
        viewModel.imagesLiveData.observe(viewLifecycleOwner, { images ->
            exploreAdapter.differ.submitList(images)
            binding.exploreRecyclerView.adapter = exploreAdapter
            binding.exploreProgressBar.visibility = View.GONE
            binding.exploreRecyclerView.visibility = View.VISIBLE
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}