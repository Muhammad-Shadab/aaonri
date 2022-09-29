package com.aaonri.app.ui.dashboard.fragment

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
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentShopScreenBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class ShopScreenFragment : Fragment() {
    var binding: FragmentShopScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }

        binding = FragmentShopScreenBinding.inflate(inflater, container, false)
        binding?.apply {
            progresShopping.visibility = View.VISIBLE

            profilePicCv.setOnClickListener {
                if (isUserLogin == false) {
                    activity?.finish()
                }
            }

            context?.let {
                Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).centerCrop().error(R.drawable.profile_pic_placeholder).into(profilePicIv)
            }
            navigateBack.setOnClickListener {
                if (shopWithUsWebView.canGoBack()) {
                    shopWithUsWebView.goBack()
                }
            }
            startWebView("${BuildConfig.BASE_URL.replace(":8444", "")}/StartSelling")
            requireActivity()
                .onBackPressedDispatcher
                .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (binding?.shopWithUsWebView?.canGoBack() == true)
                            binding?.shopWithUsWebView?.goBack()
                    }
                })
        }
        return binding?.root
    }

    private fun startWebView(url: String) {
        val settings: WebSettings? = binding?.shopWithUsWebView?.getSettings()
        settings?.javaScriptEnabled = true
        binding?.shopWithUsWebView?.settings?.useWideViewPort = true
        binding?.shopWithUsWebView?.settings?.loadWithOverviewMode = true
        binding?.shopWithUsWebView?.settings?.domStorageEnabled = true
        binding?.shopWithUsWebView?.clearView()
        binding?.shopWithUsWebView?.isHorizontalScrollBarEnabled = false
        binding?.shopWithUsWebView?.settings?.setAppCacheEnabled(true)
        binding?.shopWithUsWebView?.settings?.databaseEnabled = true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            binding?.shopWithUsWebView?.settings?.databasePath =
                "/data/data/" + context?.packageName.toString() + "/databases/"
        }
        binding?.shopWithUsWebView?.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
        binding?.shopWithUsWebView?.settings?.useWideViewPort = true
        binding?.shopWithUsWebView?.settings?.loadWithOverviewMode = true
        binding?.progresShopping?.visibility = View.VISIBLE
        binding?.shopWithUsWebView?.webViewClient = object : WebViewClient() {
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
            }
        }


        binding?.shopWithUsWebView?.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
