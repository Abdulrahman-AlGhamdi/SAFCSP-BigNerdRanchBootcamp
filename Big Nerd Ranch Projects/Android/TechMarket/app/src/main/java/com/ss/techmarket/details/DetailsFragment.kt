package com.ss.techmarket.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.ss.techmarket.common.Repository
import com.ss.techmarket.databinding.FragmentDetailsBinding
import com.ss.techmarket.exception.NetworkListener
import com.ss.techmarket.utils.changeFont

private var checkLiveData = 0

class DetailsFragment : Fragment(), NetworkListener {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val argument by navArgs<DetailsFragmentArgs>()
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        init()
        showData()

        return binding.root
    }

    private fun init() {
        detailsViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        binding.image.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun showData() {
        detailsViewModel.getProductDetails(argument.id)
        detailsViewModel.productDetailsLiveData.observe(viewLifecycleOwner, { product ->
            if (checkLiveData != product.id) {
                binding.title.text = product.title
                binding.description.text = product.description
                binding.price.text = product.price.toString()
                for (image in product.images) {
                    detailsViewModel.detailImages.add(image)
                }
                binding.image.adapter = DetailsPagerAdapter(detailsViewModel.detailImages)
                binding.indicator.setViewPager(binding.image)

                product.tags.forEach { tag ->
                    binding.tags.addView(Chip(activity).apply {
                        text = tag.title
                    })
                }
                checkLiveData = product.id
            } else {
                checkLiveData = 0
            }
        })
    }

    override fun onBadRequest() {
        displaySnackBar("Network Bad Request")
    }

    override fun onGatewayTimeOut() {
        displaySnackBar("Connection Time Out")
    }

    override fun onClientTimeOut() {
        displaySnackBar("Connection Time Out")
    }

    override fun onUnauthorized() {
        displaySnackBar("Unauthorized User")
    }

    override fun onInternalError() {
        displaySnackBar("Network Internal Error")
    }

    private fun displaySnackBar(message: String) {
        val snack = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        snack.changeFont(requireContext())
        snack.show()
    }

    override fun onStart() {
        super.onStart()
        detailsViewModel.repository.addNetworkListener(this)
    }

    override fun onStop() {
        super.onStop()
        detailsViewModel.repository.removeNetworkListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}