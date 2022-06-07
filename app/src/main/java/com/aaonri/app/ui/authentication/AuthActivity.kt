package com.aaonri.app.ui.authentication

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityAuthBinding


class AuthActivity : BaseActivity() {
    var authBinding: ActivityAuthBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(authBinding?.root)

        // hiding the status bar and making it transparent
        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT


        /*try {
            val uri: Uri? = intent?.data
            if (uri != null) {
                val params: List<String> = uri.pathSegments
                val id = params[params.size - 1]
                Toast.makeText(applicationContext, "id = $id", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }*/


    }
}