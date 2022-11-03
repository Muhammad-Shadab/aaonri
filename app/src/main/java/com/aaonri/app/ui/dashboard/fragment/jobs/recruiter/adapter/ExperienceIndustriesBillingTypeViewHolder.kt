package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.data.jobs.recruiter.model.AllActiveExperienceLevelResponseItem
import com.aaonri.app.data.jobs.recruiter.model.AllActiveIndustryResponseItem
import com.aaonri.app.data.jobs.recruiter.model.BillingTypeResponseItem
import com.aaonri.app.databinding.CategoryCardItem1Binding
import com.aaonri.app.databinding.CategoryCardItemBinding
import com.aaonri.app.databinding.CategoryItem2Binding

sealed class ExperienceIndustriesBillingTypeViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((item: Any) -> Unit)? = null

    class ExperienceLevelViewHolder(private val binding: CategoryCardItemBinding) :
        ExperienceIndustriesBillingTypeViewHolder(binding) {

        fun bind(allActiveExperienceLevelResponseItem: AllActiveExperienceLevelResponseItem) {
            binding.apply {
                countryTv.text = allActiveExperienceLevelResponseItem.experienceLevel

                countryTv.setOnClickListener {
                    itemClickListener?.invoke(
                        allActiveExperienceLevelResponseItem
                    )
                }
            }
        }
    }

    class IndustriesViewHolder(private val binding: CategoryCardItem1Binding) :
        ExperienceIndustriesBillingTypeViewHolder(binding) {

        fun bind(industriesResponseItem: AllActiveIndustryResponseItem) {
            binding.apply {
                countryTv.text = industriesResponseItem.industryType

                countryTv.setOnClickListener {
                    itemClickListener?.invoke(industriesResponseItem)
                }
            }
        }
    }

    class BillingTypeViewHolder(private val binding: CategoryItem2Binding) :
        ExperienceIndustriesBillingTypeViewHolder(binding) {

        fun bind(billingTypeResponseItem: BillingTypeResponseItem) {
            binding.apply {
                categoryTv.text = billingTypeResponseItem.billingType

                categoryTv.setOnClickListener {
                    itemClickListener?.invoke(billingTypeResponseItem)
                }
            }
        }
    }


}