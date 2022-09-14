package com.aaonri.app.ui.dashboard.fragment.jobs.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.jobs.seeker.model.AllJobsResponseItem
import com.aaonri.app.databinding.AllPostedJobsItemBinding

class JobAdapter : RecyclerView.Adapter<JobViewHolders>() {

    private var data = listOf<Any>()

    var itemClickListener: ((view: View, item: Any, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolders {
        return when (viewType) {
            R.layout.fragment_all_job -> JobViewHolders.AllActiveJobsViewHolders(
                AllPostedJobsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun onBindViewHolder(holder: JobViewHolders, position: Int) {
        holder.itemClickListener = itemClickListener
        when (holder) {
            is JobViewHolders.AllActiveJobsViewHolders -> {
                if (data[position] is AllJobsResponseItem) {
                    holder.bind(data[position] as AllJobsResponseItem)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setData1")
    fun setData(data: List<Any>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is AllJobsResponseItem -> R.layout.fragment_all_job
            else -> R.layout.fragment_all_job
        }
    }
}