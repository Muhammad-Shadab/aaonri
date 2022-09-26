package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
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
                binding?.progresShopping?.visibility = View.VISIBLE
                advertiseWebView?.loadUrl(url)
            }

            // this will enable the javascript settings
            advertiseWebView?.settings?.javaScriptEnabled = true

           advertiseWebView?.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                    binding?.progresShopping?.visibility = View.GONE
                    view.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView, url: String) {

                    binding?.progresShopping?.visibility = View.GONE

                }

                override fun onReceivedError(
                    view: WebView,
                    errorCode: Int,
                    description: String,
                    failingUrl: String
                ) {

                    binding?.progresShopping?.visibility = View.GONE
                Toast.makeText(context, "Error:$description", Toast.LENGTH_SHORT)
                    .show()
                }
            }

            closeAdvertiseBtn.setOnClickListener {
                findNavController().navigateUp()
            }

        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}