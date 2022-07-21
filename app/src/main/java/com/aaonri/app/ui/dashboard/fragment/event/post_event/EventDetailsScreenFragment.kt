package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.model.EventAddGoingRequest
import com.aaonri.app.data.event.model.EventAddInterestedRequest
import com.aaonri.app.data.event.model.EventDetailsResponse
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class EventDetailsScreenFragment : Fragment() {
    val args: EventDetailsScreenFragmentArgs by navArgs()
    var evenDetailsBinding: FragmentEventDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    val eventViewModel: EventViewModel by activityViewModels()
    var eventPremiumLink: String = ""
    var startDate = ""
    var endDate = ""
    var eventTitleName = ""
    var isVisiting = false
    var isInterested = false
    var firstImageuri = ""
    var eventname = ""
    val eventdesc = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        evenDetailsBinding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        postEventViewModel.getEventDetails(args.eventId)

        evenDetailsBinding?.apply {

            if (args.isMyEvent) {
                moreBtn.visibility = View.VISIBLE
            }
            val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
            if (email != null) {
                postEventViewModel.getisUserVisitingEventInfo(
                    email, args.eventId
                )

                postEventViewModel.getUserisInterested(
                    email, "Event", args.eventId
                )
            }

            val bottomSheetOuter = BottomSheetBehavior.from(eventDetailsBottom)

            bottomSheetOuter.peekHeight = 470
            bottomSheetOuter.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetOuter.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (bottomSheetOuter.state == 3) {
                        arrowBottomSheet.rotation = 180F
                    } else if (bottomSheetOuter.state == 4) {
                        arrowBottomSheet.rotation = 360F
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    return
                }
            })
            navigateBack.setOnClickListener {
                postEventViewModel.eventDetailsData.value = null
                findNavController().navigateUp()

            }


            /* shareBtn.setOnClickListener {
                 //getScreenShot(view)
                 *//*context?.let { it1 -> shareImage(it1,  ) }*//*
            }*/

            moreBtn.setOnClickListener {
                context?.let { PreferenceManager<String>(it) }
                    ?.set("description", "")
                val action =
                    EventDetailsScreenFragmentDirections.actionEventDetailsScreenFragmentToUpdateDeleteClassifiedBottom(
                        args.eventId,
                        false
                    )
                findNavController().navigate(action)
            }
            interestedBtn.setOnClickListener {
                eventViewModel.setCallEventApiAfterDelete(true)
                val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
                postEventViewModel.addEventAddInterested(
                    EventAddInterestedRequest(
                        emailId = email.toString(),
                        favourite = isInterested,
                        itemId = args.eventId,
                        service = "Event"
                    )
                )
            }

            goingBtn.setOnClickListener {
                eventViewModel.setCallEventApiAfterDelete(true)
                val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
                postEventViewModel.addEventGoing(
                    EventAddGoingRequest(
                        emailId = email.toString(),
                        eventId = args.eventId,
                        visiting = isVisiting
                    )
                )

            }
            calendarBtn.setOnClickListener {
                try {
                    val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val mStartTime = mSimpleDateFormat.parse(startDate)
                    val mEndTime = mSimpleDateFormat.parse(endDate)
                    val mIntent = Intent(Intent.ACTION_EDIT)
                    mIntent.type = "vnd.android.cursor.item/event"
                    mIntent.putExtra("beginTime", mStartTime.time)
                    mIntent.putExtra("time", true)
                    mIntent.putExtra("rule", "FREQ=YEARLY")
                    mIntent.putExtra("endTime", mEndTime.time)
                    mIntent.putExtra("title", eventTitleName.toString())
                    startActivity(mIntent)
                } catch (e: Exception) {

                }

            }

            shareBtn.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_SEND).setType("image/*")
                    val bitmap = addImage.drawable.toBitmap() // your imageView here.
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path = MediaStore.Images.Media.insertImage(
                        requireContext().contentResolver,
                        bitmap,
                        "tempimage",
                        null
                    )
                    val uri = Uri.parse(path)
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.setType("text/plain")
                    val shareSub = "${BuildConfig.BASE_URL}/#/events/details/${args.eventId}"
                    intent.putExtra(Intent.EXTRA_TEXT, shareSub)
                    startActivity(intent)
