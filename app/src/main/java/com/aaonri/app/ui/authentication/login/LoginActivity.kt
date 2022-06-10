package com.aaonri.app.ui.authentication.login

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.aaonri.app.base.BaseActivity
import com.aaonri.app.databinding.ActivityAuthBinding


class LoginActivity : BaseActivity() {
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


        /*val uri: Uri? = intent.data
        if (uri != null){
            val path = uri.toString()
            Toast.makeText(this, "path = $path", Toast.LENGTH_SHORT).show()
        }
*/
    }
}