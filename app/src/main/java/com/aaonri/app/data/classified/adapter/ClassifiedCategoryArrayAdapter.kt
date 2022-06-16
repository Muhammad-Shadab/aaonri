package com.aaonri.app.data.classified.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem
import com.aaonri.app.data.classified.model.ClassifiedCategorySubCategory
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.databinding.SpinnerItemBinding


class ClassifiedCategoryArrayAdapter(
    context: Context,
    classifiedCategory: List<ClassifiedCategoryResponseItem>
) : ArrayAdapter<ClassifiedCategoryResponseItem>(context, 0, classifiedCategory) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val classifiedCategory = getItem(position)
        val inflater = LayoutInflater.from(parent.context)
        val binding = SpinnerItemBinding.inflate(inflater, parent, false)

        binding.spinnerText.text = classifiedCategory?.title

        return binding.root
    }
}

class ClassifiedSubCategoryArrayAdapter(
    context: Context,
    classifiedCategory: List<ClassifiedSubcategoryX>
) : ArrayAdapter<ClassifiedSubcategoryX>(context, 0, classifiedCategory) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val classifiedCategory = getItem(position)
        val inflater = LayoutInflater.from(parent.context)
        val binding = SpinnerItemBinding.inflate(inflater, parent, false)

        binding.spinnerText.text = classifiedCategory?.title

        return binding.root
    }
}
