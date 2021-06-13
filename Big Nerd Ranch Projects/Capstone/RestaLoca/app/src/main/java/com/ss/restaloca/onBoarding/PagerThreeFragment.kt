package com.ss.restaloca.onBoarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ss.restaloca.common.Constants.FINISH_SHARED_PREFERENCES_NAME
import com.ss.restaloca.common.Constants.IS_FINISHED_KEY
import com.ss.restaloca.databinding.FragmentPagerThreeBinding

class PagerThreeFragment : Fragment() {

    private var _binding: FragmentPagerThreeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagerThreeBinding.inflate(inflater, container, false)

        onClick()

        return binding.root
    }

    private fun onClick() {
        binding.finish.setOnClickListener {
            disableViewPager()
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToMapsFragment()
            findNavController().navigate(action)
        }
    }

    private fun disableViewPager() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            FINISH_SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_FINISHED_KEY, true)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}