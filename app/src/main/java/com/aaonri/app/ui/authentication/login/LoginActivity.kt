package com.aaonri.app.ui.authentication.login

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityAuthBinding
import java.security.MessageDigest


class LoginActivity : BaseActivity() {
    var binding: ActivityAuthBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        printKeyHash()
        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

    }

    private fun printKeyHash() {
        try {
            val info =
                packageManager.getPackageInfo("com.aaonri.app.dev", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }

        } catch (e: Exception) {
            Log.i("printKeyHash", "printKeyHash: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}