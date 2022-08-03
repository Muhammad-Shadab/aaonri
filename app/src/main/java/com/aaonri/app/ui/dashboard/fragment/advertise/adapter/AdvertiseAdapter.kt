package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.AdvertisementItemBinding

class AdvertiseAdapter(private var selectedServices: ((value: String) -> Unit)) :
    RecyclerView.Adapter<AdvertiseAdapter.AdvertiseViewHolder>() {

    private var data = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertisementItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            advertiseNameTv.text = data[position]
        }
    }

    @JvmName("setData1")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class AdvertiseViewHolder(val binding: AdvertisementItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}