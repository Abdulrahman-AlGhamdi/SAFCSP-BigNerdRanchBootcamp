package com.ss.restaloca

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ss.restaloca.databinding.ActivityMainBinding
import com.ss.restaloca.details.DetailsViewModel
import com.ss.restaloca.map.MapViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var mapViewModel: MapViewModel
    lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        detailsViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        navController = findNavController(R.id.fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)

        if (Intent.ACTION_SEARCH == intent?.action) {
            mapViewModel.querySuggestionSearch?.postValue(intent.getStringExtra(SearchManager.QUERY))
        }
    }
}