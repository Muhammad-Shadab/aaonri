package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.AdvertiseActivePageResponse
import com.aaonri.app.data.advertise.model.AdvertiseActivePageResponseItem
import com.aaonri.app.data.advertise.model.AdvertisePageLocationResponseItem
import com.aaonri.app.data.advertise.model.ModuleTemplate
import com.aaonri.app.databinding.AdvertiseTemplateItemBinding
import com.bumptech.glide.Glide

class AdvertiseTemplateAdapter(private var selectedServices: ((value: AdvertiseActivePageResponseItem) -> Unit)) :
    RecyclerView.Adapter<AdvertiseTemplateAdapter.AdvertiseViewHolder>() {

    private var data = listOf<ModuleTemplate>()
    private var advertisePageList = listOf<AdvertiseActivePageResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertiseTemplateItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.apply {

            holder.itemView.setOnClickListener {
                selectedServices(advertisePageList[position])
                advertisePageList[position].isSelected = true
                notifyDataSetChanged()
            }

            if (advertisePageList[position].isSelected) {
                successTick.visibility = View.VISIBLE
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.selectedAdvertiseTemplateStroke
                    )
                }?.let { it2 ->
                    cardView.strokeColor = it2
                }
                selectedServices(advertisePageList[position])
            } else {
                successTick.visibility = View.GONE
            }


            //Glide.with(context).load(data[position].templateLink).into(imageView)

        }
    }

    @JvmName("setData1")
    fun setData(advertisePageList: List<AdvertiseActivePageResponseItem>) {
        this.advertisePageList = advertisePageList
        notifyDataSetChanged()
    }

    override fun getItemCount() = advertisePageList.size

    inner class AdvertiseViewHolder(val binding: AdvertiseTemplateItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}


class AdvertiseTemplateLocationAdapter(private var selectedServices: ((value: AdvertisePageLocationResponseItem) -> Unit)) :
    RecyclerView.Adapter<AdvertiseTemplateLocationAdapter.AdvertiseViewHolder>() {

    private var data = listOf<ModuleTemplate>()
    private var advertisePageList = listOf<AdvertisePageLocationResponseItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertiseTemplateItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.apply {

            holder.itemView.setOnClickListener {
                selectedServices(advertisePageList[position])
                advertisePageList[position].isSelected = true
                notifyDataSetChanged()
            }

            if (advertisePageList[position].isSelected) {
                successTick.visibility = View.VISIBLE
                selectedServices(advertisePageList[position])
                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.selectedAdvertiseTemplateStroke
                    )
                }?.let { it2 ->
                    cardView.strokeColor = it2
                }
            } else {
                successTick.visibility = View.GONE
            }


            //Glide.with(context).load(data[position].templateLink).into(imageView)

        }

    }

    @JvmName("setData1")
    fun setData(advertisePageList: List<AdvertisePageLocationResponseItem>) {
        this.advertisePageList = advertisePageList
        notifyDataSetChanged()
    }

    override fun getItemCount() = advertisePageList.size

    inner class AdvertiseViewHolder(val binding: AdvertiseTemplateItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}
