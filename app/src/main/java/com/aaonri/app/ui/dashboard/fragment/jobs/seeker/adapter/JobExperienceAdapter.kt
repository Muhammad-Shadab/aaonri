package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.authentication.register.model.CommunityAuth
import com.aaonri.app.data.jobs.seeker.model.ExperienceLevelResponseItem
import com.aaonri.app.databinding.CommunityItemBinding
import com.aaonri.app.databinding.JobExperienceItemBinding

class JobExperienceAdapter(private var selectedCommunity: ((value: ExperienceLevelResponseItem) -> Unit)? = null) :
    RecyclerView.Adapter<JobExperienceAdapter.CustomViewHolder>() {

    private var data = listOf<ExperienceLevelResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JobExperienceItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.experienceTv.text = data[position].experienceLevel

        /*if (selectedCommunityList.contains(data[position])) {
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
        }*/

        holder.itemView.setOnClickListener {

            /* if (selectedCommunityList.contains(data[position])) {
                 selectedCommunityList.remove(data[position])
                 holder.binding.communityText.setBackgroundColor(
                     ContextCompat.getColor(
                         context,
                         R.color.white
                     )
                 )
                 holder.binding.communityText.setTextColor(context.getColor(R.color.textViewColor))
             } else {
                 holder.binding.communityText.setBackgroundColor(
                     ContextCompat.getColor(
                         context,
                         R.color.blueBtnColor
                     )
                 )
                 holder.binding.communityText.setTextColor(context.getColor(R.color.white))
                 selectedCommunityList.add(data[position])
             }
             selectedCommunity?.let { it1 -> it1(selectedCommunityList) }*/
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ExperienceLevelResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: JobExperienceItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}