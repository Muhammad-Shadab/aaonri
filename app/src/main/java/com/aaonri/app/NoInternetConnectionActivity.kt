package com.aaonri.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.aaonri.app.databinding.ActivityNoInternetConnectionBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.snackbar.Snackbar

class NoInternetConnectionActivity : AppCompatActivity() {
    var binding: ActivityNoInternetConnectionBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoInternetConnectionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        binding?.apply {
            retryBtn.setOnClickListener {
                if (Constant.isConnected(baseContext)) {
                    val isUserLogin =
                        baseContext.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }
                    if (isUserLogin == true) {
                        startActivity(Intent(baseContext, MainActivity::class.java))
                    } else {
                        startActivity(Intent(baseContext, LoginActivity::class.java))
                    }
                    finish()
                } else {
                    val snackbar =
                        Snackbar.make(mainll, "No Internet Connection", Snackbar.LENGTH_SHORT)
                            .setAction(
                                "OK"
                            ) { }
                    snackbar.show()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}