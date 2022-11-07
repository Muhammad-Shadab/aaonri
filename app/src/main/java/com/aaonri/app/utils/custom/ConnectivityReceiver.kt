package com.aaonri.app.utils.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.aaonri.app.NoInternetConnectionActivity
import com.google.android.material.snackbar.Snackbar

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, arg1: Intent) {
        val isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        if (isConnected) {
            context.startActivity(Intent(context, NoInternetConnectionActivity::class.java))
        } else {

        }
    }
}



