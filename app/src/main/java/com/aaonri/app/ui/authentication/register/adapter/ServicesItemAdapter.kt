package com.aaonri.app.ui.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.databinding.ServicesGridItemBinding
import com.google.android.material.snackbar.Snackbar

class ServicesItemAdapter(
    private var selectedServices: ((value: MutableList<ServicesResponseItem>) -> Unit),
    private var isJobSelected: (value: Boolean) -> Unit
) :
    RecyclerView.Adapter<ServicesItemAdapter.CustomViewHolder>() {

    private var data = ArrayList<ServicesResponseItem>()

    var selectedCategoriesList: MutableList<ServicesResponseItem> = mutableListOf()
    var savedCategoriesList: MutableList<ServicesResponseItem> = mutableListOf()

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

                if (data[position].active) {
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

                /*if (data[position].isSelected) {
                    selectedServices(selectedCategoriesList)
                }*/
                selectedServices(selectedCategoriesList)

                itemView.setOnClickListener {
                    if (selectedCategoriesList.contains(data[position])) {
                        selectedCategoriesList.remove(data[position])
                    } else {
                        selectedCategoriesList.add(data[position])
                    }
                    if (data[position].active) {
                        data[position].isSelected = !data[position].isSelected
                        notifyDataSetChanged()
                        if (data[position].id == 17 && data[position].isSelected) {
                            isJobSelected(true)
                        } else {
                            isJobSelected(false)
                        }
                    } else {
                        Snackbar.make(
                            itemView,
                            "This service is currently unavailable",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    selectedServices(selectedCategoriesList)
                }

                if (data[position].active) {
                    if (data[position].isSelected) {
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

                /*itemView.setOnClickListener {
                    if (data[position].active) {
                        if (selectedCategoriesList.contains(data[position])) {
                            selectedCategoriesList.remove(data[position])
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
                            if (data[position].id == 3) {
                                isJobSelected(false)
                            }
                        } else {
                            if (data[position].id == 3) {
                                isJobSelected(true)
                            }
                            selectedCategoriesList.add(data[position])
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
                        }
                        selectedServices(selectedCategoriesList)
                    } else {
                        Snackbar.make(
                            itemView,
                            "This service is currently unavailable",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }*/
            }
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ServicesResponseItem>) {
        this.data = data as ArrayList<ServicesResponseItem>
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(
        val binding: ServicesGridItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root)
}



