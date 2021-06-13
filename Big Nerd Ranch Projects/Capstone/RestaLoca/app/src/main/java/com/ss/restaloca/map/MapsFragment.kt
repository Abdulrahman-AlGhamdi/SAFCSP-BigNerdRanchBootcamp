package com.ss.restaloca.map

import android.Manifest
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ss.restaloca.common.BusinessModel
import com.ss.restaloca.common.Constants.REQUEST_PERMISSION_CODE
import com.ss.restaloca.common.Constants.YELP_RADIUS_METER
import com.ss.restaloca.MainActivity
import com.ss.restaloca.R
import com.ss.restaloca.database.RestaLocaDatabase
import com.ss.restaloca.databinding.FragmentMapsBinding
import com.ss.restaloca.recentSearch.RecentSearchProviderFactory
import com.ss.restaloca.recentSearch.RecentSearchRepository
import com.ss.restaloca.recentSearch.RecentSearchViewModel

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mapViewModel: MapViewModel
    private lateinit var recentSearchViewModel: RecentSearchViewModel
    private var searchQuery = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

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
            checkLocationPermission()
        } else {
            // if not connected
            binding.group.visibility = View.GONE
            binding.cardLoading.visibility = View.VISIBLE
            binding.checkConnection.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    checkConnection()
                }
            }
        }
    }

    private fun init() {
        // Initialize instance of restaLoca view model
        mapViewModel = (activity as MainActivity).mapViewModel

        // Initialize map fragment
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Initialize instance of recent search view model
        val repository = RecentSearchRepository(RestaLocaDatabase(requireContext()))
        val factory = RecentSearchProviderFactory(repository)
        recentSearchViewModel = ViewModelProvider(this, factory)[RecentSearchViewModel::class.java]

        // Fill the spinner adapter by putting the business type
        val businessType = listOf(
            MapBusinessType(getString(R.string.restaurant), R.drawable.icon_restaurant),
            MapBusinessType(getString(R.string.coffee), R.drawable.icon_coffee)
        )
        binding.spinner.adapter = MapSpinnerAdapter(requireContext(), businessType)

        // Bottom navigation configuration
        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(1).isEnabled = false
        binding.bottomNavigation.setupWithNavController(findNavController())

        // Adding searchable to search view
        val manager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val component = ComponentName(requireContext(), MainActivity::class.java)
        binding.search.setSearchableInfo(manager.getSearchableInfo(component))
    }

    private fun checkLocationPermission() {
        // Hide check connection - show map
        binding.group.visibility = View.VISIBLE
        binding.cardLoading.visibility = View.GONE
        binding.checkConnection.visibility = View.GONE

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showBusinessesOnMap()
            if (!BUSINESS_RESPONSE.equals(null))
                showRecyclerBusinesses(BUSINESS_RESPONSE)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun showBusinessesOnMap() {
        mapFragment.getMapAsync { googleMap ->

            getSearchQuery(googleMap)
            showBusinesses(googleMap)
            getSearchSuggestion(googleMap)

            googleMap.setOnMapLongClickListener { latLng ->

                // Update the search
                updateData(googleMap, latLng, null)

                // clear the search query
                searchQuery = ""
            }
        }
    }

    private fun getSearchSuggestion(googleMap: GoogleMap) {

        // clear all search history from database
        binding.deleteHistory.setOnClickListener {
            SearchRecentSuggestions(
                requireContext(),
                SearchSuggestions.AUTHORITY,
                SearchSuggestions.MODE
            ).clearHistory()
        }

        binding.search.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                mapViewModel.querySuggestionSearch?.observe(viewLifecycleOwner, { suggestion ->
                    if (searchQuery != suggestion) {

                        // Update the search
                        updateData(googleMap, null, suggestion)

                        // Update for the new suggestion
                        searchQuery = suggestion
                    }
                })
                return false
            }
        })
    }

    private fun getSearchQuery(googleMap: GoogleMap) {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (searchQuery != query) {

                    // Adding query to search history database
                    val suggestions = SearchRecentSuggestions(
                        requireContext(),
                        SearchSuggestions.AUTHORITY,
                        SearchSuggestions.MODE
                    )
                    suggestions.saveRecentQuery(query, null)

                    // Update the search
                    updateData(googleMap, null, query)

                    // Update for the new query
                    searchQuery = query.toString()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun updateData(googleMap: GoogleMap, latLng: LatLng?, query: String?) {
        // Show progress bar - Hide recycler view
        binding.cardLoading.visibility = View.VISIBLE
        binding.mapRecycler.visibility = View.INVISIBLE

        // Clear search focus
        binding.search.clearFocus()

        // Clear map markers
        googleMap.clear()

        // Type of business for location
        if (query != null)
            when (binding.spinner.selectedItemPosition) {
                0 -> mapViewModel.getBusinessesByLocation(
                    query.toString(),
                    getString(R.string.restaurant)
                )
                1 -> mapViewModel.getBusinessesByLocation(
                    query.toString(),
                    getString(R.string.coffee)
                )
            }

        // Type of business for latLng
        if (latLng != null)
            when (binding.spinner.selectedItemPosition) {
                0 -> mapViewModel.getBusinessesByLatLng(
                    latLng.latitude.toString(),
                    latLng.longitude.toString(),
                    getString(R.string.restaurant)
                )
                1 -> mapViewModel.getBusinessesByLatLng(
                    latLng.latitude.toString(),
                    latLng.longitude.toString(),
                    getString(R.string.coffee)
                )
            }
    }

    private fun showBusinesses(googleMap: GoogleMap) {
        mapViewModel.businessLiveData.observe(viewLifecycleOwner, { response ->

            BUSINESS_RESPONSE = response

            var latLng = LatLng(0.0, 0.0)
            if (response.region?.center != null)
                latLng = LatLng(response.region.center.latitude, response.region.center.longitude)

            // Draw radius circle
            drawCircle(googleMap, latLng)
            // Move camera to searched location
            moveCamera(googleMap, latLng)
            // Add markers for each business
            addMarkers(googleMap, response)
            // Show businesses in the recycler view
            showRecyclerBusinesses(response)
            // Show business details after marker clicked
            onMarkerCLick(googleMap, response)
            // Save businesses to database
            saveToDatabase(response)
        })
    }

    private fun drawCircle(googleMap: GoogleMap, latLng: LatLng) {
        val circle = CircleOptions().apply {
            center(latLng)
            radius(YELP_RADIUS_METER.toDouble() * 1.4)
            fillColor(0x20333333)
            strokeWidth(1f)
        }
        googleMap.addCircle(circle)
    }

    private fun moveCamera(googleMap: GoogleMap, latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().apply {
            target(latLng)
            zoom(11.0.toFloat())
        }
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition.build()))
    }

    private fun addMarkers(googleMap: GoogleMap, model: BusinessModel) {
        model.businesses?.forEach { business ->
            val latLng = LatLng(business.coordinates.latitude, business.coordinates.longitude)
            val marker = MarkerOptions()
            marker.apply {
                position(latLng)
                title(business.name)
                snippet(business.id)
            }
            googleMap.addMarker(marker)

            // Hide progress bar - Show recycler view
            binding.cardLoading.visibility = View.INVISIBLE
            binding.mapRecycler.visibility = View.VISIBLE
        }
    }

    private fun showRecyclerBusinesses(business: BusinessModel) {
        val layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding.mapRecycler.layoutManager = layoutManager
        val adapter = MapRecyclerAdapter()
        adapter.differ.submitList(business.businesses)
        binding.mapRecycler.adapter = adapter
    }

    private fun onMarkerCLick(googleMap: GoogleMap, model: BusinessModel) {
        googleMap.setOnMarkerClickListener { marker ->
            model.businesses?.forEach { business ->
                if (marker.snippet == business.id) {
                    val direction = MapsFragmentDirections
                    val action = direction.actionMapsFragmentToDetailsFragment(business)
                    findNavController().navigate(action)
                }
            }
            true
        }
    }

    private fun saveToDatabase(model: BusinessModel) {
        recentSearchViewModel.deleteAllRecentSearch()
        model.businesses?.forEach { business ->
            recentSearchViewModel.addRecentSearch(business)
        }
    }

    companion object {
        private var BUSINESS_RESPONSE = BusinessModel(null, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}