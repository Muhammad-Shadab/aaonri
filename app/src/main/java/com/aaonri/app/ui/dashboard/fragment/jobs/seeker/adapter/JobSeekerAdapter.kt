package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.*
import com.aaonri.app.databinding.*

class JobSeekerAdapter : RecyclerView.Adapter<JobViewHolders>() {

    private var data = listOf<Any>()

    var itemClickListener: ((view: View, item: Any, position: Int) -> Unit)? = null

    var viewResumeOrCoverLetterBtnListener: ((isViewCoverLetterClicked: Boolean, item: UserJobProfileResponseItem) -> Unit)? =
        null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolders {
        return when (viewType) {
            R.layout.fragment_all_job -> JobViewHolders.AllActiveJobsViewHolders(
                AllPostedJobsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.category_card_item -> JobViewHolders.ExperienceCategoriesViewHolder(
                CategoryCardItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.category_card_item1 -> JobViewHolders.JobApplicabilityViewHolder(
                CategoryCardItem1Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.category_item2 -> JobViewHolders.JobAvailabilityViewHolder(
                CategoryItem2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.my_job_profile_item -> JobViewHolders.MyJobProfileViewHolder(
                MyJobProfileItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: JobViewHolders, position: Int) {
        holder.itemClickListener = itemClickListener
        holder.viewResumeOrCoverLetterBtnListener = viewResumeOrCoverLetterBtnListener

        when (holder) {
            is JobViewHolders.AllActiveJobsViewHolders -> {
                if (data[position] is AllJobsResponseItem) {
                    holder.bind(data[position] as AllJobsResponseItem)
                }
            }
            is JobViewHolders.ExperienceCategoriesViewHolder -> {
                if (data[position] is ExperienceLevelResponseItem) {
                    holder.bind(data[position] as ExperienceLevelResponseItem)
                }
            }
            is JobViewHolders.JobApplicabilityViewHolder -> {
                if (data[position] is AllActiveJobApplicabilityResponseItem) {
                    holder.bind(data[position] as AllActiveJobApplicabilityResponseItem)
                }
            }
            is JobViewHolders.JobAvailabilityViewHolder -> {
                if (data[position] is ActiveJobAvailabilityResponseItem) {
                    holder.bind(data[position] as ActiveJobAvailabilityResponseItem)
                }
            }
            is JobViewHolders.MyJobProfileViewHolder -> {
                if (data[position] is UserJobProfileResponseItem) {
                    holder.bind(data[position] as UserJobProfileResponseItem)
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
            is AllJobsResponseItem -> R.layout.fragment_all_job
            is ExperienceLevelResponseItem -> R.layout.category_card_item
            is AllActiveJobApplicabilityResponseItem -> R.layout.category_card_item1
            is ActiveJobAvailabilityResponseItem -> R.layout.category_item2
            is UserJobProfileResponseItem -> R.layout.my_job_profile_item
            else -> R.layout.fragment_all_job
        }
    }
}