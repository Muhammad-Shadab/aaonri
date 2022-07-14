package com.aaonri.app.ui.authentication.login

import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityAuthBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.math.log


class LoginActivity : BaseActivity() {
    var authBinding: ActivityAuthBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(authBinding?.root)
         printKeyHash()
        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT


        /*val uri: Uri? = intent.data
        if (uri != null){
            val path = uri.toString()
            Toast.makeText(this, "path = $path", Toast.LENGTH_SHORT).show()
        }
*/
    }

    private fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo("com.aaonri.app",PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEYHASH",Base64.encodeToString(md.digest(),Base64.DEFAULT))
            }

        }catch (e: Exception)
        {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }


}