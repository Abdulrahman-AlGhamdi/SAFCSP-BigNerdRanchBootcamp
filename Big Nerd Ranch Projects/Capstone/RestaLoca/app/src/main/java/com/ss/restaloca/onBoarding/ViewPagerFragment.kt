package com.ss.restaloca.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ss.restaloca.databinding.FragmentViewPagerBinding

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        // Initialize views and variables
        init()

        return binding.root
    }

    private fun init() {
        val fragments = arrayListOf(PagerOneFragment(), PagerTwoFragment(), PagerThreeFragment())
        val adapter = ViewPagerAdapter(fragments, requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.pagerIndicator, binding.viewPager) { _, _ ->

        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}