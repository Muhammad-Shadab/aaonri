package com.aaonri.app.data.main.adapter

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.Html
import android.view.Gravity
import android.view.View
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


    /** Advertise ViewHolder for text only Type**/
    class TextOnlyViewHolder(private val binding: TextOnlyItemBinding) :
        AdvertiseViewHolder(binding) {
        val context = binding.textOnlyFl.context

        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {

            binding.advertiseDesc.text =
                Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription).trim()
            binding.textOnlyFl.setOnClickListener {
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                    context?.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)
                        )
                    )
                }
            }
            binding.textOnlyFl.layoutParams.width = getScreenWidth() / 2 - 50
        }
    }

    /** Advertise ViewHolder for image only Type**/
    class ImageOnlyViewHolder(private val binding: ImageOnlyViewHolderBinding) :
        AdvertiseViewHolder(binding) {
        val context = binding.imageView.context
        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                    .into(binding.imageView)
            }

            binding.textOnlyFl.setOnClickListener {
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                    context?.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)
                        )
                    )
                }
            }
            binding.textOnlyFl.layoutParams.width = getScreenWidth() / 2 - 50
        }
    }

    /** Advertise ViewHolder for Image with text Type**/
    class ImageAndTextViewHolder(private val binding: ImageWithTextBinding) :
        AdvertiseViewHolder(binding) {
        val context = binding.imageView.context
        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {
            when(findAllActiveAdvertiseResponseItem.template.code)
            {
                  "IMTB"->{binding.bothLl.gravity = Gravity.BOTTOM
                      binding.advertiseDesc.gravity = Gravity.CENTER}

                "IMTL"->{binding.bothLl.gravity = Gravity.BOTTOM
                         binding.advertiseDesc.gravity = Gravity.START }

                else -> {
                    binding.bothLl.gravity = Gravity.CENTER
                    binding.advertiseDesc.gravity = Gravity.CENTER
                }
            }


            if(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription.isNullOrEmpty())
            {
                binding.advertiseDesc.visibility = View.GONE

            }else{
                binding.advertiseDesc.visibility = View.VISIBLE
                binding.advertiseDesc.text =
                    Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription).trim()
            }
            context?.let { it1 ->
                Glide.with(it1)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                    .into(binding.imageView)
            }

            binding.imgWithTxtCl.setOnClickListener {
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                    context?.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)
                        )
                    )
                }
            }
            binding.imgWithTxtCl.layoutParams.width = getScreenWidth() / 2 - 50
        }
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

}