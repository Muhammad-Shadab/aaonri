package com.aaonri.app.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.databinding.CategoryCardItemBinding

class ModuleCategoryAdapter(private var selectedCategory: ((value: ServicesResponseItem) -> Unit)) :
    RecyclerView.Adapter<ModuleCategoryAdapter.ModuleCategoryViewHolder>() {

    var data = mutableListOf<ServicesResponseItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModuleCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryCardItemBinding.inflate(inflater, parent, false)
        return ModuleCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleCategoryViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.countryTv.text = data[position].interestDesc
        holder.binding.countryTv.setOnClickListener {
            selectedCategory(data[position])
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    fun setData(data: List<ServicesResponseItem>) {
        this.data = data as MutableList<ServicesResponseItem>
        notifyDataSetChanged()
    }

    inner class ModuleCategoryViewHolder(val binding: CategoryCardItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}