//                uri.toFile().delete()
                } catch (e: Exception) {
                }
            }

        }

        postEventViewModel.eventuserVisitinginfoData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    isVisiting = !response.data.toBoolean()
                    //Toast.makeText(context, "${response.data?.favourite}", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }
        postEventViewModel.eventuserInterestedinfoData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    isInterested = !response.data.toBoolean()

                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }
        postEventViewModel.addInterestedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                    if (response.data?.favourite != null) {
                        isInterested = !response.data?.favourite
                    }
                    postEventViewModel.getEventDetails(args.eventId)

                }
                is Resource.Error -> {

                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }

        postEventViewModel.addGoingData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    isVisiting = !response.data?.visiting!!
                    postEventViewModel.getEventDetails(args.eventId)

                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }

        postEventViewModel.eventDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    evenDetailsBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    evenDetailsBinding?.progressBar?.visibility = View.GONE

                    response.data?.let {
                        setEventdDetails(it)
                        EventStaticData.updateEventDetails(it)
                    }
                }
                is Resource.Error -> {
                    evenDetailsBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }

        eventViewModel.callEventDetailsApiAfterUpdating.observe(viewLifecycleOwner) {
            if (it) {
                postEventViewModel.getEventDetails(args.eventId)
                eventViewModel.setCallEventDetailsApiAfterUpdating(false)
            }
        }

