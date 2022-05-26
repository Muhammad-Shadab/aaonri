package com.aaonri.app.data.authentication.register.model

import com.aaonri.app.R

data class ServicesModel(
    val title: String,
    val image: Int
)

val listOfService = listOf(
    ServicesModel("Real Estate", R.drawable.ic_real_state),
    ServicesModel("Travel", R.drawable.ic_travel),
    ServicesModel("Immigration", R.drawable.ic_immigration),
    ServicesModel("Events", R.drawable.ic_event),
    ServicesModel("Community", R.drawable.ic_community),
    ServicesModel("Financial", R.drawable.ic_financial),
    ServicesModel("Buy & Sell", R.drawable.ic_buy_and_sale),
    ServicesModel("Jobs", R.drawable.ic_job),
    ServicesModel("Matrimony", R.drawable.ic_matrimony),
)
