package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.advertise.model.AllAdvertiseResponseItem
import com.aaonri.app.databinding.AdvertiseTemplateItemBinding
import com.aaonri.app.databinding.AdvertisementItemBinding
import com.bumptech.glide.Glide

class AdvertiseTemplateAdapter(private var selectedServices: ((value: String) -> Unit)) :
    RecyclerView.Adapter<AdvertiseTemplateAdapter.AdvertiseViewHolder>() {

    private var data = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertiseTemplateItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            Glide.with(context).load(data[position]).into(imageView)
        }
        holder.itemView.setOnClickListener {
            //selectedServices(data[position])
        }
    }

    @JvmName("setData1")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class AdvertiseViewHolder(val binding: AdvertiseTemplateItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}