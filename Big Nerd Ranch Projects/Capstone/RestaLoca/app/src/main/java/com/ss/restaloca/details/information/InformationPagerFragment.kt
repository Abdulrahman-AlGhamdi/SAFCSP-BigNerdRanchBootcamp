package com.ss.restaloca.details.information

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ss.restaloca.MainActivity
import com.ss.restaloca.R
import com.ss.restaloca.databinding.FragmentInformationPagerBinding
import com.ss.restaloca.details.DetailsViewModel

class InformationPagerFragment : Fragment() {

    private var _binding: FragmentInformationPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationPagerBinding.inflate(inflater, container, false)

        // Initialize views and variables
        init()
        showInformation()

        return binding.root
    }

    private fun init() {
        detailsViewModel = (activity as MainActivity).detailsViewModel
    }

    @SuppressLint("SetTextI18n")
    private fun showInformation() {
        detailsViewModel.informationLiveData.observe(viewLifecycleOwner, { information ->

            // Send list to information recycler adapter
            val layoutManager = GridLayoutManager(requireContext(), 2)
            binding.informationRecycler.layoutManager = layoutManager
            val adapter = InformationRecyclerAdapter()
            information.hours?.first()?.open?.let { open ->
                adapter.differ.submitList(open)
            }
            binding.informationRecycler.adapter = adapter

            // Business phone
            when {
                information.display_phone == null -> binding.phone.let { textView ->
                    textView.text = getString(R.string.not_available)
                    textView.setTextColor(resources.getColor(R.color.red, null))
                }
                information.phone != null -> binding.phone.let { textView ->
                    textView.text = information.display_phone
                    textView.setOnClickListener {
                        startActivity(
                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + information.phone))
                        )
                    }
                }
            }

            // Business address
            binding.address.let { textView ->
                textView.text = information.location.address1
                textView.setTextColor(resources.getColor(R.color.white, null))
            }

            // Business price
            when {
                information.price == null -> binding.price.let { textView ->
                    textView.text = getString(R.string.not_available)
                    textView.setTextColor(resources.getColor(R.color.red, null))
                }
                information.price.length == 1 -> binding.price.let { textView ->
                    textView.text = getString(R.string.inexpensive) + " " + information.price
                    textView.setTextColor(resources.getColor(R.color.green, null))
                }
                information.price.length == 2 -> binding.price.let { textView ->
                    textView.text = getString(R.string.moderately_priced) + " " + information.price
                    textView.setTextColor(resources.getColor(R.color.orange, null))
                }
                information.price.length == 3 -> binding.price.let { textView ->
                    textView.text = getString(R.string.expensive) + " " + information.price
                    textView.setTextColor(resources.getColor(R.color.dark_yellow, null))
                }
                information.price.length == 4 -> binding.price.let { textView ->
                    textView.text = getString(R.string.highly_expensive) + " " + information.price
                    textView.setTextColor(resources.getColor(R.color.red, null))
                }
            }

            // Business status
            if (information.hours?.last() != null)
                if (information.hours.last().is_open_now)
                    binding.status.let { textView ->
                        textView.text = getString(R.string.open)
                        textView.setTextColor(resources.getColor(R.color.green, null))
                    }
                else
                    binding.status.let { textView ->
                        textView.text = getString(R.string.closed)
                        textView.setTextColor(resources.getColor(R.color.red, null))
                    }

            // Show Views
            binding.informationRecycler.visibility = View.VISIBLE
            binding.linearPhone.visibility = View.VISIBLE
            binding.linearPrice.visibility = View.VISIBLE
            binding.linearAddress.visibility = View.VISIBLE
            binding.linearStatus.visibility = View.VISIBLE
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}