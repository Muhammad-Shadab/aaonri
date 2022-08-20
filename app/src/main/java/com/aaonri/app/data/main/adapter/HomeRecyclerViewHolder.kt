package com.aaonri.app.data.main.adapter

import android.text.Html
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.databinding.ImageOnlyViewHolderBinding
import com.aaonri.app.databinding.ImageWithTextBinding
import com.aaonri.app.databinding.TextOnlyItemBinding
import com.bumptech.glide.Glide

sealed class HomeRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class TextOnlyViewHolder(private val binding: TextOnlyItemBinding) :
        HomeRecyclerViewHolder(binding) {

        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {
            binding.textView.text =
                findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle
            binding.advertiseDesc.text =
                Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription)
        }
    }

    class ImageOnlyViewHolder(private val binding: ImageOnlyViewHolderBinding) :
        HomeRecyclerViewHolder(binding) {
        val context = binding.imageView.context
        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                    .into(binding.imageView)
            }
        }
    }

    class ImageAndTextViewHolder(private val binding: ImageWithTextBinding) :
        HomeRecyclerViewHolder(binding) {
        val context = binding.imageView.context
        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {
            binding.textView.text =
                findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle
            binding.advertiseDesc.text =
                Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription)
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                    .into(binding.imageView)
            }
        }

    }

}