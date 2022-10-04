package com.aaonri.app.ui.dashboard.fragment.homescreen_filter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.home.model.InterestResponseItem
import com.aaonri.app.databinding.ServicesGridItemBinding
import com.aaonri.app.utils.PreferenceManager

class SearchFilterModuleAdapter(
    private var selectedServices: ((value: InterestResponseItem) -> Unit),
) :
    RecyclerView.Adapter<SearchFilterModuleAdapter.CustomViewHolder>() {

    private var data = ArrayList<InterestResponseItem>()

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

                var jobId = 0
                var advertiseId = 0
                var classifiedId = 0
                var eventId = 0
                var immigrationId = 0
                var shopWithUsId = 0
                var astrologyId = 0
                var businessNeedId = 0
                var communityConnectId = 0
                var foundationAndDonationId = 0
                var homeNeedId = 0
                var legalServicesId = 0
                var matrimonyId = 0
                var medicalCareId = 0
                var realStateId = 0
                var sports = 0
                var studentService = 0
                var travelStay = 0

                if (BuildConfig.FLAVOR == "dev") {
                    advertiseId = 27
                    classifiedId = 2
                    eventId = 8
                    immigrationId = 3
                    jobId = 17
                    shopWithUsId = 22
                    astrologyId = 4
                    businessNeedId = 26
                    communityConnectId = 10
                    foundationAndDonationId = 13
                    homeNeedId = 25
                    legalServicesId = 18
                    matrimonyId = 19
                    medicalCareId = 20
                    realStateId = 21
                    sports = 5
                    studentService = 16
                    travelStay = 24
                } else {
                    advertiseId = 19
                    classifiedId = 1
                    eventId = 2
                    immigrationId = 4
                    jobId = 3
                    shopWithUsId = 15
                    astrologyId = 5
                    businessNeedId = 18
                    communityConnectId = 8
                    foundationAndDonationId = 9
                    homeNeedId = 17
                    legalServicesId = 11
                    matrimonyId = 12
                    medicalCareId = 13
                    realStateId = 14
                    sports = 6
                    studentService = 10
                    travelStay = 16
                }

                /*if (userInterestService.contains("$advertiseId")) {
                    //"Advertise With Us"
                    if (data[position].id == advertiseId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$classifiedId")) {
                    //"Classifieds"
                    if (data[position].id == classifiedId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$eventId")) {
                    //"Events"
                    if (data[position].id == eventId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$immigrationId")) {
                    //"Immigration"
                    if (data[position].id == immigrationId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$jobId")) {
                    //"Jobs"
                    if (data[position].id == jobId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$shopWithUsId")) {
                    //"Shop With Us"
                    if (data[position].id == shopWithUsId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$astrologyId")) {
                    //"Astrology"
                    if (data[position].id == astrologyId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$businessNeedId")) {
                    //"Business Needs"
                    if (data[position].id == businessNeedId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$communityConnectId")) {
                    //"Community Connect"
                    if (data[position].id == communityConnectId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$foundationAndDonationId")) {
                    //"Foundation & Donations"
                    if (data[position].id == foundationAndDonationId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$homeNeedId")) {
                    //"Home Needs"
                    if (data[position].id == homeNeedId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$legalServicesId")) {
                    //"Legal Services"
                    if (data[position].id == legalServicesId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$matrimonyId")) {
                    //"Matrimony & Weddings"
                    if (data[position].id == matrimonyId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$medicalCareId")) {
                    //"Medical Care"
                    if (data[position].id == medicalCareId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$realStateId")) {
                    //"Real Estate"
                    if (data[position].id == realStateId) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$sports")) {
                    //"Sports"
                    if (data[position].id == sports) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$studentService")) {
                    //"Student Services"
                    if (data[position].id == studentService) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }
                if (userInterestService.contains("$travelStay")) {
                    //"Travel and Stay"
                    if (data[position].id == travelStay) {
                        addSelectedDataList(data[position])
                        data[position].isSelected = true
                    }
                }*/

                servicesGridTv.text = data[position].interestDesc

                when (data[position].interestDesc) {
                    "Classifieds" -> {
                        servicesGridIv.load(R.drawable.ic_classified)
                        servicesGridIv.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.blueBtnColor
                            )
                        )
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

                /*if (data[position].id == jobId) {
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
                }*/

                itemView.setOnClickListener {
                    selectedServices(data[position])
                    context?.let { it1 -> PreferenceManager<Int>(it1) }
                        ?.set("selectedSearchModule", position)
                    notifyDataSetChanged()
                }

                if (context?.let { PreferenceManager<Int>(it)["selectedSearchModule", -1] } == position) {
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

                /*if (data[position].active) {
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

                if (data[position].id == jobId) {
                    if (data[position].isSelected) {
                        isJobSelected(true)
                    } else {
                        isJobSelected(false)
                    }
                }*/

            }
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<InterestResponseItem>) {
        this.data = data as ArrayList<InterestResponseItem>
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(
        val binding: ServicesGridItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root)
}
