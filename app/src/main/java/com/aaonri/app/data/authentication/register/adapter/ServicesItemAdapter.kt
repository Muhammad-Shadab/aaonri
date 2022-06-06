package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
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
    private var selectedServices: ((value: List<ServicesResponseItem>) -> Unit),
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.apply {
            binding.apply {

                servicesGridTv.text = data[position].interestDesc
                when (data[position].id) {
                    1 -> {
                        servicesGridIv.load(R.drawable.ic_classified)
                    }
                    2 -> {
                        servicesGridIv.load(R.drawable.ic_event)
                    }
                    3 -> {
                        servicesGridIv.load(R.drawable.ic_job)
                    }
                    4 -> {
                        servicesGridIv.load(R.drawable.ic_immigration)
                    }
                    5 -> {
                        servicesGridIv.load(R.drawable.ic_astrology)
                    }
                    6 -> {
                        servicesGridIv.load(R.drawable.ic_sports)
                    }
                    8 -> {
                        servicesGridIv.load(R.drawable.ic_community)
                    }
                    9 -> {
                        servicesGridIv.load(R.drawable.ic_donation)
                    }
                    10 -> {
                        servicesGridIv.load(R.drawable.ic_education)
                    }
                    11 -> {
                        servicesGridIv.load(R.drawable.ic_legal_service)
                    }
                    12 -> {
                        servicesGridIv.load(R.drawable.ic_matrimony)
                    }
                    13 -> {
                        servicesGridIv.load(R.drawable.ic_medical)
                    }
                    14 -> {
                        servicesGridIv.load(R.drawable.ic_real_state)
                    }
                    15 -> {
                        servicesGridIv.load(R.drawable.ic_shop)
                    }
                    16 -> {
                        servicesGridIv.load(R.drawable.ic_travel)
                    }
                    17 -> {
                        servicesGridIv.load(R.drawable.ic_home_need)
                    }
                    18 -> {
                        servicesGridIv.load(R.drawable.ic_business_need)
                    }
                    19 -> {
                        servicesGridIv.load(R.drawable.ic_advertise)
                    }
                }

                if (data[position].active) {
                    if (selectedCategoriesList.contains(data[position]) || savedCategoriesList.contains(
                            data[position]
                        )
                    ) {
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
                }
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



