package com.aaonri.app.ui.dashboard.fragment.jobs.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.data.jobs.seeker.model.AllJobsResponseItem
import com.aaonri.app.databinding.AllPostedJobsItemBinding

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
}