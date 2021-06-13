package com.ss.flickrlocation.UI.Fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ss.flickrlocation.Database.FlickrDatabase
import com.ss.flickrlocation.R
import com.ss.flickrlocation.Repository.FlickrRepository
import com.ss.flickrlocation.ViewModel.FlickrViewModel
import com.ss.flickrlocation.ViewModel.FlickrViewModelProviderFactory
import com.ss.flickrlocation.databinding.FragmentDetailsBinding

class DetailsFragment : BottomSheetDialogFragment() {

    private lateinit var detailsMap: SupportMapFragment
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FlickrViewModel
    private val arguments by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        init()
        checkConnection()

        return binding.root
    }

    private fun init() {
        detailsMap = childFragmentManager.findFragmentById(R.id.details_map) as SupportMapFragment
        val repository = FlickrRepository(FlickrDatabase(requireContext()))
        val viewModelProviderFactory = FlickrViewModelProviderFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(FlickrViewModel::class.java)
    }

    private fun checkConnection() {
        val connectionManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectionManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            binding.detailsCheckConnection.visibility = View.GONE
            checkIfPhotoIsFavorite()
            showImageDetails()
        } else {
            binding.detailsProgressBar.visibility = View.VISIBLE
            binding.detailsCheckConnection.visibility = View.VISIBLE
            binding.detailsCheckConnection.setOnClickListener {
                checkConnection()
            }
        }
    }

    private fun checkIfPhotoIsFavorite() {
        viewModel.getAllFavorites().observe(viewLifecycleOwner, { favorite ->
            for (isFavorite in favorite)
                if (arguments.selectedImage.id == isFavorite.id)
                    arguments.selectedImage.added = isFavorite.added
            addOrRemoveFavorite()
        })
    }

    private fun showImageDetails() {
        binding.detailsProgressBar.visibility = View.VISIBLE
        viewModel.getImageDetails(arguments.selectedImage.id)
        viewModel.imageDetailsLiveData.observe(viewLifecycleOwner, { photo ->
            if (photo.location.region._content != null)
                binding.detailsCity.text = photo.location.region._content
            if (photo.location.country._content != null)
                binding.detailsCountry.text = photo.location.country._content
            if (photo.location.locality._content != null)
                binding.detailsLocality.text = photo.location.locality._content
            if (photo.owner.username != null)
                binding.detailsUsername.text = photo.owner.username
            if (photo.views != null)
                binding.detailsViewCount.text = photo.views
            if (photo.comments._content != null)
                binding.detailsCommentsCount.text = photo.comments._content
            if (photo.location.latitude != null && photo.location.longitude != null) {
                val latLng =
                    LatLng(photo.location.latitude.toDouble(), photo.location.longitude.toDouble())
                detailsMap.getMapAsync { googleMap ->
                    googleMap.addMarker(MarkerOptions().position(latLng))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
            if (photo.server != null && photo.id != null && photo.secret != null)
                binding.detailsImage.load("https://live.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg")
            binding.detailsGroup.visibility = View.VISIBLE
        })
    }

    private fun addOrRemoveFavorite() {
        if (!arguments.selectedImage.added) {
            binding.detailsFavorite.setImageResource(R.drawable.favorite_empty_icon)
            binding.detailsFavorite.setOnClickListener {
                binding.detailsFavorite.setImageResource(R.drawable.favorite_fill_icon)
                viewModel.addFavoriteAndUpdate(arguments.selectedImage)
                arguments.selectedImage.added = true
                Toast.makeText(
                    requireContext(),
                    R.string.successfully_added_to_library,
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            binding.detailsFavorite.setImageResource(R.drawable.favorite_fill_icon)
            binding.detailsFavorite.setOnClickListener {
                binding.detailsFavorite.setImageResource(R.drawable.favorite_empty_icon)
                viewModel.deleteFavorite(arguments.selectedImage)
                arguments.selectedImage.added = false
                Toast.makeText(
                    requireContext(),
                    R.string.successfully_removed_from_favorite,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}