package com.aaonri.app.ui.dashboard.fragment.jobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.AdvertisementItemBinding
import com.aaonri.app.databinding.AllPostedJobsItemBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter

class JobAdapter(private var selectedServices: ((value: String) -> Unit)) :
    RecyclerView.Adapter<JobAdapter.JobAViewHolder>() {

    private var data = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AllPostedJobsItemBinding.inflate(inflater, parent, false)
        return JobAViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobAViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            nameTv.text = data[position]
        }
    }

    @JvmName("setData1")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class JobAViewHolder(val binding: AllPostedJobsItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}