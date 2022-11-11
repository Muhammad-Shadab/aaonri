package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.JobApplicantResponseItem
import com.aaonri.app.databinding.JobApplicantItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class JobApplicantAdapter(private var selectedProfileJob: ((value: JobApplicantResponseItem) -> Unit)) :
    RecyclerView.Adapter<JobApplicantAdapter.JobApplicantViewHolder>() {

    private var data = listOf<JobApplicantResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobApplicantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JobApplicantItemBinding.inflate(inflater, parent, false)
        return JobApplicantViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: JobApplicantViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.apply {

            data[position].apply {
                jobSeekerNameTv.text = fullName
                if (location != null) {
                    locationTv.text = location
                }
                if (profileImage != null) {
                    context?.let {
                        Glide.with(it)
                            .load(profileImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .circleCrop()
                            .error(R.drawable.profile_pic_placeholder)
                            .into(profilePicIv)
                    }
                }
            }

            applicantCv.setOnClickListener {
                selectedProfileJob(data[position])
            }
        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<JobApplicantResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class JobApplicantViewHolder(val binding: JobApplicantItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}