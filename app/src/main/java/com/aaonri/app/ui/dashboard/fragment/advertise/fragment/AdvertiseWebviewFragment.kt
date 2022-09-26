package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
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
            progresShopping?.visibility = View.VISIBLE

            if (url != null) {
                startWebView(url)
            }

            requireActivity()
                .onBackPressedDispatcher
                .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (binding?.advertiseWebView?.canGoBack() == true) {
                            binding?.advertiseWebView?.goBack()

                        }else{
                            findNavController().navigateUp()
                        }
                    }
                })

            closeAdvertiseBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            navigateBack.setOnClickListener{
                if (binding?.advertiseWebView?.canGoBack() == true) {
                    binding?.advertiseWebView?.goBack()

                }else{
                    findNavController().navigateUp()
                }
            }

        }
        return binding?.root
    }
    private fun startWebView(url: String) {
        val settings: WebSettings? = binding?.advertiseWebView?.getSettings()
        settings?.javaScriptEnabled = true
        binding?.advertiseWebView?.settings?.useWideViewPort = true
        binding?.advertiseWebView?.settings?.loadWithOverviewMode = true
        binding?.advertiseWebView?.settings?.domStorageEnabled = true
        binding?.advertiseWebView?.clearView()
        binding?.advertiseWebView?.isHorizontalScrollBarEnabled = false
        binding?.advertiseWebView?.settings?.setAppCacheEnabled(true)
        binding?.advertiseWebView?.settings?.databaseEnabled = true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            binding?.advertiseWebView?.settings?.databasePath =
                "/data/data/" + context?.packageName.toString() + "/databases/"
        }
        binding?.advertiseWebView?.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        binding?.advertiseWebView?.settings?.useWideViewPort = true
        binding?.advertiseWebView?.settings?.loadWithOverviewMode = true
        binding?.progresShopping?.visibility = View.VISIBLE
        binding?.advertiseWebView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
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
//                Toast.makeText(context, "Error:$description", Toast.LENGTH_SHORT)
//                    .show()
            }
        }


        binding?.advertiseWebView?.loadUrl(url)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}