package com.aaonri.app.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.net.ConnectivityManager
import java.text.ParseException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object Constant {

    const val BASE_URL = "https://aaonri.com"

    const val IS_USER_LOGIN = "IS_USER_LOGIN"
    const val PROFILE_USER = "PROFILE_USER"
    const val USER_EMAIL = "USER_EMAIL"
    const val USER_ZIP_CODE = "USER_ZIP_CODE"
    const val USER_CITY = "USER_CITY"
    const val USER_STATE = "USER_STATE"
    const val USER_INTERESTED_SERVICES = "USER_INTERESTED_SERVICES"
    const val ORIGIN_COUNTRY = "ORIGIN_COUNTRY"


    // Gmail Login
    const val GMAIL_FIRST_NAME = "GMAIL_FIRST_NAME"
    const val GMAIL_LAST_NAME = "GMAIL_LAST_NAME"

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


    fun convertIntoLocalTime(
        strTime: String, serverTimeZone: String, dateFormat: String?
    ): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat)
        val serverDateTime: ZonedDateTime = LocalDateTime.parse(strTime, formatter)
            .atZone(ZoneId.of(serverTimeZone))
        val deviceTime: ZonedDateTime = serverDateTime
            .withZoneSameInstant(ZoneId.systemDefault())
        return deviceTime.format(formatter)
    }


}