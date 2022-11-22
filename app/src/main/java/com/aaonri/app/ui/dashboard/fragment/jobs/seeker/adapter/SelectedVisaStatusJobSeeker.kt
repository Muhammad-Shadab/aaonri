package com.aaonri.app.ui.dashboard.fragment.jobs.seeker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.seeker.model.AllActiveJobApplicabilityResponseItem
import com.aaonri.app.databinding.VisaStausItemBinding

class SelectedVisaStatusJobSeeker(private var selectedVisaStatusJobApplicability: ((value: List<AllActiveJobApplicabilityResponseItem>) -> Unit)) :
    RecyclerView.Adapter<SelectedVisaStatusJobSeeker.CustomViewHolder>() {

    private var data = listOf<AllActiveJobApplicabilityResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VisaStausItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        if (data[position].isSelected) {
            holder.binding.selectedCommunityText.text = data[position].applicability
            holder.binding.selectedCommunityText.visibility = View.VISIBLE
        } else {
            holder.binding.selectedCommunityText.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            data[position].isSelected = !data[position].isSelected
            selectedVisaStatusJobApplicability(data)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<AllActiveJobApplicabilityResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(val binding: VisaStausItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}