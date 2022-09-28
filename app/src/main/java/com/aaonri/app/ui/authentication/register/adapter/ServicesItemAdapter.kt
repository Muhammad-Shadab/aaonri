package com.aaonri.app.ui.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.databinding.ServicesGridItemBinding
import com.google.android.material.snackbar.Snackbar

class ServicesItemAdapter(
    private var selectedServices: ((value: MutableList<ServicesResponseItem>) -> Unit),
    private var isJobSelected: (value: Boolean) -> Unit,
    private var selected: (value: String) -> Unit
) :
    RecyclerView.Adapter<ServicesItemAdapter.CustomViewHolder>() {

    private var data = ArrayList<ServicesResponseItem>()
    private var isProfileScreen: Boolean = false
    var selectedCategoriesList: MutableList<ServicesResponseItem> = mutableListOf()
    var userInterestService = ""
    //var savedCategoriesList: MutableList<ServicesResponseItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ServicesGridItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.apply {
            binding.apply {
                if (userInterestService.contains("27")) {
                    //"Advertise With Us"
                    if (data[position].id == 27) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("2")) {
                    //"Classifieds"
                    if (data[position].id == 2) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("8")) {
                    //"Events"
                    if (data[position].id == 8) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("3")) {
                    //"Immigration"
                    if (data[position].id == 3) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("17")) {
                    //"Jobs"
                    if (data[position].id == 17) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("22")) {
                    //"Shop With Us"
                    if (data[position].id == 22) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("4")) {
                    //"Astrology"
                    if (data[position].id == 4) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("26")) {
                    //"Business Needs"
                    if (data[position].id == 26) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("10")) {
                    //"Community Connect"
                    if (data[position].id == 10) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("13")) {
                    //"Foundation & Donations"
                    if (data[position].id == 13) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("25")) {
                    //"Home Needs"
                    if (data[position].id == 25) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("18")) {
                    //"Legal Services"
                    if (data[position].id == 18) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("19")) {
                    //"Matrimony & Weddings"
                    if (data[position].id == 19) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("20")) {
                    //"Medical Care"
                    if (data[position].id == 20) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("21")) {
                    //"Real Estate"
                    if (data[position].id == 21) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("5")) {
                    //"Sports"
                    if (data[position].id == 5) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("16")) {
                    //"Student Services"
                    if (data[position].id == 16) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("24")) {
                    //"Travel and Stay"
                    if (data[position].id == 24) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }

                servicesGridTv.text = data[position].interestDesc
                when (data[position].interestDesc) {
                    "Classifieds" -> {
                        servicesGridIv.load(R.drawable.ic_classified)
                    }
                    "Events" -> {
                        servicesGridIv.load(R.drawable.ic_event)
                    }
                    "Jobs" -> {
                        servicesGridIv.load(R.drawable.ic_job)
                    }
                    "Immigration" -> {
                        servicesGridIv.load(R.drawable.ic_immigration)
                    }
                    "Astrology" -> {
                        servicesGridIv.load(R.drawable.ic_astrology)
                    }
                    "Sports" -> {
                        servicesGridIv.load(R.drawable.ic_sports)
                    }
                    "Community Connect" -> {
                        servicesGridIv.load(R.drawable.ic_community)
                    }
                    "Foundation & Donations" -> {
                        servicesGridIv.load(R.drawable.ic_donation)
                    }
                    "Student Services" -> {
                        servicesGridIv.load(R.drawable.ic_education)
                    }
                    "Legal Services" -> {
                        servicesGridIv.load(R.drawable.ic_legal_service)
                    }
                    "Matrimony & Weddings" -> {
                        servicesGridIv.load(R.drawable.ic_matrimony)
                    }
                    "Medical Care" -> {
                        servicesGridIv.load(R.drawable.ic_medical)
                    }
                    "Real Estate" -> {
                        servicesGridIv.load(R.drawable.ic_real_state)
                    }
                    "Shop With Us" -> {
                        servicesGridIv.load(R.drawable.ic_shop)
                    }
                    "Travel and Stay" -> {
                        servicesGridIv.load(R.drawable.ic_travel)
                    }
                    "Home Needs" -> {
                        servicesGridIv.load(R.drawable.ic_home_need)
                    }
                    "Business Needs" -> {
                        servicesGridIv.load(R.drawable.ic_business_need)
                    }
                    "Advertise With Us" -> {
                        servicesGridIv.load(R.drawable.ic_advertise)
                    }
                }

                if (data[position].id == 17) {
                    data[position].active = false
                }

                if (data[position].active && !isProfileScreen) {
                    servicesGridIv.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.blueBtnColor
                        )
                    )
                    servicesGridIv.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.serviceCardLightBlue
                        )
                    )
                } else {
                    servicesGridIv.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.disableServiceColor
                        )
                    )
                }

                itemView.setOnClickListener {
                    if (data[position].active) {
                        selected(data[position].interestDesc)
                        addSelectedDataList(data[position])
                        data[position].isSelected = !data[position].isSelected
                        selectedServices(selectedCategoriesList)
                        notifyDataSetChanged()
                    } else {
                        Snackbar.make(
                            itemView,
                            "This service is currently unavailable",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                if (data[position].active) {
                    if (data[position].isSelected && !isProfileScreen) {
                        if (!selectedCategoriesList.contains(data[position])) {
                            addSelectedDataList(data[position])
                        }
                        selectedServices(selectedCategoriesList)
                        servicesGridIv.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                        servicesGridIv.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.blueBtnColor
                            )
                        )
                    } else {
                        servicesGridIv.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.blueBtnColor
                            )
                        )
                        servicesGridIv.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.serviceCardLightBlue
                            )
                        )
                    }
                }

                if (userInterestService.length == position) {
                    userInterestService = ""
                }

                if (data[position].id == 17) {
                    if (data[position].isSelected) {
                        isJobSelected(true)
                    } else {
                        isJobSelected(false)
                    }
                }

            }
        }
    }

    private fun addSelectedDataList(servicesResponseItem: ServicesResponseItem) {
        if (selectedCategoriesList.contains(servicesResponseItem)) {
            selectedCategoriesList.remove(servicesResponseItem)
        } else {
            selectedCategoriesList.add(servicesResponseItem)
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ServicesResponseItem>, isProfileScreen: Boolean) {
        this.data = data as ArrayList<ServicesResponseItem>
        this.isProfileScreen = isProfileScreen
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedServicesList(value: String) {
        userInterestService = value
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(
        val binding: ServicesGridItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root)
}



