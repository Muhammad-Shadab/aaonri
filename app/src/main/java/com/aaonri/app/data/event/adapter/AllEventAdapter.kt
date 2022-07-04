package com.aaonri.app.data.event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.event.model.Event
import com.aaonri.app.databinding.EventItemBinding
import com.bumptech.glide.Glide

class AllEventAdapter(private var selectedServices: ((value: Event) -> Unit)) :
    RecyclerView.Adapter<AllEventAdapter.EventViewHolder>() {

    private var data = listOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventItemBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            if (data[position].images.isNotEmpty()) {
                Glide.with(context).load(data[position].images[0].imagePath)
                eventName.text = data[position].title
            } else {

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