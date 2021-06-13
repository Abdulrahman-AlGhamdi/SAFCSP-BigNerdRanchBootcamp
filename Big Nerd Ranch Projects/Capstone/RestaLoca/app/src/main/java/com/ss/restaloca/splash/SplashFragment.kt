package com.ss.restaloca.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ss.restaloca.common.Constants.FINISH_SHARED_PREFERENCES_NAME
import com.ss.restaloca.common.Constants.IS_FINISHED_KEY
import com.ss.restaloca.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        endSplash()

        return binding.root
    }

    private fun endSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (checkViewPager()) {
                val action = SplashFragmentDirections.actionSplashFragmentToMapsFragment()
                findNavController().navigate(action)
            } else {
                val action = SplashFragmentDirections.actionSplashFragmentToViewPagerFragment()
                findNavController().navigate(action)
            }
        }, 2000)
    }

    private fun checkViewPager(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences(
            FINISH_SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(IS_FINISHED_KEY, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}