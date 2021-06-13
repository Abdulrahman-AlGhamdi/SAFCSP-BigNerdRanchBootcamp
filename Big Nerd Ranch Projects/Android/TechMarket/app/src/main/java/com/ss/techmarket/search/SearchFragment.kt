package com.ss.techmarket.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ss.techmarket.R
import com.ss.techmarket.databinding.FragmentSearchBinding
import com.ss.techmarket.exception.NetworkListener
import com.ss.techmarket.utils.changeFont

class SearchFragment : Fragment(), NetworkListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchViewModel: SearchViewModel
    var addOnce = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        init()
        getAutoComplete()
        onSuggestionCLick()

        return binding.root
    }

    private fun init() {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getAutoComplete() {
        binding.autoComplete.addTextChangedListener { editable ->
            if (editable != null && editable.isNotBlank())
                if (editable.toString().first().toString() == "#")
                    showSuggestions(searchViewModel.tagsSuggestions.keys.toMutableList(), true)
                else
                    searchViewModel.getSearchProducts(editable.toString())
        }

        searchViewModel.productsListLiveData.observe(viewLifecycleOwner, { response ->
            searchViewModel.productsSuggestions.clear()

            response.data.forEach {
                searchViewModel.productsSuggestions.plusAssign(it.title to it.id)
            }
            showSuggestions(searchViewModel.productsSuggestions.keys.toMutableList(), false)
        })

        searchViewModel.tagListLiveData.observe(viewLifecycleOwner, { response ->
            searchViewModel.tagsSuggestions.clear()

            response.data.forEach {
                searchViewModel.tagsSuggestions.plusAssign("#${it.title}" to it.id)
            }
        })
    }

    private fun showSuggestions(list: MutableList<String>, check: Boolean) {
        if (check) {
            if (addOnce) {
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.row_search_autocomplete_item,
                    list
                )
                binding.autoComplete.setAdapter(adapter)
                addOnce = false
            }
        } else {
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.row_search_autocomplete_item,
                list
            )
            binding.autoComplete.setAdapter(adapter)
            addOnce = true
        }
    }

    private fun onSuggestionCLick() {

        binding.autoComplete.setOnItemClickListener { parent, view, position, id ->

            searchViewModel.tagsSuggestions.forEach {
                if (it.key == parent.getItemAtPosition(position))
                    searchViewModel.getSearchProductsByTag(it.value)
            }

            searchViewModel.productsSuggestions.forEach {
                if (it.key == parent.getItemAtPosition(position)) {
                    val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it.value)
                    findNavController().navigate(action)
                }
            }
        }

        searchViewModel.tagProductListLiveData.observe(viewLifecycleOwner, {
            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireView().windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            binding.recycler.adapter = SearchProductsAdapter(it.data)
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
        searchViewModel.repository.addNetworkListener(this)
    }

    override fun onStop() {
        super.onStop()
        searchViewModel.repository.removeNetworkListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}