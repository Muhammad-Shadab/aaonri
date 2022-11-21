package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.seeker.model.JobAlert
import com.aaonri.app.databinding.JobAlertItemBinding

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
                jobAlertTitleTv.text = jobAlertName
                jobAlertSubTitleTv.text = role
                jobAddressTv.text = location
                jobExperienceTv.text = workExp
                jobCategoriesTv.text = "$expectedSalary - $workStatus"
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