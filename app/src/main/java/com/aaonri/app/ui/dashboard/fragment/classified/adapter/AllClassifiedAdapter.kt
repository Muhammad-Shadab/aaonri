package com.aaonri.app.ui.dashboard.fragment.classified.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.ClassifiedCardItemsBinding

class AllClassifiedAdapter : RecyclerView.Adapter<AllClassifiedAdapter.ClassifiedViewHolder>() {

    var data = mutableListOf("")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClassifiedCardItemsBinding.inflate(inflater, parent, false)
        return ClassifiedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            classifiedPriceTv.text = "$29.99"
            classifiedDescTv.text = "7 Ports USB3.0"
            locationClassifiedTv.text = "New York-08512"
        }
        holder.itemView.setOnClickListener {
            /*navigation?.navigate(R.id.action_classifiedScreenFragment_to_classifiedDetailsFragment)*/
        }
    }

    override fun getItemCount() = 20

    inner class ClassifiedViewHolder(val binding: ClassifiedCardItemsBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )


}