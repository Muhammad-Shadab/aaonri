package com.aaonri.app.ui.dashboard.fragment.event.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.event.model.Event
import com.aaonri.app.databinding.EventItemBinding
import com.bumptech.glide.Glide
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AllEventAdapter(private var selectedServices: ((value: Event) -> Unit)) :
    RecyclerView.Adapter<AllEventAdapter.EventViewHolder>() {

    private var data = listOf<Event>()
    private var startDate: String? = null
    private var startTimeOfEvent: String? = null
    private var endTimeOfEvent: String? = null
    private var timeZone: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {

            try {
                startDate = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .parse(data[position].startDate.split("T")[0])
                )
                startTimeOfEvent = LocalTime.parse(data[position].startTime)
                    .format(DateTimeFormatter.ofPattern("h:mma"))
                endTimeOfEvent = LocalTime.parse(data[position].endTime)
                    .format(DateTimeFormatter.ofPattern("h:mma"))
                timeZone = data[position].timeZone
                eventTiming.text =
                    "Starts From $startDate, $startTimeOfEvent - $endTimeOfEvent  $timeZone"
            } catch (e: Exception) {

            }

            if (data[position].images.isNotEmpty()) {
                if (data[position].images[0].imagePath.contains(".cover") || data[position].images[0].imagePath.contains(
                        ".first"
                    ) || data[position].images[0].imagePath.contains(".second") || data[position].images[0].imagePath.contains(
                        ".third"
                    )
                ) {
                    data[position].images.forEachIndexed { index, userAdsImage ->
                        if (userAdsImage.imagePath.contains(".cover")) {
                            val image =
                                "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${data[position].images[index].imagePath}"
                            placeholder.visibility = View.GONE
                            Glide.with(context).load(image)
                                .into(eventImageView)
                        }
                    }
                } else {
                    val image =
                        "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${data[position].images[0].imagePath}"
                    placeholder.visibility = View.GONE
                    Glide.with(context).load(image)
                        .into(eventImageView)
                }
                eventName.text = data[position].title

                totalVisiting.text = data[position].totalVisiting.toString()
                totalFavourite.text = data[position].totalFavourite.toString()
                try {
                    eventLocationZip.text =
                        if (data[position].city.isNotEmpty()) "${data[position].city}" else "" + (if (data[position].zipCode.isNotEmpty() && data[position].city.isNotEmpty()) "-" else "") + if (data[position].zipCode.isNotEmpty()) "${data[position].zipCode}" else ""
                } catch (e: Exception) {

                }
                if (data[position].fee > 0) {
                    val df = DecimalFormat("#,###.00")
                    df.roundingMode = RoundingMode.DOWN
                    val roundoff = df.format(data[position].fee)
                    eventFee.text = "$$roundoff"
                } else {
                    eventFee.text = "FREE"
                }

            } else {
                placeholder.visibility = View.VISIBLE
                eventName.text = data[position].title
                totalVisiting.text = data[position].totalVisiting.toString()
                totalFavourite.text = data[position].totalFavourite.toString()
                try {
                    eventLocationZip.text =
                        if (data[position].city.isNotEmpty()) "${data[position].city}" else "" + (if (data[position].zipCode.isNotEmpty() && data[position].city.isNotEmpty()) "-" else "") + if (data[position].zipCode.isNotEmpty()) "${data[position].zipCode}" else ""
                } catch (e: Exception) {

                }
                if (data[position].fee > 0) {
                    eventFee.text = "$" + data[position].fee.toString()
                } else {
                    eventFee.text = "FREE"
                }
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




