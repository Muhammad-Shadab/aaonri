package com.aaonri.app.utils

import android.content.Context
import android.net.ConnectivityManager

object Constant {

    const val BASE_URL = "https://aaonri.com"

    const val IS_USER_LOGIN = "IS_USER_LOGIN"
    const val PROFILE_USER = "PROFILE_USER"
    const val USER_EMAIL = "USER_EMAIL"
    const val USER_ZIP_CODE = "USER_ZIP_CODE"
    const val USER_CITY = "USER_CITY"
    const val USER_STATE = "USER_STATE"
    const val ORIGIN_COUNTRY = "ORIGIN_COUNTRY"

    // Gmail Login
    const val GMAIL_FIRST_NAME = "GMAIL_FIRST_NAME"
    const val GMAIL_LAST_NAME = "GMAIL_LAST_NAME"

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}