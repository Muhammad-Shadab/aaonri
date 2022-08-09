package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.advertise.model.ModuleTemplate
import com.aaonri.app.databinding.AdvertiseTemplateItemBinding
import com.bumptech.glide.Glide

class AdvertiseTemplateAdapter(private var selectedServices: ((value: String) -> Unit)) :
    RecyclerView.Adapter<AdvertiseTemplateAdapter.AdvertiseViewHolder>() {

    private var data = listOf<ModuleTemplate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertiseTemplateItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.apply {
            holder.itemView.setOnClickListener {

                notifyDataSetChanged()
            }
            Glide.with(context).load(data[position].templateLink).into(imageView)

        }
    }

    @JvmName("setData1")
    fun setData(data: List<ModuleTemplate>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class AdvertiseViewHolder(val binding: AdvertiseTemplateItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}


class AdvertiseTemplateLocationAdapter(private var selectedServices: ((value: String) -> Unit)) :
    RecyclerView.Adapter<AdvertiseTemplateLocationAdapter.AdvertiseViewHolder>() {

    private var data = listOf<ModuleTemplate>()
    var rowIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertiseTemplateItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.itemView.setOnClickListener {
            rowIndex = position
            notifyDataSetChanged()
        }
        holder.binding.apply {
            Glide.with(context).load(data[position].templateLink).into(imageView)

            if (rowIndex == position) {
                successTick.visibility = View.VISIBLE
                selectedServices(data[position].moduleName)
            } else {
                successTick.visibility = View.GONE
            }
        }

    }

    @JvmName("setData1")
    fun setData(data: List<ModuleTemplate>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class AdvertiseViewHolder(val binding: AdvertiseTemplateItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}