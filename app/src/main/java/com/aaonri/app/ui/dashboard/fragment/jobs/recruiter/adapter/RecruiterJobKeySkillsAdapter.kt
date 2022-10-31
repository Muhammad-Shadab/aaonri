package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.JobKeySkillItemBinding

class RecruiterJobKeySkillsAdapter :
    RecyclerView.Adapter<RecruiterJobKeySkillsAdapter.KeySkillViewHolder>() {

    private var data = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeySkillViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JobKeySkillItemBinding.inflate(inflater, parent, false)
        return KeySkillViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: KeySkillViewHolder,
        position: Int
    ) {
        val context = holder.itemView.context

        holder.binding.apply {
            keySkillTv.text = data[position]
        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class KeySkillViewHolder(val binding: JobKeySkillItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}