//        eventViewModel.eventDetailsData..observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is Resource.Loading -> {
//
//                }
//                is Resource.Success -> {
//                    evenDetailsBinding?.sellerName?.text =
//                        response.data?.firstName + " " + response.data?.lastName
//                }
//                is Resource.Error -> {
//                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
//                        .show()
//                }
//                else -> {
//                }
//            }
//        }


        postEventViewModel.deleteEventData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    findNavController().navigateUp()
                    eventViewModel.setCallEventApiAfterDelete(true)
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        postEventViewModel.eventCategoryData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    /*categoryBinding?.progressBar?.visibility = View.VISIBLE*/
                }
                is Resource.Success -> {
                    response.data?.let { EventStaticData.updateEventCategory(it) }
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        return evenDetailsBinding?.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEventdDetails(event: EventDetailsResponse) {
        eventPremiumLink = event.socialMediaLink
        eventname = event.title
        evenDetailsBinding?.ll1?.visibility = View.VISIBLE
        startDate = "${event.startDate.split("T")[0]}T${event.startTime}:00"
        endDate = "${event.endDate.split("T")[0]}T${event.endTime}:00"
        eventTitleName = event.title
        evenDetailsBinding?.navigateBack?.visibility = View.VISIBLE
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        if (event.createdBy == email) {
            evenDetailsBinding?.moreBtn?.visibility = View.VISIBLE
            if (EventStaticData.getEventCategory().isEmpty()) {
                postEventViewModel.getEventCategory()
            }
        }
        /*if (eventPremiumLink.isEmpty()) {
            evenDetailsBinding?.buyTicket?.visibility = View.GONE
        } else {
            evenDetailsBinding?.buyTicket?.visibility = View.VISIBLE
        }*/
        //event.images.sortedWith(compareByDescending { it.imageId })
        event.images.forEachIndexed { index, userAdsImage ->
            if (userAdsImage.imagePath.contains(".cover") || userAdsImage.imagePath.contains(".first") || userAdsImage.imagePath.contains(
                    ".second"
                ) || userAdsImage.imagePath.contains(".third")
            ) {
                if (userAdsImage.imagePath.contains(".cover")) {
                    /* evenDetailsBinding?.image1CardView?.visibility = View.VISIBLE

                       context?.let {
                           evenDetailsBinding?.addImage?.let { it1 ->
                               Glide.with(it)
                                   .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                   .into(it1)
                           }
                       }
                         context?.let {
                             evenDetailsBinding?.addImage?.let { it1 ->
                                 Glide.with(it)
                                     .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                     .into(it1)
                             }
                         }*/
                    evenDetailsBinding?.image1?.visibility = View.GONE
//                    context?.let {
//                        evenDetailsBinding?.image1?.let { it1 ->
//                            Glide.with(it)
//                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
//                                .into(it1)
//                        }
//                    }
                }
                if (userAdsImage.imagePath.contains(".first")) {
                    evenDetailsBinding?.image2CardView?.visibility = View.VISIBLE

                    context?.let {
                        evenDetailsBinding?.addImage?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .into(it1)
                        }
                    }

                    context?.let {
                        evenDetailsBinding?.image2?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .into(it1)
                        }
                    }
                }
                if (userAdsImage.imagePath.contains(".second")) {
                    evenDetailsBinding?.image3CardView?.visibility = View.VISIBLE
                    context?.let {
                        evenDetailsBinding?.image2?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .into(it1)
                        }
                    }
                }

                if (userAdsImage.imagePath.contains(".second")) {
                    evenDetailsBinding?.image3CardView?.visibility = View.VISIBLE

                    /*  context?.let {
                      evenDetailsBinding?.addImage?.let { it1 ->
                          Glide.with(it)
                              .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                              .into(it1)
                      }
                  }*/

                    context?.let {
                        evenDetailsBinding?.image3?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .into(it1)
                        }
                    }
                }
                if (userAdsImage.imagePath.contains(".third")) {
                    evenDetailsBinding?.image4CardView?.visibility = View.VISIBLE

                    /*context?.let {
                    evenDetailsBinding?.addImage?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                            .into(it1)
                    }
                }*/

                    context?.let {
                        evenDetailsBinding?.image4?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .into(it1)
                        }
                    }
                }
            } else {


                when (index) {
                    0 -> {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                        evenDetailsBinding?.image1CardView?.visibility = View.VISIBLE
                        evenDetailsBinding?.image1CardView?.visibility = View.VISIBLE

                        /*  context?.let {
                          evenDetailsBinding?.addImage?.let { it1 ->
                              Glide.with(it)
                                  .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                  .into(it1)
                          }
                      }*/

                        context?.let {
                            evenDetailsBinding?.image1?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                    1 -> {
                        evenDetailsBinding?.image2CardView?.visibility = View.VISIBLE

                        /*  context?.let {
                          evenDetailsBinding?.addImage?.let { it1 ->
                              Glide.with(it)
                                  .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                  .into(it1)
                          }
                      }*/

                        context?.let {
                            evenDetailsBinding?.image2?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                    2 -> {
                        evenDetailsBinding?.image3CardView?.visibility = View.VISIBLE

                        /* context?.let {
                         evenDetailsBinding?.addImage?.let { it1 ->
                             Glide.with(it)
                                 .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                 .into(it1)
                         }
                     }*/

                        context?.let {
                            evenDetailsBinding?.image3?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                    3 -> {
                        evenDetailsBinding?.image4CardView?.visibility = View.VISIBLE

                        /* context?.let {
                         evenDetailsBinding?.addImage?.let { it1 ->
                             Glide.with(it)
                                 .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                 .into(it1)
                         }
                     }*/

                        context?.let {
                            evenDetailsBinding?.image4?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                }
            }
        }

        /*  if (event.images.isNotEmpty()) {
              context?.let {
                  evenDetailsBinding?.addImage?.let { it1 ->
                      Glide.with(it)
                          .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${event.images[0].imagePath}")
                          .into(it1)
                  }
              }
          }*/

        evenDetailsBinding?.image1?.setOnClickListener {
            event.images.forEachIndexed { index, userAdsImage ->
                if (userAdsImage.imagePath.contains(".cover") || userAdsImage.imagePath.contains(".first") || userAdsImage.imagePath.contains(
                        ".second"
                    ) || userAdsImage.imagePath.contains(".third")
                ) {
                    if (userAdsImage.imagePath.contains(".cover")) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }

                    }

                } else {
                    if (index == 0) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                }
                changeCardViewBorder(0)
            }
        }

        evenDetailsBinding?.image2?.setOnClickListener {
            event.images.forEachIndexed { index, userAdsImage ->
                if (userAdsImage.imagePath.contains(".cover") || userAdsImage.imagePath.contains(".first") || userAdsImage.imagePath.contains(
                        ".second"
                    ) || userAdsImage.imagePath.contains(".third")
                ) {
                    if (userAdsImage.imagePath.contains(".first")) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }

                    }

                } else {
                    if (index == 1) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                }
                changeCardViewBorder(1)
            }
        }
        evenDetailsBinding?.image3?.setOnClickListener {
            event.images.forEachIndexed { index, userAdsImage ->
                if (userAdsImage.imagePath.contains(".cover") || userAdsImage.imagePath.contains(".first") || userAdsImage.imagePath.contains(
                        ".second"
                    ) || userAdsImage.imagePath.contains(".third")
                ) {
                    if (userAdsImage.imagePath.contains(".second")) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }

                    }

                } else {
                    if (index == 2) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                }
                changeCardViewBorder(2)
            }
        }
        evenDetailsBinding?.image4?.setOnClickListener {
            event.images.forEachIndexed { index, userAdsImage ->
                if (userAdsImage.imagePath.contains(".cover") || userAdsImage.imagePath.contains(".first") || userAdsImage.imagePath.contains(
                        ".second"
                    ) || userAdsImage.imagePath.contains(".third")
                ) {
                    if (userAdsImage.imagePath.contains(".third")) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                } else {
                    if (index == 3) {
                        context?.let {
                            evenDetailsBinding?.addImage?.let { it1 ->
                                Glide.with(it)
                                    .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                    .into(it1)
                            }
                        }
                    }
                }
                changeCardViewBorder(3)
            }
        }
        if (event.fee > 0) {
            val random = event.fee

            val df = DecimalFormat("###.00")
            df.roundingMode = RoundingMode.DOWN
            val roundoff = df.format(random)

            evenDetailsBinding?.eventPriceTv?.text = "$$roundoff"
        } else {
            evenDetailsBinding?.eventPriceTv?.text = "FREE"
        }
        evenDetailsBinding?.eventTitle?.text = event.title
        evenDetailsBinding?.eventDescTv?.text = Html.fromHtml(event.description)
        evenDetailsBinding?.locationIconEvent?.visibility = View.VISIBLE
        try {
            evenDetailsBinding?.locationEventTv?.text =
                "${if (!event.address1.isNullOrEmpty()) event.address1 else ""} ${if (!event.address2.isNullOrEmpty()) event.address2 else ""} ${if (!event.city.isNullOrEmpty()) event.city else ""} ${if (!event.state.isNullOrEmpty()) event.state else ""} "
        } catch (e: Exception) {
        }
        evenDetailsBinding?.eventLocationZip?.text = event.zipCode
        evenDetailsBinding?.eventCategoryTv?.text = "Category: " + event.category
        evenDetailsBinding?.eventDetailsBottom?.visibility = View.VISIBLE
        if (event.socialMediaLink.isNotEmpty()) {
            evenDetailsBinding?.buyTicket?.visibility = View.VISIBLE
            evenDetailsBinding?.premiumLink?.text = event.socialMediaLink
        } else {
            evenDetailsBinding?.buyTicket?.visibility = View.GONE
        }
        evenDetailsBinding?.totalVisitingTv?.text = event.totalVisiting.toString() + " going"
        evenDetailsBinding?.totalFavoriteTv?.text = event.totalFavourite.toString() + " Interested"

        evenDetailsBinding?.premiumLink?.setOnClickListener {
            if (URLUtil.isValidUrl(eventPremiumLink)) {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(eventPremiumLink)))
            } else {
                showAlert("Invalid link")
            }
        }

        try {
            evenDetailsBinding?.postedDate1?.text = "${
                DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .parse(event.startDate.split("T")[0])
                    )
            }, ${
                LocalTime.parse(event.startTime)
                    .format(DateTimeFormatter.ofPattern("h:mma"))
            } ${event.timeZone}"
            evenDetailsBinding?.postedDate2?.text = "${
                DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(event.endDate.split("T")[0])
                    )
            }, ${
                LocalTime.parse(event.endTime)
                    .format(DateTimeFormatter.ofPattern("h:mma"))
            } ${event.timeZone}"
        } catch (e: Exception) {
        }

