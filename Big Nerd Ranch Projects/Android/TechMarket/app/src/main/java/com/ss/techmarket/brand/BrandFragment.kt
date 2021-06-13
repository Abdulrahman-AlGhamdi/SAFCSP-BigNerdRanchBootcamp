package com.ss.techmarket.brand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ss.techmarket.common.Product
import com.ss.techmarket.common.ProductResponse
import com.ss.techmarket.common.Repository
import com.ss.techmarket.databinding.FragmentBrandBinding
import com.ss.techmarket.exception.NetworkListener
import com.ss.techmarket.utils.changeFont
import kotlinx.coroutines.launch

class BrandFragment : Fragment(), NetworkListener {

    private var _binding: FragmentBrandBinding? = null
    private val binding get() = _binding!!
    private val argument by navArgs<BrandFragmentArgs>()
    private lateinit var brandViewModel: BrandViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrandBinding.inflate(inflater, container, false)

        init()
        showBrands()

        return binding.root
    }

    private fun init() {
        brandViewModel = ViewModelProvider(this)[BrandViewModel::class.java]
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showBrands() {
        val categoriesList = mutableListOf<Pair<String, ProductResponse>>()
        brandViewModel.allCategoryLiveDate.observe(viewLifecycleOwner, { response ->

            brandViewModel.showBrandsJob?.cancel()

            brandViewModel.showBrandsJob = brandViewModel.viewModelScope.launch {
                response.data.forEach { category ->
                    val productResponse = brandViewModel.repository.getProducts(
                        1,
                        category = category.id,
                        tag = argument.id
                    )
                    if (productResponse != null && productResponse.data.isNotEmpty()) {
                        categoriesList.add(category.title to productResponse)
                        binding.recycler.adapter =
                            BrandRecyclerAdapter(requireContext(), categoriesList)
                    }
                }
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
        brandViewModel.repository.addNetworkListener(this)
    }

    override fun onStop() {
        super.onStop()
        brandViewModel.repository.removeNetworkListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}