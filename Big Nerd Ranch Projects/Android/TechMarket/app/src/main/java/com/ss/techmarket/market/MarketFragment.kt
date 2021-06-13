package com.ss.techmarket.market

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.ss.techmarket.R
import com.ss.techmarket.databinding.FragmentMarketBinding
import com.ss.techmarket.exception.NetworkListener
import com.ss.techmarket.utils.changeFont

class MarketFragment : Fragment(), NetworkListener {

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private lateinit var marketViewModel: MarketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)

        init()
        showBrands()
        showCategories()
        handleButtonCLick()

        return binding.root
    }

    private fun init() {
        marketViewModel = ViewModelProvider(this)[MarketViewModel::class.java]
        binding.promotions.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        marketViewModel.promotionImageList.clear()
        marketViewModel.promotionImageList.add(R.drawable.promotion1)
        marketViewModel.promotionImageList.add(R.drawable.promotion2)
        marketViewModel.promotionImageList.add(R.drawable.promotion3)
        binding.promotions.adapter = MarketPromotionsAdapter(marketViewModel.promotionImageList)
        binding.indicator.setViewPager(binding.promotions)
        binding.categoryRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.brandRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showCategories() {
        marketViewModel.allCategoriesLiveData.observe(viewLifecycleOwner, {
            binding.categoryRecycler.adapter = MarketCategoryRecyclerAdapter(it.data)
        })
    }

    private fun showBrands() {
        marketViewModel.allTAgsLiveData.observe(viewLifecycleOwner, { response ->
            binding.brandRecycler.adapter =
                MarketBrandRecyclerAdapter(response.data.filter { it.isBrand })
        })
    }

    private fun handleButtonCLick() {
        binding.add.setOnClickListener {
            val action = MarketFragmentDirections.actionMarketFragmentToCreatePostFragment()
            findNavController().navigate(action)
        }
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
        marketViewModel.repository.addNetworkListener(this)
    }

    override fun onResume() {
        super.onResume()
        marketViewModel.handlerCallback(binding.promotions)
    }

    override fun onPause() {
        super.onPause()
        marketViewModel.removeCallback()
    }

    override fun onStop() {
        super.onStop()
        marketViewModel.repository.removeNetworkListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}