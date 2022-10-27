package com.aaonri.app.ui.dashboard.fragment.event.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.CategoryCardItemBinding

class EventTimeZoneAdapter(private var selectedCategory: ((value: String) -> Unit)) :
    RecyclerView.Adapter<EventTimeZoneAdapter.EventCategoryViewHolder>() {

    var data = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryCardItemBinding.inflate(inflater, parent, false)
        return EventCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventCategoryViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.countryTv.text = data[position]
        holder.binding.countryTv.setOnClickListener {
            selectedCategory(data[position])
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    fun setData(data: List<String>?) {
        this.data = data as MutableList<String>
        notifyDataSetChanged()
    }

    inner class EventCategoryViewHolder(val binding: CategoryCardItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}