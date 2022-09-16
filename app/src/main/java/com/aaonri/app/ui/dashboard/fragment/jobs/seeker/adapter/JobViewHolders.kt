package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.data.jobs.seeker.model.ActiveJobAvailabilityResponseItem
import com.aaonri.app.data.jobs.seeker.model.AllActiveJobApplicabilityResponseItem
import com.aaonri.app.data.jobs.seeker.model.AllJobsResponseItem
import com.aaonri.app.data.jobs.seeker.model.ExperienceLevelResponseItem
import com.aaonri.app.databinding.AllPostedJobsItemBinding
import com.aaonri.app.databinding.CategoryCardItem1Binding
import com.aaonri.app.databinding.CategoryCardItemBinding
import com.aaonri.app.databinding.CategoryItem2Binding

sealed class JobViewHolders(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: Any, position: Int) -> Unit)? = null

    class AllActiveJobsViewHolders(private val binding: AllPostedJobsItemBinding) :
        JobViewHolders(binding) {

        @SuppressLint("SetTextI18n")
        fun bind(allJobsResponseItem: AllJobsResponseItem) {
            binding.apply {
                jobTitleTv.text = allJobsResponseItem.title
                jobCompanyNameTv.text = allJobsResponseItem.company
                experienceTv.text = allJobsResponseItem.experienceLevel
                locationTv.text =
                    "${allJobsResponseItem.country}, ${allJobsResponseItem.state}, ${allJobsResponseItem.city}"
                jobPriceTv.text =
                    "${allJobsResponseItem.salaryRange} - ${allJobsResponseItem.jobType}"
                dateTv.text = allJobsResponseItem.createdOn
                jobViewTv.text = allJobsResponseItem.viewCount.toString()
                jobApplicationTv.text = allJobsResponseItem.applyCount.toString()

                root.setOnClickListener {
                    itemClickListener?.invoke(it, allJobsResponseItem, adapterPosition)
                }
            }
        }
    }

    class ExperienceCategoriesViewHolder(private val binding: CategoryCardItemBinding) :
        JobViewHolders(binding) {
        fun bind(experienceLevelResponseItem: ExperienceLevelResponseItem) {
            binding.apply {
                countryTv.text = experienceLevelResponseItem.experienceLevel

                root.setOnClickListener {
                    itemClickListener?.invoke(it, experienceLevelResponseItem, adapterPosition)
                }
            }
        }
    }

    class JobApplicabilityViewHolder(private val binding: CategoryCardItem1Binding) :
        JobViewHolders(binding) {
        fun bind(allActiveJobApplicabilityResponseItem: AllActiveJobApplicabilityResponseItem) {
            binding.apply {
                countryTv.text = allActiveJobApplicabilityResponseItem.applicability

                root.setOnClickListener {
                    itemClickListener?.invoke(it, allActiveJobApplicabilityResponseItem, adapterPosition)
                }
            }
        }
    }

    class JobAvailabilityViewHolder(private val binding: CategoryItem2Binding) :
        JobViewHolders(binding) {
        fun bind(activeJobAvailabilityResponseItem: ActiveJobAvailabilityResponseItem) {
            binding.apply {
                categoryTv.text = activeJobAvailabilityResponseItem.availability

                root.setOnClickListener {
                    itemClickListener?.invoke(it, activeJobAvailabilityResponseItem, adapterPosition)
                }
            }
        }
    }


}