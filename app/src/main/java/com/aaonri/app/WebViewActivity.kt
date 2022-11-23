package com.aaonri.app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityWebViewBinding
import com.aaonri.app.utils.custom.ConnectivityReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : BaseActivity() {
    var binding: ActivityWebViewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        val connectivityReceiver = ConnectivityReceiver()
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        val url = intent.getStringExtra("url")
        val hideBottomBar = intent.getBooleanExtra("hideBottomBar", false)

        binding?.apply {

            progressBar.visibility = View.VISIBLE

            if (url != null) {
                startWebView(url)
            }

            if (hideBottomBar) {
                navigateBack.visibility = View.GONE
                navigateForward.visibility = View.GONE
                openInBrowser.visibility = View.GONE
            }

            closeWebViewBtn.setOnClickListener {
                finish()
            }

            navigateBack.setOnClickListener {
                if (advertiseWebView.canGoBack()) {
                    advertiseWebView.goBack()
                } else {
                    finish()
                }
            }

            navigateForward.setOnClickListener {
                if (advertiseWebView.canGoForward()) {
                    advertiseWebView.goForward()
                }
            }

            openInBrowser.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }

            /*requireActivity()
                .onBackPressedDispatcher
                .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (advertiseWebView.canGoBack()) {
                            advertiseWebView.goBack()
                        } else {
                            findNavController().navigateUp()
                        }
                    }
                })*/

        }

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
                "/data/data/" + applicationContext?.packageName.toString() + "/databases/"
        }
        binding?.advertiseWebView?.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        binding?.advertiseWebView?.settings?.useWideViewPort = true
        binding?.advertiseWebView?.settings?.loadWithOverviewMode = true
        binding?.progressBar?.visibility = View.VISIBLE

        binding?.advertiseWebView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.contains(".pdf")) {
                    view.loadUrl(url)
                }
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
}