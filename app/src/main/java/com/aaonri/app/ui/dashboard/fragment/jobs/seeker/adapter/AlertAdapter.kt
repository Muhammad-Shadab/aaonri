package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.seeker.model.JobAlert
import com.aaonri.app.databinding.JobAlertItemBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class AlertAdapter(private var selectedJobAlert: ((isUpdateBtnClicked: Boolean, isDeleteBtnClicked: Boolean, value: JobAlert) -> Unit)) :
    RecyclerView.Adapter<AlertAdapter.CustomViewHolder>() {

    private var data = listOf<JobAlert>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JobAlertItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.binding.apply {

            data[position].apply {

                val random = if (expectedSalary != "string") expectedSalary.toDouble() else 0

                val df = DecimalFormat("#,###.00")
                df.roundingMode = RoundingMode.DOWN
                val roundOff = df.format(random)

                jobAlertTitleTv.text = jobAlertName
                jobAlertSubTitleTv.text = role
                jobAddressTv.text = location
                jobExperienceTv.text = workExp

                jobCategoriesTv.text = "$roundOff - $workStatus"
            }

            jobCv.setOnClickListener {
                selectedJobAlert(false, false, data[position])
            }

            updateJobAlertBtn.setOnClickListener {
                selectedJobAlert(true, false, data[position])
            }

            deleteJobAlertBtn.setOnClickListener {
                selectedJobAlert(false, true, data[position])
            }

        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<JobAlert>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: JobAlertItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}