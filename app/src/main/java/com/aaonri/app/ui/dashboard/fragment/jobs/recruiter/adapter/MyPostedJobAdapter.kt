package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.recruiter.model.JobDetails
import com.aaonri.app.databinding.MyPostedJobsItemBinding
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

class MyPostedJobAdapter(private var selectedJob: ((isEditBtnClicked: Boolean, isActivateBtnClicked: Boolean, isDeactivateBtnClicked: Boolean, isJobApplicantClick: Boolean, isJobCardClicked: Boolean, value: JobDetails) -> Unit)) :
    RecyclerView.Adapter<MyPostedJobAdapter.MyPostedJobViewHolder>() {

    private var data = listOf<JobDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostedJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyPostedJobsItemBinding.inflate(inflater, parent, false)
        return MyPostedJobViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyPostedJobViewHolder, position: Int) {
        val context = holder.itemView.context

        var userLocation = ""

        if (data[position].city.isNotEmpty()) {
            userLocation = data[position].city + ", "
        }
        if (data[position].state.isNotEmpty()) {
            userLocation += data[position].state + ", "
        }
        if (data[position].country.isNotEmpty()) {
            userLocation += data[position].country
        }


        holder.binding.apply {

            data[position].apply {

                val random = if (salaryRange != "string") salaryRange.toDouble() else 0
                val df = DecimalFormat("#,###.00")
                df.roundingMode = RoundingMode.DOWN
                val roundoff = df.format(random)

                jobTitleTv.text = title
                jobCompanyNameTv.text = company
                locationTv.text = userLocation
                experienceTv.text = experienceLevel
                postedDateTv.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .parse(createdOn.split("T")[0])
                    )

                jobViewCountTv.text = viewCount.toString()
                jobApplicationCountTv.text = applyCount.toString()
                jobPriceTv.text = "$roundoff"

                /** In flexbox layout we need to add text through programmatically in order to visible the text **/
                inactiveTv.text = "Inactive"

                if (isActive) {
                    deactivateBtn.visibility = View.VISIBLE
                    editBtn.visibility = View.VISIBLE
                    inactiveTv.visibility = View.GONE
                    activateJobBtn.visibility = View.GONE
                } else {
                    activateJobBtn.visibility = View.VISIBLE
                    inactiveTv.visibility = View.VISIBLE
                    editBtn.visibility = View.GONE
                    deactivateBtn.visibility = View.GONE
                }

                editBtn.setOnClickListener {
                    selectedJob(true, false, false, false, false, data[position])
                }

                activateJobBtn.setOnClickListener {
                    selectedJob(false, true, false, false, false, data[position])
                }

                deactivateBtn.setOnClickListener {
                    selectedJob(false, false, true, false, false, data[position])
                }

                jobCard.setOnClickListener {
                    selectedJob(false, false, false, false, true, data[position])
                }

                jobApplicantLl.setOnClickListener {
                    selectedJob(false, false, false, true, false, data[position])
                }

                userLocation = ""

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

    inner class MyPostedJobViewHolder(val binding: MyPostedJobsItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}