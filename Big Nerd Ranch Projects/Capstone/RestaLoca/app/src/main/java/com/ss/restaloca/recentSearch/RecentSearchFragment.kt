package com.ss.restaloca.recentSearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ss.restaloca.common.Businesse
import com.ss.restaloca.database.RestaLocaDatabase
import com.ss.restaloca.databinding.FragmentRecentSearchBinding

class RecentSearchFragment : Fragment() {

    private var _binding: FragmentRecentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var resentSearchViewModel: RecentSearchViewModel
    private lateinit var adapter: RecentSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentSearchBinding.inflate(inflater, container, false)

        // Initialize views and variables
        init()
        // Get the recent search from the database
        getAllRecentSearch()

        return binding.root
    }

    private fun init() {
        // Initialize instance of recent search view model
        val repository = RecentSearchRepository(RestaLocaDatabase(requireContext()))
        val factory = RecentSearchProviderFactory(repository)
        resentSearchViewModel = ViewModelProvider(this, factory)[RecentSearchViewModel::class.java]

        // Bottom navigation configuration
        binding.bottomNavigation.background = null
        binding.bottomNavigation.menu.getItem(0).isEnabled = false
        binding.bottomNavigation.menu.getItem(1).isEnabled = false
        binding.bottomNavigation.setupWithNavController(findNavController())

        // Fab button click handle
        binding.fabNavigation.setOnClickListener {
            val action = RecentSearchFragmentDirections.actionRecentSearchFragmentToMapsFragment()
            findNavController().navigate(action)
        }
    }

    private fun getAllRecentSearch() {
        resentSearchViewModel.getAllRecentSearchById().observe(viewLifecycleOwner, {
            val adapter = RecentSearchAdapter()
            checkData(adapter, it)
        })

        binding.ratingFilter.setOnClickListener {
            resentSearchViewModel.getAllRecentSearchByRating().observe(viewLifecycleOwner, {
                val adapter = RecentSearchAdapter()
                checkData(adapter, it)
            })
        }

        binding.priceFilter.setOnClickListener {
            resentSearchViewModel.getAllRecentSearchByPrice().observe(viewLifecycleOwner, {
                val adapter = RecentSearchAdapter()
                checkData(adapter, it)
            })
        }

        binding.reviewCountFilter.setOnClickListener {
            resentSearchViewModel.getAllRecentSearchByReviewCount().observe(viewLifecycleOwner, {
                val adapter = RecentSearchAdapter()
                checkData(adapter, it)
            })
        }
    }

    private fun checkData(adapter: RecentSearchAdapter, data: List<Businesse>) {
        if (data.isEmpty()) {
            binding.recentSearchRecycler.visibility = View.INVISIBLE
            binding.emptyRecentSearch.visibility = View.VISIBLE
        } else {
            binding.recentSearchRecycler.layoutManager = LinearLayoutManager(requireContext())
            adapter.differ.submitList(data)
            binding.recentSearchRecycler.adapter = adapter
            binding.emptyRecentSearch.visibility = View.INVISIBLE
            binding.recentSearchRecycler.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}