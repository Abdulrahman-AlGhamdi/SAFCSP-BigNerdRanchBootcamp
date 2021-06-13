package com.bignerdranch.android.nerdmart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.nerdmart.databinding.FragmentProductsBinding
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface
import com.bignerdranch.android.nerdmartservice.service.payload.Product
import timber.log.Timber
import javax.inject.Inject

class ProductsFragment : NerdMartAbstractFragment() {

    private lateinit var adapter: ProductRecyclerViewAdapter
    private lateinit var fragmentProductsBinding: FragmentProductsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        fragmentProductsBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_products, container, false)
        adapter = ProductRecyclerViewAdapter(emptyList(), context!!,
                this::postProductToCart)
        setupAdapter()

        updateUi()

        return fragmentProductsBinding.root
    }

    private fun setupAdapter() {
        fragmentProductsBinding.fragmentProductsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ProductsFragment.adapter
        }
    }

    private fun updateUi() {
        addDisposable(nerdMartServiceManager
                .getProducts()
                .showLoadingDialog()
                .toList()
                .subscribe { products ->
                    Timber.i("received products: $products")
                    adapter.products = products
                    adapter.notifyDataSetChanged()
                })
    }

    private fun postProductToCart(product: Product) {
        val cartSuccessObservable = nerdMartServiceManager
                .postProductToCart(product)
                .cache()
        addDisposable(cartSuccessObservable
                .subscribe { success ->
                    val message = if (success) {
                        R.string.product_add_success_message
                    } else {
                        R.string.product_add_failure_message
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                })
        addDisposable(cartSuccessObservable.filter { it }
                .flatMap { nerdMartServiceManager.getCart() }
                .showLoadingDialog()
                .subscribe { cart ->
                    (activity as NerdMartAbstractActivity).updateCartStatus(cart)
                    updateUi()
                })
    }
}
