package com.aaonri.app.data.event.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.event.model.Event
import com.aaonri.app.data.event.model.RecentEventResponseItem
import com.aaonri.app.databinding.EventItemBinding
import com.bumptech.glide.Glide
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class AllEventAdapter(private var selectedServices: ((value: Event) -> Unit)) :
    RecyclerView.Adapter<AllEventAdapter.EventViewHolder>() {

    private var data = listOf<Event>()
    private var startDate:String?=null
    private var startTimeOfEvent:String?=null
    private var endTimeOfEvent:String?=null
    private var timeZone:String?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            startDate = DateTimeFormatter.ofPattern("MMM dd").format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(data[position].startDate.split("T")[0]))
            startTimeOfEvent=LocalTime.parse(data[position].startTime).format(DateTimeFormatter.ofPattern("h:mma"))
            endTimeOfEvent=LocalTime.parse(data[position].endTime).format(DateTimeFormatter.ofPattern("h:mma"))
            timeZone=data[position].timeZone
            eventTiming.text= "$startDate, $startTimeOfEvent - $endTimeOfEvent  $timeZone"

            if (data[position].images.isNotEmpty()) {
                val image =
                    "https://www.aaonri.com/api/v1/common/eventFile/${data[position].images[0].imagePath}"
                Glide.with(context).load(image)
                    .into(eventImageView)
                eventName.text = data[position].title


                totalVisiting.text = data[position].totalVisiting.toString()
                totalFavourite.text = data[position].totalFavourite.toString()
                eventLocationZip.text = data[position].city + "-" + data[position].zipCode
                eventFee.text = "$"+data[position].fee.toString()

            } else {

                eventName.text = data[position].title
                totalVisiting.text = data[position].totalVisiting.toString()
                totalFavourite.text = data[position].totalFavourite.toString()
                eventLocationZip.text = data[position].city + "-" + data[position].zipCode
                eventFee.text = "$"+data[position].fee.toString()
            }
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    @JvmName("setData1")
    fun setData(data: List<Event>?) {
        if (data != null) {
            this.data = data
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class EventViewHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}


class RecentEventAdapter(private var selectedServices: ((value: RecentEventResponseItem) -> Unit)) :
    RecyclerView.Adapter<RecentEventAdapter.RecentEventHolder>() {

    private var data = listOf<RecentEventResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentEventHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(inflater, parent, false)
        return RecentEventHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentEventHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            if (data[position].images.isNotEmpty()) {
                val image =
                    "https://www.aaonri.com/api/v1/common/eventFile/${data[position].images[0].imagePath}"
                Glide.with(context).load(image)
                    .into(eventImageView)
                eventName.text = data[position].title
                totalVisiting.text = data[position].totalVisiting.toString()
                totalFavourite.text = data[position].totalFavourite.toString()
                eventLocationZip.text = data[position].city + "-" + data[position].zipCode
                eventFee.text ="$"+ data[position].fee.toString()

            } else {

                eventName.text = data[position].title
                totalVisiting.text = data[position].totalVisiting.toString()
                totalFavourite.text = data[position].totalFavourite.toString()
                eventLocationZip.text = data[position].city + "-" + data[position].zipCode
                eventFee.text = "$"+data[position].fee.toString()
            }
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    @JvmName("setData1")
    fun setData(data: List<RecentEventResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class RecentEventHolder(val binding: EventItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}