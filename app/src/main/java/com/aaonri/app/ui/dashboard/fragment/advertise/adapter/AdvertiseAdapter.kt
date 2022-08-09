package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse
import com.aaonri.app.data.advertise.model.AllAdvertiseResponse
import com.aaonri.app.data.advertise.model.AllAdvertiseResponseItem
import com.aaonri.app.databinding.AdvertisementItemBinding
import com.bumptech.glide.Glide
import java.time.format.DateTimeFormatter

class AdvertiseAdapter(private var selectedServices: ((value: AllAdvertiseResponseItem) -> Unit)) :
    RecyclerView.Adapter<AdvertiseAdapter.AdvertiseViewHolder>() {

    private var data = listOf<AllAdvertiseResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertisementItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            advertiseNameTv.text = data[position].title
//            advertiseLocationTv.text  = data[position].advertisementDetails?.location
            advertiseDateTv.text = "From ${DateTimeFormatter.ofPattern("MMM dd,yyyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data[position]?.fromDate?.split("T")?.get(0))
            ) } To ${DateTimeFormatter.ofPattern("MMM dd,yyyy").format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .parse(data[position]?.toDate?.split("T")?.get(0))
            ) }"

            //Glide.with(context).load(advertisemntImage).into(advertisemntImage)
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
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