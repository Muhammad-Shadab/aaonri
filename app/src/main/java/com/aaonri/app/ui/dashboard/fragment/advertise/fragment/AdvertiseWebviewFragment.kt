package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.databinding.FragmentAdvertiseWebviewBinding


class AdvertiseWebviewFragment : Fragment() {
    var binding: FragmentAdvertiseWebviewBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdvertiseWebviewBinding.inflate(inflater, container, false)
        var url = arguments?.let { AdvertiseWebviewFragmentArgs.fromBundle(it).advertiseurl }
        binding?.apply {
            advertiseWebView?.webViewClient = WebViewClient()

            // this will load the url of the website
            if (url != null) {
                advertiseWebView?.loadUrl(url)
            }

            // this will enable the javascript settings
            advertiseWebView?.settings?.javaScriptEnabled = true

            // if you want to enable zoom feature
            advertiseWebView?.settings?.setSupportZoom(true)
            closeAdvertiseBtn.setOnClickListener {
                findNavController().navigateUp()
            }

        }
        return binding?.root
    }

}