package com.ss.restaloca.details.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ss.restaloca.MainActivity
import com.ss.restaloca.databinding.FragmentReviewsPagerBinding
import com.ss.restaloca.details.DetailsViewModel

class ReviewsPagerFragment : Fragment() {

    private var _binding: FragmentReviewsPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsPagerBinding.inflate(inflater, container, false)

        // Initialize views and variables
        init()
        showCommentsData()

        return binding.root
    }

    private fun init() {
        detailsViewModel = (activity as MainActivity).detailsViewModel
    }

    private fun showCommentsData() {
        detailsViewModel.reviewsLiveData.observe(viewLifecycleOwner, {
            binding.reviewRecycler.layoutManager = LinearLayoutManager(requireContext())
            val adapter = ReviewsRecyclerAdapter()
            adapter.differ.submitList(it.reviews)
            binding.reviewRecycler.adapter = adapter

            // Hide progress bar - Show Data
            binding.reviewRecycler.visibility = View.VISIBLE
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}