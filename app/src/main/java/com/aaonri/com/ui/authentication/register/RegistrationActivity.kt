package com.aaonri.com.ui.authentication.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.aaonri.com.R
import com.aceinteract.android.stepper.StepperNavigationView

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_stepper) as NavHostFragment
        val navController = navHostFragment.navController

        val stepper = findViewById<StepperNavigationView>(R.id.stepper)
        stepper.setupWithNavController(navController)

    }
}