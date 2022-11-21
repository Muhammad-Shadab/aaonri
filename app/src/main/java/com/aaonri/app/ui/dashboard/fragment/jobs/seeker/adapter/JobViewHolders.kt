package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.data.jobs.seeker.model.*
import com.aaonri.app.databinding.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

sealed class JobViewHolders(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: Any, position: Int) -> Unit)? = null

    var viewResumeOrCoverLetterBtnListener: ((isViewCoverLetterClicked: Boolean, item: JobProfile) -> Unit)? =
        null

    class AllActiveJobsViewHolders(private val binding: AllPostedJobsItemBinding) :
        JobViewHolders(binding) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(allJobsResponseItem: AllJobsResponseItem) {
            binding.apply {
                var random = 0.0
                if (allJobsResponseItem.salaryRange != null) {
                    if (allJobsResponseItem.salaryRange.isNotEmpty()) {
                        random =
                            if (allJobsResponseItem.salaryRange != "string" && allJobsResponseItem.salaryRange != "60k" && allJobsResponseItem.salaryRange != "@#$%^&") allJobsResponseItem.salaryRange.toDouble() else 0.0
                    }
                }
                val df = DecimalFormat("#,###.00")
                df.roundingMode = RoundingMode.DOWN
                val roundoff = df.format(random)

                jobTitleTv.text = allJobsResponseItem.title
                jobCompanyNameTv.text = allJobsResponseItem.company
                experienceTv.text = allJobsResponseItem.experienceLevel
                locationTv.text =
                    "${allJobsResponseItem.country}, ${allJobsResponseItem.state}, ${allJobsResponseItem.city}"
                jobPriceTv.text = "$roundoff - ${allJobsResponseItem.jobType}"
                dateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .parse(allJobsResponseItem.createdOn.split("T")[0])
                    )
                jobViewTv.text = allJobsResponseItem.viewCount.toString()
                jobApplicationTv.text = allJobsResponseItem.applyCount.toString()

                jobApplyBtn.setOnClickListener {
                    itemClickListener?.invoke(it, allJobsResponseItem, adapterPosition)
                }

                jobCv.setOnClickListener {
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
                    itemClickListener?.invoke(
                        it,
                        allActiveJobApplicabilityResponseItem,
                        adapterPosition
                    )
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
                    itemClickListener?.invoke(
                        it,
                        activeJobAvailabilityResponseItem,
                        adapterPosition
                    )
                }
            }
        }
    }

    class MyJobProfileViewHolder(private val binding: MyJobProfileItemBinding) :
        JobViewHolders(binding) {
        @SuppressLint("SetTextI18n")
        fun bind(userJobProfileResponseItem: JobProfile) {
            binding.apply {
                jobSeekerNameTv.text =
                    "${userJobProfileResponseItem.firstName} ${userJobProfileResponseItem.lastName}"
                jobSeekerGmailTv.text = userJobProfileResponseItem.contactEmailId
                jobSeekerMobileTv.text =
                    userJobProfileResponseItem.phoneNo.replace("""[(,), ]""".toRegex(), "")
                        .replace("-", "")
                        .replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
                jobSeekerAddressTv.text = userJobProfileResponseItem.location

                updateProfileBtn.setOnClickListener {
                    itemClickListener?.invoke(it, userJobProfileResponseItem, adapterPosition)
                }

                viewResumeTv.setOnClickListener {
                    viewResumeOrCoverLetterBtnListener?.invoke(
                        false,
                        userJobProfileResponseItem
                    )
                }

                viewletterTv.setOnClickListener {
                    viewResumeOrCoverLetterBtnListener?.invoke(
                        true,
                        userJobProfileResponseItem
                    )
                }


            }
        }
    }


}