package com.aaonri.app.ui.dashboard.fragment.classified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.databinding.CategoryCardItemBinding

class ClassifiedSubCategoryAdapter(private var deleteFilter: ((value: ClassifiedSubcategoryX) -> Unit)) :
    RecyclerView.Adapter<ClassifiedSubCategoryAdapter.ClassifiedCategoryViewHolder>() {

    var subCate = mutableListOf<ClassifiedSubcategoryX>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassifiedCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryCardItemBinding.inflate(inflater, parent, false)
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

    inner class ClassifiedCategoryViewHolder(val binding: CategoryCardItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}