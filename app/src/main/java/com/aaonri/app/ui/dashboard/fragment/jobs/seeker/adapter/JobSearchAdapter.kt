package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.recruiter.model.JobDetails
import com.aaonri.app.databinding.AllPostedJobsItemBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

class JobSearchAdapter(private var selectedJobDetails: ((isJobApplyBtnClicked: Boolean, value: JobDetails) -> Unit)) :
    RecyclerView.Adapter<JobSearchAdapter.CustomViewHolder>() {

    private var data = listOf<JobDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AllPostedJobsItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.binding.apply {
            var random = 0.0
            if (data[position].salaryRange != null) {
                if (data[position].salaryRange?.isNotEmpty() == true) {
                    random =
                        if (data[position].salaryRange != "string" && data[position].salaryRange != "60k" && data[position].salaryRange != "@#$%^&") data[position].salaryRange?.toDouble()!! else 0.0
                }
            }
            val df = DecimalFormat("#,###.00")
            df.roundingMode = RoundingMode.DOWN
            val roundoff = df.format(random)

            jobTitleTv.text = data[position].title
            jobCompanyNameTv.text = data[position].company
            experienceTv.text = data[position].experienceLevel
            locationTv.text =
                "${data[position].country}, ${data[position].state}, ${data[position].city}"
            jobPriceTv.text = "$roundoff - ${data[position].jobType}"
            dateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                .format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .parse(data[position].createdOn.split("T")[0])
                )
            jobViewTv.text = data[position].viewCount.toString()
            jobApplicationTv.text = data[position].applyCount.toString()

            jobApplyBtn.setOnClickListener {
                selectedJobDetails(true, data[position])
            }

            jobCv.setOnClickListener {
                selectedJobDetails(false, data[position])
            }
        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<JobDetails>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: AllPostedJobsItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}