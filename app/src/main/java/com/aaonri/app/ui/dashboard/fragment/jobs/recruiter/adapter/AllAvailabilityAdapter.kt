package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.recruiter.model.AvailabilityResponseItem
import com.aaonri.app.databinding.CategoryCardItemBinding

class AllAvailabilityAdapter(private var selectedAvailability: ((value: AvailabilityResponseItem) -> Unit)) :
    RecyclerView.Adapter<AllAvailabilityAdapter.AllAvailabilityViewHolder>() {

    private var data = listOf<AvailabilityResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllAvailabilityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryCardItemBinding.inflate(inflater, parent, false)
        return AllAvailabilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllAvailabilityViewHolder, position: Int) {
        val context = holder.itemView.context


        holder.binding.apply {
            countryTv.text = data[position].availability

            countryTv.setOnClickListener {
                selectedAvailability(data[position])
            }

        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<AvailabilityResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class AllAvailabilityViewHolder(val binding: CategoryCardItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}