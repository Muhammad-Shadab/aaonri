package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.seeker.model.JobProfile
import com.aaonri.app.databinding.RecruiterConsultantProfileItemBinding

class ConsultantProfileAdapter :
    RecyclerView.Adapter<ConsultantProfileAdapter.ConsultantViewHolder>() {

    private var data = listOf<JobProfile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecruiterConsultantProfileItemBinding.inflate(inflater, parent, false)
        return ConsultantViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ConsultantViewHolder, position: Int) {

        holder.binding.apply {

            data[position].apply {
                consultantNameTv.text = firstName + " " + lastName
                consultantGmailTv.text = contactEmailId
                consultantMobileTv.text = phoneNo.replace("""[(,), ]""".toRegex(), "")
                    .replace("-", "").replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
                consultantAddressTv.text = location
            }

        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<JobProfile>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ConsultantViewHolder(val binding: RecruiterConsultantProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}