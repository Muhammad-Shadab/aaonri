package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.AllActiveExperienceLevelResponseItem
import com.aaonri.app.data.jobs.recruiter.model.AllActiveIndustryResponseItem
import com.aaonri.app.data.jobs.recruiter.model.BillingTypeResponseItem
import com.aaonri.app.data.jobs.seeker.model.*
import com.aaonri.app.databinding.CategoryCardItem1Binding
import com.aaonri.app.databinding.CategoryCardItemBinding
import com.aaonri.app.databinding.CategoryItem2Binding

class ExperienceIndustriesBillingTypeAdapter :
    RecyclerView.Adapter<ExperienceIndustriesBillingTypeViewHolder>() {

    private var data = listOf<Any>()

    var itemClickListener: ((value: Any) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExperienceIndustriesBillingTypeViewHolder {
        return when (viewType) {
            R.layout.category_card_item -> ExperienceIndustriesBillingTypeViewHolder.ExperienceLevelViewHolder(
                CategoryCardItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.category_card_item1 ->
                ExperienceIndustriesBillingTypeViewHolder.IndustriesViewHolder(
                    CategoryCardItem1Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            R.layout.category_item2 -> ExperienceIndustriesBillingTypeViewHolder.BillingTypeViewHolder(
                CategoryItem2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(
        holder: ExperienceIndustriesBillingTypeViewHolder,
        position: Int
    ) {
        holder.itemClickListener = itemClickListener
        when (holder) {
            is ExperienceIndustriesBillingTypeViewHolder.ExperienceLevelViewHolder -> {
                if (data[position] is AllActiveExperienceLevelResponseItem) {
                    holder.bind(data[position] as AllActiveExperienceLevelResponseItem)
                }
            }
            is ExperienceIndustriesBillingTypeViewHolder.IndustriesViewHolder -> {
                if (data[position] is AllActiveIndustryResponseItem) {
                    holder.bind(data[position] as AllActiveIndustryResponseItem)
                }
            }
            is ExperienceIndustriesBillingTypeViewHolder.BillingTypeViewHolder -> {
                if (data[position] is BillingTypeResponseItem) {
                    holder.bind(data[position] as BillingTypeResponseItem)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setData1")
    fun setData(data: List<Any>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is AllActiveExperienceLevelResponseItem -> R.layout.category_card_item
            is AllActiveIndustryResponseItem -> R.layout.category_card_item1
            is BillingTypeResponseItem -> R.layout.category_item2
            else -> R.layout.fragment_all_job
        }
    }

}