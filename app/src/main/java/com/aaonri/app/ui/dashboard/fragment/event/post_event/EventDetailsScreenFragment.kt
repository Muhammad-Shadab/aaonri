package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.CalendarContract
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.event.EventStaticData
import com.aaonri.app.data.event.model.EventAddGoingRequest
import com.aaonri.app.data.event.model.EventAddInterestedRequest
import com.aaonri.app.data.event.model.EventDetailsResponse
import com.aaonri.app.data.event.model.ImageXX
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentEventDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class EventDetailsScreenFragment : Fragment() {
    var binding: FragmentEventDetailsBinding? = null
    val args: EventDetailsScreenFragmentArgs by navArgs()
    val postEventViewModel: PostEventViewModel by activityViewModels()
    val eventViewModel: EventViewModel by activityViewModels()
    var adsGenericAdapter: AdsGenericAdapter? = null
    var eventPremiumLink: String = ""
    var isUserLogin: Boolean? = null
    var startDate = ""
    var endDate = ""
    var eventTitleName = ""
    var eventTimeZone = ""
    var isVisiting = false
    var isInterested = false
    var firstImageuri = ""
    var eventname = ""
    var eventLocationText = ""
    val eventdesc = ""
    private lateinit var layoutManager: LinearLayoutManager
    var adRvposition = 0
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    var path = ""
    var image1Link = ""
    var image2Link = ""
    var image3Link = ""
    var image4Link = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }
        postEventViewModel.getEventDetails(args.eventId)

        val guestUserLoginDialog = Dialog(requireContext())
        guestUserLoginDialog.setContentView(com.aaonri.app.R.layout.guest_user_login_dialog)
        guestUserLoginDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )
        guestUserLoginDialog.setCancelable(false)
        val dismissBtn =
            guestUserLoginDialog.findViewById<TextView>(R.id.dismissDialogTv)
        val loginBtn =
            guestUserLoginDialog.findViewById<TextView>(R.id.loginDialogTv)

        loginBtn.setOnClickListener {
            activity?.finish()
        }
        dismissBtn.setOnClickListener {
            guestUserLoginDialog.dismiss()
        }

        adsGenericAdapter = AdsGenericAdapter()
        adsGenericAdapter?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {

                val action =
                    EventDetailsScreenFragmentDirections.actionEventDetailsScreenFragmentToAdvertiseWebviewFragment(
                        item.advertisementDetails.url
                    )
                findNavController().navigate(action)
            }
        }
        binding?.apply {

            /*if (args.isMyEvent) {
                moreBtn.visibility = View.VISIBLE
            }*/

            val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
            if (email != null) {
                postEventViewModel.getisUserVisitingEventInfo(email, args.eventId)
                postEventViewModel.getUserisInterested(email, "Event", args.eventId)
            }

            if (ActiveAdvertiseStaticData.getAdvertiseOnEventDetails().isNotEmpty()) {
                adsGenericAdapter?.items = ActiveAdvertiseStaticData.getAdvertiseOnEventDetails()

                layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                bottomAdvertiseRv.layoutManager = layoutManager
                bottomAdvertiseRv.adapter = adsGenericAdapter
                bottomAdvertiseRv.addItemDecoration(
                    GridSpacingItemDecoration(
                        2,
                        32, 0
                    )
                )
                bottomAdvertiseRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == 1) {
                            stopAutoScrollBanner()
                        } else if (newState == 0) {

                            adRvposition = layoutManager.findFirstCompletelyVisibleItemPosition()
                            runAutoScrollBanner()
                        }
                    }
                })
            }


            val bottomSheetOuter = BottomSheetBehavior.from(eventDetailsBottom)

            /* val screenDp = context?.let { dpFromPx(it, getScreenHeight().toFloat()) }
             if (screenDp != null) {
                 if (screenDp in 900.0..1000.0) {
                     bottomSheetOuter.peekHeight = 630
                 } else if (screenDp in 800.0..900.0) {
                     bottomSheetOuter.peekHeight = 480
                 }else if (screenDp in 700.0..800.0) {
                     bottomSheetOuter.peekHeight = 650
                 } else if (screenDp in 600.0..700.0) {
                     bottomSheetOuter.peekHeight = 830
                 }
             }*/

            postEventViewModel.eventDetailsData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        response.data?.let {
                            setEventDetails(it)
                            EventStaticData.updateEventDetails(it)
                        }
                        binding?.linear?.viewTreeObserver?.addOnGlobalLayoutListener(
                            object :
                                OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    binding?.linear!!.viewTreeObserver.removeOnGlobalLayoutListener(
                                        this
                                    )
                                    val hiddenView: View =
                                        binding?.linear!!.getChildAt(1)
                                    bottomSheetOuter.peekHeight = hiddenView.top
                                }
                            })
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                    }
                }
            }

            linear.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    linear.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val hiddenView: View = linear.getChildAt(1)
                    bottomSheetOuter.peekHeight = hiddenView.top
                }
            })

            bottomSheetOuter.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetOuter.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (bottomSheetOuter.state == 3) {
                        arrowBottomSheet.rotation = 180F
                    } else if (bottomSheetOuter.state == 4) {
                        arrowBottomSheet.rotation = 360F
                        totalFavVisitingLl.visibility = View.VISIBLE
                    } else if (bottomSheetOuter.state == 1) {
                        totalFavVisitingLl.visibility = View.GONE
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
                if (isUserLogin == true) {
                    eventViewModel.setCallEventApiAfterDelete(true)
                    val email =
                        context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
                    postEventViewModel.addEventAddInterested(
                        EventAddInterestedRequest(
                            emailId = email.toString(),
                            favourite = isInterested,
                            itemId = args.eventId,
                            service = "Event"
                        )
                    )
                } else {
                    guestUserLoginDialog.show()
                }
            }

            goingBtn.setOnClickListener {
                if (isUserLogin == true) {
                    eventViewModel.setCallEventApiAfterDelete(true)
                    val email =
                        context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
                    postEventViewModel.addEventGoing(
                        EventAddGoingRequest(
                            emailId = email.toString(),
                            eventId = args.eventId,
                            visiting = isVisiting
                        )
                    )
                } else {
                    guestUserLoginDialog.show()
                }
            }
            calendarBtn.setOnClickListener {
                if (isUserLogin == true) {
                    try {
                        val mSimpleDateFormat =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                        when (eventTimeZone) {
                            "EST" -> eventTimeZone = "America/New_York"
                            "PST" -> eventTimeZone = "America/Los_Angeles"
                            "CST" -> eventTimeZone = "America/Chicago"
                            "MST" -> eventTimeZone = "America/Edmonton"
                        }

                        mSimpleDateFormat.timeZone = TimeZone.getTimeZone(eventTimeZone)
                        val mStartTime = mSimpleDateFormat.parse(startDate)
                        val mEndTime = mSimpleDateFormat.parse(endDate)
                        val mSimpleDateFormat1 =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                        mSimpleDateFormat1.timeZone =
                            TimeZone.getTimeZone(TimeZone.getDefault().toZoneId())
                        val changedStartTime = mSimpleDateFormat1.format(mStartTime)
                        val changedEndTime = mSimpleDateFormat1.format(mEndTime)

                        Log.e("data", changedStartTime)
                        val mIntent = Intent(Intent.ACTION_EDIT)
                        mIntent.type = "vnd.android.cursor.item/event"
                        mIntent.putExtra(
                            "beginTime",
                            mSimpleDateFormat1.parse(changedStartTime).time
                        )
                        mIntent.putExtra("time", true)
                        mIntent.putExtra("rule", "FREQ=YEARLY")
                        mIntent.putExtra(
                            "endTime",
                            mSimpleDateFormat1.parse(changedEndTime).time
                        )
                        mIntent.putExtra("title", eventTitleName)
                        mIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocationText)
                        startActivity(mIntent)
                    } catch (e: Exception) {

                    }
                } else {
                    guestUserLoginDialog.show()
                }

            }

            shareBtn.setOnClickListener {
                if (isUserLogin == true) {
                    val bitmap = addImage.drawable.toBitmap()
                    val shareIntent: Intent
                    var path =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .toString() + "/Share.png"
                    var out: OutputStream? = null
                    val file = File(path)
                    try {
                        out = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        out.flush()
                        out.close()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    path = file.path
                    val bmpUri = Uri.parse("$path")
                    shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    val baseUrl = BuildConfig.BASE_URL.replace(":8444", "")
                    val shareSub = "${baseUrl}/events/details/${args.eventId}"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
                    shareIntent.type = "image/png"
                    startActivity(Intent.createChooser(shareIntent, "Share with"))
                } else {
                    guestUserLoginDialog.show()
                }
            }

            image1CardView.setOnClickListener {
                context?.let { it1 ->
                    Glide.with(it1).load(image1Link).error(R.drawable.small_image_placeholder)
                        .into(addImage)
                }
                changeCardViewBorder(0)
            }

            image2CardView.setOnClickListener {
                context?.let { it1 ->
                    Glide.with(it1).load(image2Link).error(R.drawable.small_image_placeholder)
                        .into(addImage)
                }
                changeCardViewBorder(1)
            }

            image3CardView.setOnClickListener {
                context?.let { it1 ->
                    Glide.with(it1).load(image3Link).error(R.drawable.small_image_placeholder)
                        .into(addImage)
                }
                changeCardViewBorder(2)
            }

            image4CardView.setOnClickListener {
                context?.let { it1 ->
                    Glide.with(it1).load(image4Link).error(R.drawable.small_image_placeholder)
                        .into(addImage)
                }
                changeCardViewBorder(3)
            }

            reportInappropriateTv.setOnClickListener {
                val selectorIntent = Intent(Intent.ACTION_SENDTO)
                selectorIntent.data = Uri.parse("mailto:")

                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("admin@aaonri.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Inappropriate Content!")
                emailIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Dear aaonri admin, \n\nI would like to report this item, as inappropriate.\n\n${
                        BuildConfig.BASE_URL.replace(
                            ":8444",
                            ""
                        )
                    }/event/details/${args.eventId}"
                )
                emailIntent.selector = selectorIntent

                activity?.startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
        }



        postEventViewModel.eventuserVisitinginfoData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    isVisiting = !response.data.toBoolean()

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
            }
        }

        eventViewModel.callEventDetailsApiAfterUpdating.observe(viewLifecycleOwner) {
            if (it) {
                /**this is used to call details api after updation event but now it will navigate to back and when user open details page then detail api call automatically**/
                //postEventViewModel.getEventDetails(args.eventId)
                eventViewModel.setCallEventDetailsApiAfterUpdating(false)
                findNavController().navigateUp()
            }
        }

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
            }
        }


        return binding?.root
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEventDetails(event: EventDetailsResponse) {
        var listOfEventImages = mutableListOf<ImageXX>()
        eventPremiumLink = event.socialMediaLink
        eventname = event.title
        eventTimeZone = event.timeZone
        binding?.ll1?.visibility = View.VISIBLE

        /* var startTimeOfEvent =
             LocalTime.parse(event.startTime).format(DateTimeFormatter.ofPattern("h:mma"))
         var endTimeOfEvent =
             LocalTime.parse(event.endTime).format(DateTimeFormatter.ofPattern("h:mma"))
         Toast.makeText(context, "$startTimeOfEvent", Toast.LENGTH_SHORT).show()*/
        startDate = "${event.startDate.split("T")[0]}T${event.startTime}:00"
        endDate = "${event.endDate.split("T")[0]}T${event.endTime}:00"
        eventTitleName = event.title
        binding?.navigateBack?.visibility = View.VISIBLE
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        if (event.createdBy == email) {
            binding?.reportInappropriateTv?.visibility = View.GONE
            binding?.moreBtn?.visibility = View.VISIBLE
        }
        if (event.fee <= 0) {
            binding?.buyTicketHereTv?.text = "Reserve your ticket here"
        }
        /*if (eventPremiumLink.isEmpty()) {
            evenDetailsBinding?.buyTicket?.visibility = View.GONE
        } else {
            evenDetailsBinding?.buyTicket?.visibility = View.VISIBLE
        }*/
        //event.images.sortedWith(compareByDescending { it.imageId })


        /** Removing first index from list of images **/
        if (event.images.size > 1) {
            event.images.forEachIndexed { index, imageXX ->
                if (index > 0) {
                    if (!listOfEventImages.contains(imageXX)) {
                        listOfEventImages.add(imageXX)
                    }
                }
            }
        } else {
            event.images.forEach { imageXX ->
                if (!listOfEventImages.contains(imageXX)) {
                    listOfEventImages.add(imageXX)
                }
            }
        }

        listOfEventImages.forEachIndexed { index, userAdsImage ->
            when (index) {
                0 -> {
                    image1Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}"
                    context?.let {
                        binding?.addImage?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it1)
                        }
                    }
                    binding?.image1CardView?.visibility = View.VISIBLE

                    /*  context?.let {
                      evenDetailsBinding?.addImage?.let { it1 ->
                          Glide.with(it)
                              .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                              .into(it1)
                      }
                  }*/

                    context?.let {
                        binding?.image1?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it1)
                        }
                    }
                    changeCardViewBorder(0)
                }
                1 -> {
                    image2Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}"

                    binding?.image2CardView?.visibility = View.VISIBLE

                    /*  context?.let {
                      evenDetailsBinding?.addImage?.let { it1 ->
                          Glide.with(it)
                              .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                              .into(it1)
                      }
                  }*/

                    context?.let {
                        binding?.image2?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it1)
                        }
                    }
                }
                2 -> {
                    image3Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}"

                    binding?.image3CardView?.visibility = View.VISIBLE

                    /* context?.let {
                     evenDetailsBinding?.addImage?.let { it1 ->
                         Glide.with(it)
                             .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                             .into(it1)
                     }
                 }*/

                    context?.let {
                        binding?.image3?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable?>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding?.eventImagePlaceHolder?.visibility =
                                            View.VISIBLE
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable?>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        return false
                                    }
                                })
                                .into(it1)
                        }
                    }
                }
                3 -> {
                    image4Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}"

                    binding?.image4CardView?.visibility = View.VISIBLE

                    /* context?.let {
                     evenDetailsBinding?.addImage?.let { it1 ->
                         Glide.with(it)
                             .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                             .into(it1)
                     }
                 }*/

                    context?.let {
                        binding?.image4?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable?>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding?.eventImagePlaceHolder?.visibility =
                                            View.VISIBLE
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable?>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        return false
                                    }
                                })
                                .into(it1)
                        }
                    }
                }
            }
        }

        if (event.fee > 0) {
            val random = event.fee

            val df = DecimalFormat("#,###.00")
            df.roundingMode = RoundingMode.DOWN
            val roundoff = df.format(random)

            binding?.eventPriceTv?.text = "$$roundoff"
        } else {
            binding?.eventPriceTv?.text = "FREE"
        }
        binding?.eventTitle?.text = event.title
        binding?.eventDescTv?.textSize = 14F
        if (event.description != null && event.description.isNotEmpty()) {
            binding?.eventDescTv?.text = Html.fromHtml(event.description)
        }
        binding?.locationIconEvent?.visibility = View.VISIBLE
        val address =
            "${if (!event.address1.isNullOrEmpty()) event.address1 + ", " else ""} ${if (!event.address2.isNullOrEmpty()) event.address2 + ", " else ""} ${if (!event.city.isNullOrEmpty()) event.city + ", " else ""} ${if (!event.state.isNullOrEmpty()) event.state + ", " else ""}"
        val text2: String = "$address ${if (!event.zipCode.isNullOrEmpty()) event.zipCode else ""}"
        eventLocationText = text2

        val spannable: Spannable = SpannableString(text2)

        if (event.zipCode == null || event.zipCode.isEmpty()) {
            eventLocationText = "This is an online event"
            binding?.locationEventTv?.text = eventLocationText
        } else {
            binding?.locationEventTv?.setText(spannable, TextView.BufferType.SPANNABLE)
        }


        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.zipcodecolor)),
            address.length,
            (text2).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //evenDetailsBinding?.eventLocationZip?.text = event.zipCode
        binding?.eventCategoryTv?.text = "Category: " + event.category
        binding?.eventDetailsBottom?.visibility = View.VISIBLE
        if (!event.socialMediaLink.isNullOrEmpty()) {
            binding?.buyTicket?.visibility = View.VISIBLE
            //evenDetailsBinding?.premiumLink?.text = event.socialMediaLink
        } else {
            binding?.buyTicket?.visibility = View.GONE
        }
        binding?.totalVisitingTv?.text = event.totalVisiting.toString() + " going"
        binding?.totalFavoriteTv?.text = event.totalFavourite.toString() + " Interested"
        binding?.totalVisiting?.text = event.totalVisiting.toString()
        binding?.totalFavourite?.text = event.totalFavourite.toString()

        binding?.premiumLink?.setOnClickListener {
            if (URLUtil.isValidUrl(eventPremiumLink)) {
                val action =
                    EventDetailsScreenFragmentDirections.actionEventDetailsScreenFragmentToAdvertiseWebviewFragment(
                        eventPremiumLink
                    )
                findNavController().navigate(action)
            } else {
                showAlert("Invalid link")
            }
        }

        try {
            binding?.postedDate1?.text = "${
                DateTimeFormatter.ofPattern("MM-dd-yyyy")
                    .format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            .parse(event.startDate.split("T")[0])
                    )
            }, ${
                LocalTime.parse(event.startTime)
                    .format(DateTimeFormatter.ofPattern("h:mma"))
            } ${event.timeZone}"
            binding?.postedDate2?.text = " ${
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
            binding?.goingBtn?.setText("GOING")
        } else {
            binding?.goingBtn?.setText("NOT GOING")
        }

        if (isInterested) {
            binding?.interestedBtn?.setText("INTERESTED")
        } else {
            binding?.interestedBtn?.setText("NOT INTERESTED")
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
                binding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image4CardView?.setStrokeColor(
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
                binding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                binding?.image2CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image4CardView?.setStrokeColor(
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
                binding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image2CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                binding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image4CardView?.setStrokeColor(
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
                binding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image3CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                binding?.image4CardView?.setStrokeColor(
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
                binding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image3CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                binding?.image4CardView?.setStrokeColor(
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

    override fun onResume() {
        super.onResume()
        runAutoScrollBanner()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScrollBanner()
    }

    fun stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask!!.cancel()
            timer!!.cancel()
            timer = null
            timerTask = null
            adRvposition = layoutManager.findFirstCompletelyVisibleItemPosition()
        }
    }

    fun runAutoScrollBanner() {
        if (timer == null && timerTask == null && adsGenericAdapter?.items?.size!! >= 3) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    if (adRvposition == Int.MAX_VALUE) {
                        adRvposition = Int.MAX_VALUE / 2
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition)
                    } else {
                        adRvposition += 2
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition)
                    }
                }
            }
            timer!!.schedule(timerTask, 4000, 4000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        postEventViewModel.eventDetailsData.value = null
        stopAutoScrollBanner()
        postEventViewModel.deleteEventData.value = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
