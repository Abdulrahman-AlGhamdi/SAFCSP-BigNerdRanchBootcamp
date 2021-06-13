package com.ss.flickrlocation.UI.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ss.flickrlocation.Database.FlickrDatabase
import com.ss.flickrlocation.Model.Image.Photo
import com.ss.flickrlocation.R
import com.ss.flickrlocation.Repository.FlickrRepository
import com.ss.flickrlocation.ViewModel.FlickrViewModel
import com.ss.flickrlocation.ViewModel.FlickrViewModelProviderFactory
import com.ss.flickrlocation.databinding.FragmentMapBinding
import kotlinx.coroutines.launch

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var viewModel: FlickrViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        init()
        checkConnection()

        return binding.root
    }

    private fun init() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val repository = FlickrRepository(FlickrDatabase(requireContext()))
        val viewModelProviderFactory = FlickrViewModelProviderFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(FlickrViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun checkConnection() {
        val connectionManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectionManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        if (isConnected) {
            binding.mapView.visibility = View.GONE
            binding.exploreCheckConnection.visibility = View.GONE
            showLocation()
        } else {
            binding.mapView.visibility = View.VISIBLE
            binding.mapProgressBar.visibility = View.VISIBLE
            binding.exploreCheckConnection.visibility = View.VISIBLE
            binding.exploreCheckConnection.setOnClickListener {
                checkConnection()
            }
        }
    }

    private fun showLocation() {
        mapFragment.getMapAsync { googleMap ->
            getUserLocationPermission(googleMap)
        }
    }

    private fun getUserLocationPermission(googleMap: GoogleMap) {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
            )
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.getImages(it.latitude.toString(), it.longitude.toString())
            googleMap.addCircle(drawCircleAround(it.latitude, it.longitude))
            showPhotoNearUser(googleMap)
            googleMap.isMyLocationEnabled = true
            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(it.latitude, it.longitude))
                .zoom(11.0.toFloat())
                .build()
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            googleMap.setOnMapLongClickListener { onClickLatLng ->
                googleMap.clear()
                viewModel.getImages(
                    onClickLatLng.latitude.toString(),
                    onClickLatLng.longitude.toString()
                )
                googleMap.addCircle(
                    drawCircleAround(
                        onClickLatLng.latitude,
                        onClickLatLng.longitude
                    )
                )
            }
        }
    }

    private fun drawCircleAround(lat: Double, lon: Double): CircleOptions {
        val latLng = LatLng(lat, lon)
        val circle = CircleOptions()
        circle.center(latLng)
        circle.radius(10000.0)
        circle.fillColor(0x20333333)
        circle.strokeWidth(1f)
        return circle
    }

    private fun showPhotoNearUser(googleMap: GoogleMap) {
        viewModel.imagesLiveData.observe(viewLifecycleOwner, { photos ->
            val bounds = LatLngBounds.Builder()
            photos.take(50).forEach { photo ->
                val latLng = LatLng(photo.latitude.toDouble(), photo.longitude.toDouble())
                bounds.include(latLng)
                lifecycleScope.launch {
                    googleMap.addMarker(MarkerOptions().position(latLng).title(photo.id))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(urlImageToBitmap(photo)))
                }
            }

            googleMap.setOnMarkerClickListener { marker ->
                photos.forEach { photo ->
                    when (photo.id) {
                        marker.title -> try {
                            findNavController().navigate(
                                MapFragmentDirections.actionMapFragmentToDetailsFragment(
                                    photo
                                )
                            )
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                    }
                }
                true
            }
        })
    }

    private suspend fun urlImageToBitmap(it: Photo): Bitmap {
        val loading = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data("https://live.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg")
            .transformations(CircleCropTransformation())
            .size(100, 100)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}