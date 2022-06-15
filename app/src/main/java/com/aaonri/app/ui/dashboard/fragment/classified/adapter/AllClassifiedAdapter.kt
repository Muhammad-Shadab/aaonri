package com.aaonri.app.ui.dashboard.fragment.classified.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.AllUserAdsClassifiedResponse
import com.aaonri.app.data.classified.model.UserAd
import com.aaonri.app.databinding.ClassifiedCardItemsBinding

class AllClassifiedAdapter : RecyclerView.Adapter<AllClassifiedAdapter.ClassifiedViewHolder>() {

    private var data = listOf<UserAd>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClassifiedCardItemsBinding.inflate(inflater, parent, false)
        return ClassifiedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            classifiedItemIv.load(R.drawable.sofa)
            classifiedPriceTv.text = "$" + data[position].askingPrice.toString()
            classifiedDescTv.text = data[position].adTitle
            locationClassifiedTv.text = data[position].adLocation
            popularTv.visibility = if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
        }
        holder.itemView.setOnClickListener {
            /*navigation?.navigate(R.id.action_classifiedScreenFragment_to_classifiedDetailsFragment)*/
        }
    }

    @JvmName("setData1")
    fun setData(data: List<UserAd>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class ClassifiedViewHolder(val binding: ClassifiedCardItemsBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )


}