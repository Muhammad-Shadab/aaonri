package com.aaonri.app.ui.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {
    var splashScreenBinding: FragmentSplashScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splashScreenBinding =
            FragmentSplashScreenBinding.inflate(inflater, container, false)

        var job: Job? = null
        job = MainScope().launch {
            delay(2000L)
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }

        return splashScreenBinding?.root
    }
}