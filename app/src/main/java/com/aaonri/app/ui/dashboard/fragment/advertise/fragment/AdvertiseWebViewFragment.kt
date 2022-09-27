package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.databinding.FragmentAdvertiseWebviewBinding


class AdvertiseWebViewFragment : Fragment() {
    var binding: FragmentAdvertiseWebviewBinding? = null
    val args: AdvertiseWebViewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvertiseWebviewBinding.inflate(inflater, container, false)
        binding?.apply {

            progressBar.visibility = View.VISIBLE
            startWebView(args.advertiseurl)

            closeWebViewBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            navigateBack.setOnClickListener {
                if (advertiseWebView.canGoBack()) {
                    advertiseWebView.goBack()
                } else {
                    findNavController().navigateUp()
                }
            }

            navigateForward.setOnClickListener {
                if (advertiseWebView.canGoForward()) {
                    advertiseWebView.goForward()
                }
            }

            requireActivity()
                .onBackPressedDispatcher
                .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (advertiseWebView.canGoBack()) {
                            advertiseWebView.goBack()
                        } else {
                            findNavController().navigateUp()
                        }
                    }
                })

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
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.advertiseWebView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                binding?.progressBar?.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                binding?.progressBar?.visibility = View.GONE
            }
        }

        binding?.advertiseWebView?.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}