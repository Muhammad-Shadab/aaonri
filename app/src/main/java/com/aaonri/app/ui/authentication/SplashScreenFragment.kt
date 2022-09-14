package com.aaonri.app.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.MainActivity
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentSplashScreenBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {
    var binding: FragmentSplashScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentSplashScreenBinding.inflate(inflater, container, false)


        val isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }

        var job: Job? = null
        job = MainScope().launch {
            //delay(2000L)
            if (isUserLogin == true) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}