package com.aaonri.app.ui.dashboard.fragment

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
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
/*//            shopWithUsWebView.getSettings().setLoadsImagesAutomatically(true)
            shopWithUsWebView.getSettings().setJavaScriptEnabled(true)
//            shopWithUsWebView.getSettings().setAllowContentAccess(true)

            shopWithUsWebView.loadUrl("http://aaonridevnew.aaonri.com/StartSelling")

            shopWithUsWebView.getSettings().setUseWideViewPort(true)
            shopWithUsWebView.getSettings().setLoadWithOverviewMode(true)
            shopWithUsWebView.getSettings().setDomStorageEnabled(true)
            shopWithUsWebView.clearView()
            shopWithUsWebView.setHorizontalScrollBarEnabled(false)
            shopWithUsWebView.getSettings().setAppCacheEnabled(true)
            shopWithUsWebView.getSettings().setDatabaseEnabled(true)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                shopWithUsWebView.getSettings().setDatabasePath(
                    "/data/data/" + context?.packageName.toString() + "/databases/"
                )
            }
            shopWithUsWebView.setVerticalScrollBarEnabled(false)
            shopWithUsWebView.getSettings().setBuiltInZoomControls(true)
            shopWithUsWebView.getSettings().setDisplayZoomControls(false)
            shopWithUsWebView.getSettings().setAllowFileAccess(true)
            shopWithUsWebView.getSettings().setPluginState(WebSettings.PluginState.OFF)
            shopWithUsWebView.setScrollbarFadingEnabled(false)
            shopWithUsWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
            shopWithUsWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR)
            shopWithUsWebView.setWebViewClient(WebViewClient())
            shopWithUsWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
            shopWithUsWebView.setInitialScale(1)

            shopWithUsWebView.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(request.url.toString())
                    }
                    return false
                }
            })
            shopWithUsWebView.setOnClickListener {
                findNavController().navigateUp()
            }*/
            startWebView("http://aaonridevnew.aaonri.com/StartSelling")

        }
        return  binding?.root
    }


    private fun startWebView(url: String) {
        val settings: WebSettings? = binding?.shopWithUsWebView?.getSettings()
        settings?.javaScriptEnabled = true
        binding?.shopWithUsWebView?.getSettings()?.setUseWideViewPort(true)
        binding?.shopWithUsWebView?.getSettings()?.setLoadWithOverviewMode(true)
        binding?.shopWithUsWebView?.getSettings()?.setDomStorageEnabled(true)
        binding?.shopWithUsWebView?.clearView()
        binding?.shopWithUsWebView?.setHorizontalScrollBarEnabled(false)
        binding?.shopWithUsWebView?.getSettings()?.setAppCacheEnabled(true)
        binding?.shopWithUsWebView?.getSettings()?.setDatabaseEnabled(true)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            binding?.shopWithUsWebView?.getSettings()?.setDatabasePath(
                "/data/data/" + context?.packageName.toString() + "/databases/"
            )
        }
        binding?.shopWithUsWebView?.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY)
        binding?.shopWithUsWebView?.getSettings()?.setBuiltInZoomControls(true)
        binding?.shopWithUsWebView?.getSettings()?.setUseWideViewPort(true)
        binding?.shopWithUsWebView?.getSettings()?.setLoadWithOverviewMode(true)
        binding?.progresShopping?.visibility = View.VISIBLE
        binding?.shopWithUsWebView?.setWebViewClient(object : WebViewClient() {
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
                Toast.makeText(context, "Error:$description", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        binding?.shopWithUsWebView?.loadUrl(url)
    }
}
