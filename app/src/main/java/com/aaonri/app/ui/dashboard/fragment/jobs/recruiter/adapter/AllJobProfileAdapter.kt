package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.recruiter.model.AllJobProfileResponseItem
import com.aaonri.app.data.jobs.recruiter.model.AllTalentResponse
import com.aaonri.app.data.jobs.recruiter.model.JobProfile
import com.aaonri.app.databinding.RecruiterAllTalentsItemBinding
import com.google.android.flexbox.FlexboxLayoutManager

class AllJobProfileAdapter(private var selectedJobProfile: ((value: JobProfile) -> Unit)) :
    RecyclerView.Adapter<AllJobProfileAdapter.AllJobProfileViewHolder>() {

    var recruiterJobKeySkillsAdapter: RecruiterJobKeySkillsAdapter? = null

    private var data = listOf<JobProfile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllJobProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecruiterAllTalentsItemBinding.inflate(inflater, parent, false)
        return AllJobProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllJobProfileViewHolder, position: Int) {
        val context = holder.itemView.context

        recruiterJobKeySkillsAdapter = RecruiterJobKeySkillsAdapter()

        holder.binding.apply {
            jobSeekerNameTv.text = data[position].firstName + " " + data[position].lastName
            jobSeekerRole.text = data[position].title
            locationTv.text = data[position].location
            experienceTv.text = data[position].experience

            recruiterJobKeySkillsAdapter?.setData(
                data[position].skillSet.split(",").toTypedArray().toList()
            )
            skillsRv.layoutManager = FlexboxLayoutManager(context)
            skillsRv.adapter = recruiterJobKeySkillsAdapter

            viewProfileBtn.setOnClickListener {
                selectedJobProfile(data[position])
            }
            profileCard.setOnClickListener {
                selectedJobProfile(data[position])
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

    inner class AllJobProfileViewHolder(val binding: RecruiterAllTalentsItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}