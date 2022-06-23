package com.aaonri.app.ui.dashboard.fragment.classified.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.Classified
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.databinding.ClassifiedCardItemsBinding
import com.squareup.picasso.Picasso

class AllClassifiedAdapter(private var selectedServices: ((value: UserAds) -> Unit)) :
    RecyclerView.Adapter<AllClassifiedAdapter.ClassifiedViewHolder>() {

    private var data = listOf<UserAds>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClassifiedCardItemsBinding.inflate(inflater, parent, false)
        return ClassifiedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            if (data[position].userAdsImages.isEmpty()) {
                classifiedPriceTv.text = "$" + data[position].askingPrice.toString()
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            } else {
                Picasso.get().load("https://www.aaonri.com/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}").into(classifiedItemIv)
                /*classifiedItemIv.load("https://www.aaonri.com/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}") {
                    placeholder(R.drawable.ic_image_placeholder)
                }*/
                classifiedPriceTv.text = "$" + data[position].askingPrice.toString()
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            }

            //classifiedPostDateTv.text = data[position].createdOn
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    @JvmName("setData1")
    fun setData(data: List<UserAds>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class ClassifiedViewHolder(val binding: ClassifiedCardItemsBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}


class FavoriteClassifiedAdapter(private var selectedServices: ((value: Classified) -> Unit)) :
    RecyclerView.Adapter<FavoriteClassifiedAdapter.ClassifiedViewHolder>() {

    private var data = listOf<Classified>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClassifiedCardItemsBinding.inflate(inflater, parent, false)
        return ClassifiedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {
        holder.binding.apply {
            if (data[position].userAdsImages.isEmpty()) {
                classifiedPriceTv.text = "$" + data[position].askingPrice.toString()
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            } else {
                Picasso.get()
                    .load("https://www.aaonri.com/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}").into(classifiedItemIv)
                classifiedPriceTv.text = "$" + data[position].askingPrice.toString()
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            }
            //classifiedPostDateTv.text = data[position].createdOn
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    fun setData(data: List<Classified>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ClassifiedViewHolder(val binding: ClassifiedCardItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

}