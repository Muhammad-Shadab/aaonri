package com.aaonri.app.data.classified.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponse
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem
import com.aaonri.app.data.classified.model.ClassifiedCategorySubCategoryX
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.databinding.FilterCardViewItemBinding

class ClassifiedCategoryAdapter(private var selectedCategory: ((value: ClassifiedCategoryResponseItem) -> Unit)) :
    RecyclerView.Adapter<ClassifiedCategoryAdapter.ClassifiedCategoryViewHolder>() {

    var data = mutableListOf<ClassifiedCategoryResponseItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassifiedCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterCardViewItemBinding.inflate(inflater, parent, false)
        return ClassifiedCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedCategoryViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.countryTv.text = data[position].title
        holder.binding.countryTv.setOnClickListener {
            selectedCategory(data[position])
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    fun setData(data: MutableList<ClassifiedCategoryResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ClassifiedCategoryViewHolder(val binding: FilterCardViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}


class ClassifiedSubCategoryAdapter(private var deleteFilter: ((value: ClassifiedSubcategoryX) -> Unit)) :
    RecyclerView.Adapter<ClassifiedSubCategoryAdapter.ClassifiedCategoryViewHolder>() {

    var subCate = mutableListOf<ClassifiedSubcategoryX>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassifiedCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilterCardViewItemBinding.inflate(inflater, parent, false)
        return ClassifiedCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedCategoryViewHolder, position: Int) {
        holder.binding.countryTv.text = subCate[position].title
        holder.binding.countryTv.setOnClickListener {
            deleteFilter(subCate[position])
        }
    }

    override fun getItemCount() = subCate.size

    fun setSubCategoryData(subCategoryX: MutableList<ClassifiedSubcategoryX>) {
        this.subCate = subCategoryX
        notifyDataSetChanged()
    }

    inner class ClassifiedCategoryViewHolder(val binding: FilterCardViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
