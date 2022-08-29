package com.aaonri.app.data.home.adapter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.View
import android.webkit.URLUtil
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.BuildConfig
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.event.model.UserEvent
import com.aaonri.app.databinding.ClassifiedAdvertiseItemBinding
import com.aaonri.app.databinding.ClassifiedCardItemsBinding
import com.aaonri.app.databinding.EventAdvertiseItemBinding
import com.aaonri.app.databinding.EventItemBinding
import com.bumptech.glide.Glide
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

sealed class HomeScreenViewHolders(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var itemClickListenerClassified: ((view: View, item: UserAds, position: Int) -> Unit)? =
        null

    var itemClickListenerEvent: ((view: View, item: UserEvent, position: Int) -> Unit)? =
        null

    class ClassifiedViewHolder(private val binding: ClassifiedCardItemsBinding) :
        HomeScreenViewHolders(binding) {
        val context = binding.classifiedCardView.context
        fun bind(userAds: UserAds) {
            binding.apply {
                val random = userAds.askingPrice

                val df = DecimalFormat("#,###.00")
                df.roundingMode = RoundingMode.DOWN
                val roundoff = df.format(random)

                if (userAds.favorite) {
                    like.visibility = View.VISIBLE
                }

                if (userAds.userAdsImages.isEmpty()) {

                    classifiedPriceTv.text = "$$roundoff"

                    classifiedTitleTv.text = userAds.adTitle
                    locationClassifiedTv.text =
                        userAds.adLocation + " - " + userAds.adZip
                    popularTv.visibility =
                        if (userAds.popularOnAaonri) View.VISIBLE else View.GONE
                } else {
                    Glide.with(context)
                        .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAds.userAdsImages[0].imagePath}")
                        .into(classifiedItemIv)
                    /*classifiedItemIv.load("https://www.aaonri.com/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}") {
                        placeholder(R.drawable.ic_image_placeholder)
                    }*/
                    classifiedPriceTv.text = "$$roundoff"
                    classifiedTitleTv.text = userAds.adTitle
                    locationClassifiedTv.text =
                        userAds.adLocation + " - " + userAds.adZip
                    popularTv.visibility =
                        if (userAds.popularOnAaonri) View.VISIBLE else View.GONE
                }

                val date = userAds.createdOn.subSequence(0, 10)
                val year = date.subSequence(0, 4)
                val month = date.subSequence(5, 7)
                val day = date.subSequence(8, 10)
                classifiedPostDateTv.text = "Posted On: $month-$day-$year"
                root.setOnClickListener {
                    itemClickListenerClassified?.invoke(it, userAds, adapterPosition)

                }
            }
        }
    }

    class ClassifiedAdvertiseViewHolder(private val binding: ClassifiedAdvertiseItemBinding) :
        HomeScreenViewHolders(binding) {
        val context = binding.classifiedImageWithTextAdCv.context
        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {

            when (findAllActiveAdvertiseResponseItem.advertisementPageLocation.type) {
                "TXTONLY" -> {
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle.isNullOrEmpty()) {
                        binding.classifiedOnlyTextTitle.text =
                            findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle
                    }
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription.isNullOrEmpty()) {
                        binding.classifiedOnlyTextDesc.text =
                            Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription)
                    }
                    binding.classifiedOnlyTextAdCv.visibility = View.VISIBLE
                }
                "IMGONLY" -> {
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adImage.isNullOrEmpty()) {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                                .into(binding.classifiedImageOnlyItemIv)
                        }
                    }
                    binding.classifiedImageOnlyAdCv.visibility = View.VISIBLE
                }
                "BOTH" -> {
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle.isNullOrEmpty()) {
                        binding.classifiedImageWithTextTitle.text =
                            findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle
                    }
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription.isNullOrEmpty()) {
                        binding.classifiedImageWithTextDesc.text =
                            Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription)
                    }
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adImage.isNullOrEmpty()) {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                                .into(binding.classifiedImageWithTextIv)
                        }
                    }
                    binding.classifiedImageWithTextAdCv.visibility = View.VISIBLE
                }
            }
            binding.constraintLayout.setOnClickListener {
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                    context?.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)
                        )
                    )
                }
            }
        }
    }

    class EventViewHolder(private val binding: EventItemBinding) : HomeScreenViewHolders(binding) {
        val context = binding.eventImageView.context
        private var startDate: String? = null
        private var startTimeOfEvent: String? = null
        private var endTimeOfEvent: String? = null
        private var timeZone: String? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: UserEvent) {
            binding.apply {

                try {
                    startDate = DateTimeFormatter.ofPattern("MM-dd-yyyy").format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .parse(data.startDate.split("T")[0])
                    )
                    startTimeOfEvent = LocalTime.parse(data.startTime)
                        .format(DateTimeFormatter.ofPattern("h:mma"))
                    endTimeOfEvent = LocalTime.parse(data.endTime)
                        .format(DateTimeFormatter.ofPattern("h:mma"))
                    timeZone = data.timeZone
                    eventTiming.text =
                        "Starts From $startDate, $startTimeOfEvent - $endTimeOfEvent  $timeZone"
                } catch (e: Exception) {

                }

                if (data.images.isNotEmpty()) {
                    if (data.images[0].imagePath.contains(".cover") || data.images[0].imagePath.contains(
                            ".first"
                        ) || data.images[0].imagePath.contains(".second") || data.images[0].imagePath.contains(
                            ".third"
                        )
                    ) {
                        data.images.forEachIndexed { index, userAdsImage ->

                            if (userAdsImage.imagePath.contains(".cover")) {
                                val image =
                                    "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${data.images[index].imagePath}"
                                Glide.with(context).load(image)
                                    .into(eventImageView)
                            }

                        }
                    } else {
                        val image =
                            "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${data.images[0].imagePath}"
                        Glide.with(context).load(image)
                            .into(eventImageView)
                    }
                    eventName.text = data.title

                    totalVisiting.text = data.totalVisiting.toString()
                    totalFavourite.text = data.totalFavourite.toString()
                    try {
                        eventLocationZip.text =
                            if (data.city.isNotEmpty()) "${data.city}" else "" + (if (data.zipCode.isNotEmpty() && data.city.isNotEmpty()) "-" else "") + if (data.zipCode.isNotEmpty()) "${data.zipCode}" else ""
                    } catch (e: Exception) {

                    }
                    if (data.fee > 0) {
                        val df = DecimalFormat("#,###.00")
                        df.roundingMode = RoundingMode.DOWN
                        val roundoff = df.format(data.fee)
                        eventFee.text = "$$roundoff"
                    } else {
                        eventFee.text = "FREE"
                    }

                } else {
                    eventName.text = data.title
                    totalVisiting.text = data.totalVisiting.toString()
                    totalFavourite.text = data.totalFavourite.toString()
                    try {
                        eventLocationZip.text =
                            if (data.city.isNotEmpty()) "${data.city}" else "" + (if (data.zipCode.isNotEmpty() && data.city.isNotEmpty()) "-" else "") + if (data.zipCode.isNotEmpty()) "${data.zipCode}" else ""
                    } catch (e: Exception) {

                    }
                    if (data.fee > 0) {
                        eventFee.text = "$" + data.fee.toString()
                    } else {
                        eventFee.text = "FREE"
                    }
                }
                root.setOnClickListener {
                    itemClickListenerEvent?.invoke(it, data, adapterPosition)
                }
            }
        }
    }

    class EventAdvertiseViewHolder(private val binding: EventAdvertiseItemBinding) :
        HomeScreenViewHolders(binding) {
        val context = binding.constraintLayout.context

        fun bind(findAllActiveAdvertiseResponseItem: FindAllActiveAdvertiseResponseItem) {

            when (findAllActiveAdvertiseResponseItem.advertisementPageLocation.type) {
                "TXTONLY" -> {
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle.isNullOrEmpty()) {
                        binding.textOnlyTitle.text =
                            findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle
                    }
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription.isNullOrEmpty()) {
                        binding.textOnlyDesc.text =
                            Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription)
                    }
                    binding.textOnlyCv.visibility = View.VISIBLE
                }
                "IMGONLY" -> {
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adImage.isNullOrEmpty()) {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                                .into(binding.imageOnlyIv)
                        }
                    }
                    binding.imageOnlyCv.visibility = View.VISIBLE
                }
                "BOTH" -> {
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle.isNullOrEmpty()) {
                        binding.imageWithTextTitle.text =
                            findAllActiveAdvertiseResponseItem.advertisementDetails.adTitle
                    }
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription.isNullOrEmpty()) {
                        binding.imageWithTextDesc.text =
                            Html.fromHtml(findAllActiveAdvertiseResponseItem.advertisementDetails.adDescription)
                    }
                    if (!findAllActiveAdvertiseResponseItem.advertisementDetails.adImage.isNullOrEmpty()) {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${findAllActiveAdvertiseResponseItem.advertisementDetails.adImage}")
                                .into(binding.imageWithTextIv)
                        }
                    }
                    binding.imageWithTextCv.visibility = View.VISIBLE
                }
            }
            binding.constraintLayout.setOnClickListener {
                if (URLUtil.isValidUrl(findAllActiveAdvertiseResponseItem.advertisementDetails.url)) {
                    context?.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(findAllActiveAdvertiseResponseItem.advertisementDetails.url)
                        )
                    )
                }
            }
        }

    }

}