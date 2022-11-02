package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.jobs.recruiter.model.StateListResponseItem
import com.aaonri.app.databinding.CategoryCardItem1Binding

class StateAdapter(private var selectedState: ((value: StateListResponseItem) -> Unit)) :
    RecyclerView.Adapter<StateAdapter.StateViewHolder>() {

    private var data = listOf<StateListResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryCardItem1Binding.inflate(inflater, parent, false)
        return StateViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.apply {

            data[position].apply {
                countryTv.text = name
            }

            countryTv.setOnClickListener {
                selectedState(data[position])
            }

        }

    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<StateListResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class StateViewHolder(val binding: CategoryCardItem1Binding) :
        RecyclerView.ViewHolder(binding.root)
}