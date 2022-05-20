package com.aaonri.com.ui.authentication.register

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.aaonri.com.R
import com.aaonri.com.base.BaseActivity
import com.aaonri.com.databinding.ActivityRegistrationBinding
import com.aceinteract.android.stepper.StepperNavListener
import com.aceinteract.android.stepper.StepperNavigationView

class RegistrationActivity : BaseActivity() {
    lateinit var stepper: StepperNavigationView
    var registerBinding: ActivityRegistrationBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(registerBinding?.root)

        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_stepper) as NavHostFragment
        val navController = navHostFragment.navController

        stepper = findViewById<StepperNavigationView>(R.id.stepper)
        stepper.setupWithNavController(navController)*/

    }
}