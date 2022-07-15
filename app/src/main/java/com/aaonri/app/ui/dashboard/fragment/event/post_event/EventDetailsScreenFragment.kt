package com.aaonri.app.ui.dashboard.fragment.event.post_event

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.event.model.EventDetailsResponse
import com.aaonri.app.data.event.model.ImageXX
import com.aaonri.app.data.event.viewmodel.EventViewModel
import com.aaonri.app.data.event.viewmodel.PostEventViewModel
import com.aaonri.app.databinding.FragmentEventDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.event.EventScreenFragmentDirections
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class EventDetailsScreenFragment : Fragment() {
    val args: EventDetailsScreenFragmentArgs by navArgs()
    var evenDetailsBinding: FragmentEventDetailsBinding? = null
    val postEventViewModel: PostEventViewModel by activityViewModels()
    var eventPremiumLink: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        evenDetailsBinding = FragmentEventDetailsBinding.inflate(inflater, container, false)

        evenDetailsBinding?.apply {

            if (args.isMyEvent) {
                moreBtn.visibility = View.VISIBLE
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

            moreBtn.setOnClickListener {
                val action =
                    EventDetailsScreenFragmentDirections.actionEventDetailsScreenFragmentToUpdateDeleteClassifiedBottom(
                        args.eventId,
                        false
                    )
                findNavController().navigate(action)
            }

        }

        postEventViewModel.eventDetailsData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Resource.Loading -> {
                    evenDetailsBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    evenDetailsBinding?.progressBar?.visibility = View.GONE

                    response.data?.let { setEventdDetails(it) }
                    //Toast.makeText(context, "${response.data?.favourite}", Toast.LENGTH_SHORT).show()
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


        return evenDetailsBinding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setEventdDetails(event: EventDetailsResponse) {
        eventPremiumLink = event.socialMediaLink
        /*if (eventPremiumLink.isEmpty()) {
            evenDetailsBinding?.buyTicket?.visibility = View.GONE
        } else {
            evenDetailsBinding?.buyTicket?.visibility = View.VISIBLE
        }*/
        //event.images.sortedWith(compareByDescending { it.imageId })
        if(event.images.size>1) {
            event.images.forEachIndexed { index, userAdsImage ->
                if (userAdsImage.imagePath.contains(".cover") || userAdsImage.imagePath.contains(".first") || userAdsImage.imagePath.contains(
                        ".second"
                    ) || userAdsImage.imagePath.contains(".third")
                ) {
                    /*if (userAdsImage.imagePath.contains(".cover")) {
                    evenDetailsBinding?.image1CardView?.visibility = View.VISIBLE

                    *//*  context?.let {
                      evenDetailsBinding?.addImage?.let { it1 ->
                          Glide.with(it)
                              .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                              .into(it1)
                      }
                  }*//*
                    context?.let {
                        evenDetailsBinding?.addImage?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .into(it1)
                        }
                    }
                    context?.let {
                        evenDetailsBinding?.image1?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/eventFile/${userAdsImage.imagePath}")
                                .into(it1)
                        }
                    }
                }*/
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
        evenDetailsBinding?.locationEventTv?.text = event.city
        evenDetailsBinding?.eventLocationZip?.text = event.zipCode
        evenDetailsBinding?.eventCategoryTv?.text = "Category: " + event.category
        evenDetailsBinding?.premiumLink?.text = event.socialMediaLink
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

    override fun onResume() {
        super.onResume()
        postEventViewModel.getEventDetails(args.eventId)
    }

    override fun onDestroy() {
        super.onDestroy()
        postEventViewModel.eventDetailsData.value = null
    }
}