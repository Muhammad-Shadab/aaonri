package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.advertise.model.AllAdvertiseResponse
import com.aaonri.app.data.advertise.model.AllAdvertiseResponseItem
import com.aaonri.app.databinding.AdvertisementItemBinding
import com.bumptech.glide.Glide

class AdvertiseAdapter(private var selectedServices: ((value: AllAdvertiseResponseItem) -> Unit)) :
    RecyclerView.Adapter<AdvertiseAdapter.AdvertiseViewHolder>() {

    private var data = listOf<AllAdvertiseResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertisementItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            advertiseNameTv.text = data[position].title
            advertiseLocationTv.text = data[position].fromDate

            //Glide.with(context).load(advertisemntImage).into(advertisemntImage)
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    @JvmName("setData1")
    fun setData(data: List<AllAdvertiseResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class AdvertiseViewHolder(val binding: AdvertisementItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}