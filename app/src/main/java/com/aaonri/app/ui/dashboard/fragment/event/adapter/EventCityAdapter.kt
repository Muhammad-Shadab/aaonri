package com.aaonri.app.ui.dashboard.fragment.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.databinding.CategoryCardItemBinding

class EventCityAdapter(private var selectedCategory: ((value: String) -> Unit)) :
    RecyclerView.Adapter<EventCityAdapter.ClassifiedCategoryViewHolder>() {

    var data = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassifiedCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryCardItemBinding.inflate(inflater, parent, false)
        return ClassifiedCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedCategoryViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.countryTv.text = data[position]
        holder.binding.countryTv.setTextColor(context.getColor(R.color.black))
        holder.binding.countryTv.setOnClickListener {
            selectedCategory(data[position])
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    @JvmName("setData1")
    fun setData(data: MutableList<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ClassifiedCategoryViewHolder(val binding: CategoryCardItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

