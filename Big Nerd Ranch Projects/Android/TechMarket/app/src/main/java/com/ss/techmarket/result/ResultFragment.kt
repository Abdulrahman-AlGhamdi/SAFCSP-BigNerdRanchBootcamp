package com.ss.techmarket.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ss.techmarket.common.Repository
import com.ss.techmarket.databinding.FragmentResultBinding
import com.ss.techmarket.exception.NetworkListener
import com.ss.techmarket.utils.changeFont

private var checkLiveData = 0

class ResultFragment : Fragment(), NetworkListener {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val argument by navArgs<ResultFragmentArgs>()
    private lateinit var resultViewModel: ResultViewModel
    private var lastPages = 0
    private var count = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        init()
        showData()
        pagination()

        return binding.root
    }

    private fun init() {
        resultViewModel = ViewModelProvider(this)[ResultViewModel::class.java]
        binding.recycler.layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(itemDecoration)
    }

    private fun showData() {
        resultViewModel.searchProduct(count, argument.id)
        resultViewModel.productLiveData.observe(viewLifecycleOwner, {
            if (checkLiveData != it.meta.currentPage) {

                binding.recycler.adapter = ResultRecyclerAdapter(it.data)
                lastPages = it.meta.lastPage
                binding.pageNumber.text = it.meta.currentPage.toString()

                checkLiveData = it.meta.currentPage
            } else {
                checkLiveData = 0
            }
        })
    }

    private fun pagination() {
        binding.next.setOnClickListener {
            if (count < lastPages) {
                count++
                resultViewModel.searchProduct(count, argument.id)
            }

        }
        binding.previous.setOnClickListener {
            if (count > 1) {
                count--
                resultViewModel.searchProduct(count, argument.id)
            }
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
        resultViewModel.repository.addNetworkListener(this)
    }

    override fun onStop() {
        super.onStop()
        resultViewModel.repository.removeNetworkListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}