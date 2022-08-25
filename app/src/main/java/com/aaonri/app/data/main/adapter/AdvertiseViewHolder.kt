package com.aaonri.app.data.main.adapter

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.webkit.URLUtil
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.databinding.ImageOnlyViewHolderBinding
import com.aaonri.app.databinding.ImageWithTextBinding
import com.aaonri.app.databinding.TextOnlyItemBinding
import com.bumptech.glide.Glide

sealed class AdvertiseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    /** Advertise ViewHolder for multiView Type**/
    class TextOnlyViewHolder(private val binding: TextOnlyItemBinding) :
        AdvertiseViewHolder(binding) {
        val context = binding.textOnlyFl.context
        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {

            binding.textView.text =
                findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle
            binding.advertiseDesc.text =
                Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription)
            binding.textOnlyFl.setOnClickListener {
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                    context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)))
                }
            }

        }
    }

    /** Advertise ViewHolder for multiView Type**/
    class ImageOnlyViewHolder(private val binding: ImageOnlyViewHolderBinding) :
        AdvertiseViewHolder(binding) {
        val context = binding.imageView.context
        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                    .into(binding.imageView)
            }

            binding.imageOnlyCv.setOnClickListener{
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                 context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)))
                }
            }
        }
    }

    /** Advertise ViewHolder for multiView Type**/
    class ImageAndTextViewHolder(private val binding: ImageWithTextBinding) :
        AdvertiseViewHolder(binding) {
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

            binding.imageOnlyCv.setOnClickListener{
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                    context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)))
                }
            }
        }
    }

}