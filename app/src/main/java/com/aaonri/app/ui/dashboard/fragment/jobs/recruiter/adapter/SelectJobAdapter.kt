package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.JobType
import com.aaonri.app.databinding.CommunityItemBinding

class SelectJobAdapter(private var selectedJobList: ((value: List<JobType>) -> Unit)) :
    RecyclerView.Adapter<SelectJobAdapter.CustomViewHolder>() {

    private var data = listOf<JobType>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommunityItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.communityText.text = data[position].name

        holder.itemView.setOnClickListener {
            data[position].isSelected = !data[position].isSelected
            selectedJobList(data)
            notifyDataSetChanged()
        }

        if (data[position].isSelected) {
            holder.binding.communityText.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.blueBtnColor
                )
            )
            holder.binding.communityText.setTextColor(context.getColor(R.color.white))
        } else {
            holder.binding.communityText.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.binding.communityText.setTextColor(context.getColor(R.color.textViewColor))
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<JobType>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: CommunityItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}