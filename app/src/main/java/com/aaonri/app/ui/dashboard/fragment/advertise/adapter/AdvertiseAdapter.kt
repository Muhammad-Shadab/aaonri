package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.advertise.model.AllAdvertiseResponseItem
import com.aaonri.app.databinding.AdvertisementItemBinding
import com.bumptech.glide.Glide
import java.time.format.DateTimeFormatter

class AdvertiseAdapter(private var selectedServices: ((value: AllAdvertiseResponseItem, isMoreMenuBtnClicked: Boolean) -> Unit)) :
    RecyclerView.Adapter<AdvertiseAdapter.AdvertiseViewHolder>() {

    private var data = listOf<AllAdvertiseResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertisementItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            advertiseNameTv.text = data[position].advertisementDetails.adTitle
            advertiseLocationTv.text = data[position].advertisementDetails.location
            postedOnDate.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data[position].fromDate.split("T").get(0))
            )
            validUpToDate.text = DateTimeFormatter.ofPattern("MM-dd-yyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data[position].toDate.split("T").get(0))
            )
            advertiseLinkTv.text = data[position].advertisementDetails.url
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${data[position].advertisementDetails.adImage}")
                    .into(advertisementImage)
            }

            //Glide.with(context).load(advertisemntImage).into(advertisemntImage)
            advertiseItemCard.setOnClickListener {
                selectedServices(data[position], false)
            }

            moreClassifiedOption.setOnClickListener {
                selectedServices(data[position], true)
            }
        }

    }

    @JvmName("setData1")
    fun setData(data: MutableList<AllAdvertiseResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class AdvertiseViewHolder(val binding: AdvertisementItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}