//        itemId = data.id

        if (event.images.isEmpty()) {
            changeCardViewBorder(9)
        } else {
            changeCardViewBorder(0)
        }
        if (isVisiting) {
            evenDetailsBinding?.goingBtn?.setText("GOING")
        } else {
            evenDetailsBinding?.goingBtn?.setText("NOT GOING")
        }

        if (isInterested) {
            evenDetailsBinding?.interestedBtn?.setText("INTERESTED")
        } else {
            evenDetailsBinding?.interestedBtn?.setText("NOT INTERESTED")
        }
    }

    private fun changeCardViewBorder(selectedImageIndex: Int) {

        if (selectedImageIndex == 0) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                evenDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image4CardView?.setStrokeColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 1) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                evenDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image4CardView?.setStrokeColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 2) {

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                evenDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image4CardView?.setStrokeColor(
                    it2
                )
            }

        } else if (selectedImageIndex == 3) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                evenDetailsBinding?.image4CardView?.setStrokeColor(
                    it2
                )
            }
        } else {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                evenDetailsBinding?.image4CardView?.setStrokeColor(
                    it2
                )
            }
        }
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    fun getScreenShot(view: View?): Bitmap? {
        return if (view != null) {
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            bitmap
        } else {
            null
        }
    }

//    fun shareImage(context: Context, file: File, message: String?) {
//        val apkURI: Uri
//        val intent = Intent(Intent.ACTION_SEND)
//        try {
//            apkURI = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                FileProvider.getUriForFile(
//                    context,
//                    context.packageName.toString() + ".provider",
//                    file
//                )
//            } else {
//                Uri.fromFile(file)
//            }
//            intent.putExtra(Intent.EXTRA_TEXT, message)
//            intent.putExtra(Intent.EXTRA_STREAM, apkURI)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//            intent.type = "image/png"
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//
//        // Intent inte = Intent.createChooser(intent,"Share file");
//
//        // List<ResolveInfo> resolverList = context.getPackageManager().queryIntentActivities(inte, PackageManager.MATCH_DEFAULT_ONLY);
//        try {
//            context.startActivity(Intent.createChooser(intent, "Share file"))
//            //    for(ResolveInfo info:resolverList){
//            //        String packageName = info.activityInfo.packageName;
//            //       context.grantUriPermission(packageName,apkURI,Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            //  }
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(context, "No app found", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        postEventViewModel.eventDetailsData.value = null
        postEventViewModel.deleteEventData.value = null
    }


}