package com.aaonri.app.ui.dashboard.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentShopScreenBinding

class ShopScreenFragment : Fragment() {
    var binding : FragmentShopScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShopScreenBinding.inflate(inflater, container, false)
        binding?.apply {
            shopWithUsWebView?.webViewClient = WebViewClient()

            // this will load the url of the website
            Toast.makeText(context, "${BuildConfig.BASE_URL.replace(":8444","")}/StartSelling", Toast.LENGTH_SHORT).show()
                shopWithUsWebView?.loadUrl("${BuildConfig.BASE_URL.replace(":8444","")}/StartSelling")


            // this will enable the javascript settings
            shopWithUsWebView.settings.javaScriptEnabled = true

            // if you want to enable zoom feature
            shopWithUsWebView.settings.setSupportZoom(true)
            shopWithUsWebView.setOnClickListener {
                findNavController().navigateUp()
            }

        }
        return  binding?.root
    }
}
