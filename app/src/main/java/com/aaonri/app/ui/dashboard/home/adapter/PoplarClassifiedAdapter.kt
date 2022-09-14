package com.aaonri.app.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.home.model.PoplarClassifiedResponseItem
import com.aaonri.app.databinding.ClassifiedCardItemsBinding
import com.bumptech.glide.Glide

class PoplarClassifiedAdapter(private var selectedServices: ((value: PoplarClassifiedResponseItem) -> Unit)) :
    RecyclerView.Adapter<PoplarClassifiedAdapter.ClassifiedViewHolder>() {

    private var data = listOf<PoplarClassifiedResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClassifiedCardItemsBinding.inflate(inflater, parent, false)
        return ClassifiedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            if (data[position].favorite) {
                like.visibility = View.VISIBLE
            }

            if (data[position].userAdsImages.isEmpty()) {
                classifiedPriceTv.text = "$" + data[position].askingPrice.toString()
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            } else {
                Glide.with(context)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}")
                    .into(classifiedItemIv)
                /*classifiedItemIv.load("https://www.aaonri.com/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}") {
                    placeholder(R.drawable.ic_image_placeholder)
                }*/
                classifiedPriceTv.text = "$" + data[position].askingPrice.toString()
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            }
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    @JvmName("setData1")
    fun setData(data: List<PoplarClassifiedResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class ClassifiedViewHolder(val binding: ClassifiedCardItemsBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}