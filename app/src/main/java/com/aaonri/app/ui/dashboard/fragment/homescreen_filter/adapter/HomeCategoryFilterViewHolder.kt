package com.aaonri.app.ui.dashboard.fragment.homescreen_filter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.event.model.Event
import com.aaonri.app.data.event.model.UserEvent
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.databinding.ClassifiedCardItemsBinding
import com.aaonri.app.databinding.EventItemBinding
import com.aaonri.app.databinding.ImmigrationsItemBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter

sealed class HomeCategoryFilterViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: Any) -> Unit)? = null

    /*class ClassifiedItemViewHolder(private val binding: ClassifiedCardItemsBinding) :
        HomeCategoryFilterViewHolder(binding) {
        val context: Context = binding.classifiedCardView.context
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
                    *//*classifiedItemIv.load("https://www.aaonri.com/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}") {
                        placeholder(R.drawable.ic_image_placeholder)
                    }*//*
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
                    //itemClickListenerClassified?.invoke(it, userAds, adapterPosition)
                }
            }
        }
    }*/

    class EventViewHolder(private val binding: EventItemBinding) :
        HomeCategoryFilterViewHolder(binding) {
        val context: Context = binding.eventImageView.context
        private var startDate: String? = null
        private var startTimeOfEvent: String? = null
        private var endTimeOfEvent: String? = null
        private var timeZone: String? = null

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: Event) {
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
                                placeholder.visibility = View.GONE
                                Glide.with(context).load(image)
                                    .error(R.drawable.small_image_placeholder)
                                    .into(eventImageView)
                            }
                        }
                    } else {
                        val image =
                            "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${data.images[0].imagePath}"
                        placeholder.visibility = View.GONE
                        Glide.with(context).load(image)
                            .error(R.drawable.small_image_placeholder)
                            .into(eventImageView)
                    }
                    eventName.text = data.title

                    totalVisiting.text = data.totalVisiting.toString()
                    totalFavourite.text = data.totalFavourite.toString()
                    try {
                        eventLocationZip.text =
                            if (data.city?.isNotEmpty() == true) "${data.city}" else "" + (if (data.zipCode?.isNotEmpty() == true && data.city?.isNotEmpty() == true) "-" else "") + if (data.zipCode?.isNotEmpty() == true) "${data.zipCode}" else ""
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
                    placeholder.visibility = View.VISIBLE
                    eventName.text = data.title
                    totalVisiting.text = data.totalVisiting.toString()
                    totalFavourite.text = data.totalFavourite.toString()
                    try {
                        eventLocationZip.text =
                            if (data.city?.isNotEmpty() == true) "${data.city}" else "" + (if (data.zipCode?.isNotEmpty() == true && data.city?.isNotEmpty() == true) "-" else "") + if (data.zipCode?.isNotEmpty() == true) "${data.zipCode}" else ""
                    } catch (e: Exception) {

                    }
                    if (data.fee > 0) {
                        eventFee.text = "$" + data.fee.toString()
                    } else {
                        eventFee.text = "FREE"
                    }
                }
                root.setOnClickListener {
                    itemClickListener?.invoke(it, data)
                }
            }
        }
    }

    class AllImmigrationDiscussionViewHolder(private val binding: ImmigrationsItemBinding) :
        HomeCategoryFilterViewHolder(binding) {
        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(discussion: Discussion) {
            binding.apply {
                val context = discussionNameTv.context
                val userEmail =
                    context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
                discussionNameTv.text = discussion.discussionTopic
                discussionDesc.text = discussion.discussionDesc
                postedByTv.text = "Posted by: ${discussion.createdBy}, ${
                    DateTimeFormatter.ofPattern("MM-dd-yyyy")
                        .format(
                            DateTimeFormatter.ofPattern("dd-MMM-yyyy").parse(discussion.createdOn)
                        )
                }"
                noOfReply.text = discussion.noOfReplies.toString()
                if (discussion.latestReply != null) {
                    latestReply.visibility = View.VISIBLE
                    latestReply.text =
                        "Last reply: ${discussion.latestReply.createdByName}  ${
                            DateTimeFormatter.ofPattern("MM-dd-yyyy")
                                .format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        .parse(discussion.latestReply.createdDate.split("T")[0])
                                )
                        }"
                }

                userEmail.let {

                    /*swipeLayout.isRightSwipeEnabled =
                        !discussion.approved && discussion.userId == it*/

                    swipeLayout.isRightSwipeEnabled = discussion.noOfReplies == 0

                    if (discussion.approved) {
                        updateImmigrationBtn.visibility = View.GONE
                    }
                }

                immigrationCv.setOnClickListener {
                    itemClickListener?.invoke(it, discussion)
                }

                updateImmigrationBtn.setOnClickListener {
                    //itemClickListener?.invoke(it, discussion, adapterPosition, true, false)
                }

                deleteImmigrationBtn.setOnClickListener {
                    //itemClickListener?.invoke(it, discussion, adapterPosition, false, true)
                }

            }
        }
    }

}