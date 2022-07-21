package com.aaonri.app.utils.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.aaonri.app.R
import com.aaonri.app.ui.dashboard.fragment.HomeScreenFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.qualifiers.ApplicationContext

class ConnectivityReceiver : BroadcastReceiver() {
    var snackbar: Snackbar? = null
    override fun onReceive(context: Context, arg1: Intent) {
        val isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        if (!isConnected) {
            /*snackbar = Snackbar.make(, "You are offline", Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
            snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar?.show()*/
        } else {

        }
    }
}



