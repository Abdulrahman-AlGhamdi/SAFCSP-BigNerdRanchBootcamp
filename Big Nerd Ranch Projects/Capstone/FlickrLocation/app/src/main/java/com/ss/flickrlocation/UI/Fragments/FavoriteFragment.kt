package com.ss.flickrlocation.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ss.flickrlocation.Adapter.FlickrAdapter
import com.ss.flickrlocation.Database.FlickrDatabase
import com.ss.flickrlocation.Model.Image.Photo
import com.ss.flickrlocation.R
import com.ss.flickrlocation.Repository.FlickrRepository
import com.ss.flickrlocation.ViewModel.FlickrViewModel
import com.ss.flickrlocation.ViewModel.FlickrViewModelProviderFactory
import com.ss.flickrlocation.databinding.FragmentDetailsBinding
import com.ss.flickrlocation.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: FlickrViewModel
    lateinit var flickrAdapter: FlickrAdapter
    private lateinit var libraryList: MutableList<Photo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        init()
        showFavoriteList()
        deleteFromLibrary()
        deleteAllFavorite()

        return binding.root
    }

    private fun init() {
        libraryList = mutableListOf()
        flickrAdapter = FlickrAdapter(FavoriteFragment::class.java.name)
        val repository = FlickrRepository(FlickrDatabase(requireContext()))
        val viewModelProviderFactory = FlickrViewModelProviderFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(FlickrViewModel::class.java)
        binding.favoriteRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun showFavoriteList() {
        viewModel.getAllFavorites().observe(viewLifecycleOwner, {
            flickrAdapter.differ.submitList(it)
            binding.favoriteRecyclerView.adapter = flickrAdapter
        })
    }

    private fun deleteFromLibrary() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val photo = flickrAdapter.differ.currentList[position]
                viewModel.deleteFavorite(photo)
                photo.added = false
                Snackbar.make(requireView(), R.string.photo_successfully_deleted, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.undo) {
                        viewModel.addFavoriteAndUpdate(photo)
                        photo.added = true
                    }
                    .show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.favoriteRecyclerView)
    }

    private fun deleteAllFavorite() {
        binding.favoriteDeleteAll.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
                .setCancelable(false)
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.deleteAllFavorites()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                }

            val alert = dialogBuilder.create()
            alert.setTitle(R.string.delete_all_the_photos)
            alert.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}