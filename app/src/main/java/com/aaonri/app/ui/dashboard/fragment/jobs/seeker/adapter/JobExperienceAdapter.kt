package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.ExperienceLevelResponseItem
import com.aaonri.app.databinding.JobExperienceItemBinding

class JobExperienceAdapter(private var selectedExperience: ((value: String) -> Unit)) :
    RecyclerView.Adapter<JobExperienceAdapter.CustomViewHolder>() {

    private var data = listOf<ExperienceLevelResponseItem>()
    var selectedExperienceValue = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JobExperienceItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.experienceTv.text = data[position].experienceLevel

        if (data[position].experienceLevel == selectedExperienceValue) {
            selectedExperience(data[position].experienceLevel)
            holder.binding.experienceTv.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.blueBtnColor
                )
            )
            holder.binding.experienceTv.setTextColor(context.getColor(R.color.white))
        } else {
            holder.binding.experienceTv.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.binding.experienceTv.setTextColor(context.getColor(R.color.textViewColor))
        }

        holder.itemView.setOnClickListener {
            if (selectedExperienceValue == data[position].experienceLevel) {
                selectedExperienceValue = ""
                selectedExperience("")
            } else {
                selectedExperienceValue = data[position].experienceLevel
            }
            notifyDataSetChanged()
        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ExperienceLevelResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setSelectedExperienceData(value: String) {
        this.selectedExperienceValue = value
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: JobExperienceItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}