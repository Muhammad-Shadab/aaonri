package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.aaonri.app.data.classified.model.LikeDislikeClassifiedRequest
import com.aaonri.app.data.classified.model.UserAdsXX
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentClassifiedDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class ClassifiedDetailsFragment : Fragment() {
    var classifiedDetailsBinding: FragmentClassifiedDetailsBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    val args: ClassifiedDetailsFragmentArgs by navArgs()
    var isClassifiedLike = false
    var itemId = 0
    var isEmailAvailable = ""
    var isPhoneAvailable = ""

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedDetailsBinding =
            FragmentClassifiedDetailsBinding.inflate(inflater, container, false)

        classifiedDetailsBinding?.apply {

            val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

            if (email != null) {
                classifiedViewModel.getClassifiedLikeDislikeInfo(email, args.addId, "Classified")
            }

            if (args.isMyClassifiedScreen) {
                moreClassifiedOption.visibility = View.VISIBLE
            }

            val bottomSheetOuter = BottomSheetBehavior.from(classifiedDetailsBottom)

            bottomSheetOuter.peekHeight = 450
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
                findNavController().navigateUp()
            }

            likeDislikeBtn.setOnClickListener {
                isClassifiedLike = !isClassifiedLike
                if (isClassifiedLike) {
                    likeDislikeBtn.load(R.drawable.heart)
                    callLikeDislikeApi()
                } else {
                    likeDislikeBtn.load(R.drawable.heart_grey)
                    callLikeDislikeApi()
                }
            }

            moreClassifiedOption.setOnClickListener {
                val action =
                    ClassifiedDetailsFragmentDirections.actionClassifiedDetailsFragmentToUpdateDeleteClassifiedBottom(
                        args.addId,
                        true
                    )
                findNavController().navigate(action)
            }

            classifiedSellerEmail.setOnClickListener {
                if (isEmailAvailable.isNotEmpty()) {
                    val emailIntent = Intent(
                        Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", isEmailAvailable, null
                        )
                    )
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "")
                    startActivity(Intent.createChooser(emailIntent, "Send email..."))
                } else {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:<$isPhoneAvailable>")
                    startActivity(intent)
                }
            }
        }

        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            if (it) {
                classifiedDetailsBinding?.sellerInformationLayout?.visibility = View.GONE
                classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.GONE

            } else {
                classifiedDetailsBinding?.sellerInformationLayout?.visibility = View.VISIBLE
                classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.VISIBLE
            }
        }


        postClassifiedViewModel.classifiedAdDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    classifiedDetailsBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    classifiedDetailsBinding?.progressBar?.visibility = View.GONE

                    response.data?.let { setClassifiedDetails(it.userAds) }
                    //Toast.makeText(context, "${response.data?.favourite}", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    classifiedDetailsBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }

        classifiedViewModel.classifiedSellerNameData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    classifiedDetailsBinding?.sellerName?.text =
                        response.data?.firstName + " " + response.data?.lastName
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }


        return classifiedDetailsBinding?.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setClassifiedDetails(data: UserAdsXX) {
        data.userAdsImages.sortedWith(compareByDescending { it.sequenceNumber })
        data.userAdsImages.forEachIndexed { index, userAdsImage ->

            when (index) {
                0 -> {
                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[0].imagePath}")
                                .into(it)
                        }
                    }
                }
                1 -> {
                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[1].imagePath}")
                                .into(it)
                        }
                    }
                }
                2 -> {
                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[2].imagePath}")
                                .into(it)
                        }
                    }
                }
                3 -> {
                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[3].imagePath}")
                                .into(it)
                        }
                    }
                }
            }

            if (userAdsImage.sequenceNumber == 1) {
                classifiedDetailsBinding?.image1CardView?.visibility = View.VISIBLE

                context?.let {
                    classifiedDetailsBinding?.image1?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .into(it1)
                    }
                }
            }
            if (userAdsImage.sequenceNumber == 2) {

                classifiedDetailsBinding?.image2CardView?.visibility = View.VISIBLE
                context?.let {
                    classifiedDetailsBinding?.image2?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .into(it1)
                    }
                }
            }
            if (userAdsImage.sequenceNumber == 3) {

                classifiedDetailsBinding?.image3CardView?.visibility = View.VISIBLE
                context?.let {
                    classifiedDetailsBinding?.image3?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .into(it1)
                    }
                }
            }
            if (userAdsImage.sequenceNumber == 4) {

                classifiedDetailsBinding?.image4CardView?.visibility = View.VISIBLE
                context?.let {
                    classifiedDetailsBinding?.image4?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .into(it1)
                    }
                }
            }
        }

        if (data.userAdsImages.isNotEmpty()) {
            classifiedDetailsBinding?.addImage?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[0].imagePath}")
                        .into(it)
                }
            }
        }


        /*     postClassifiedViewModel.sendFavoriteDataToClassifiedDetails.observe(viewLifecycleOwner) { userAds ->

                 classifiedViewModel.getClassifiedSellerName(userAds.adEmail)
                 classifiedDetailsBinding?.postedDate1?.text = DateTimeFormatter.ofPattern("dd MMM yyyy")
                     .format(
                         DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(userAds.createdOn.split("T")[0])
                     )
                 classifiedDetailsBinding?.postedDate2?.text = DateTimeFormatter.ofPattern("dd MMM yyyy")
                     .format(
                         DateTimeFormatter.ofPattern("yyyy-MM-dd")
                             .parse(userAds.adExpireDT.split("T")[0])
                     )

                 itemId = userAds.id

                 if (userAds.userAdsImages.isEmpty()) {
                     changeCardViewBorder(9)
                 } else {
                     changeCardViewBorder(0)
                 }


             }*/

        classifiedDetailsBinding?.image1?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 0) {

                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[0].imagePath}")
                                .into(it)
                        }
                    }


                    changeCardViewBorder(0)
                }
            }
        }



        classifiedDetailsBinding?.image2?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 1) {
                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[1].imagePath}")
                                .into(it)
                        }
                    }
                    changeCardViewBorder(1)
                }
            }
        }
        classifiedDetailsBinding?.image3?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 2) {
                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[2].imagePath}")
                                .into(it)
                        }
                    }
                    changeCardViewBorder(2)
                }
            }
        }
        classifiedDetailsBinding?.image4?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 3) {
                    classifiedDetailsBinding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[3].imagePath}")
                                .into(it)
                        }
                    }
                    changeCardViewBorder(3)
                }
            }
        }

        val random = data.askingPrice

        val df = DecimalFormat("###.00")
        df.roundingMode = RoundingMode.DOWN
        val roundoff = df.format(random)

        classifiedDetailsBinding?.classifiedPriceTv?.text = "$$roundoff"
        classifiedDetailsBinding?.addTitle?.text = data.adTitle
        classifiedDetailsBinding?.classifiedDescTv?.text = Html.fromHtml(data.adDescription)
        classifiedDetailsBinding?.classifiedLocationDetails?.text =
            data.adLocation + " - " + data.adZip
        classifiedDetailsBinding?.sellerName?.text =
            classifiedViewModel.getClassifiedSellerName(data.adEmail).toString()

        classifiedDetailsBinding?.postedDate1?.text = DateTimeFormatter.ofPattern("dd MMM yyyy")
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(data.createdOn.split("T")[0]))
        classifiedDetailsBinding?.postedDate2?.text = DateTimeFormatter.ofPattern("dd MMM yyyy")
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(data.adExpireDT.split("T")[0]))

        itemId = data.id

        if (data.userAdsImages.isEmpty()) {
            changeCardViewBorder(9)
        } else {
            changeCardViewBorder(0)
        }

        if (data.popularOnAaonri) {
            classifiedDetailsBinding?.popularTv?.visibility = View.VISIBLE
        } else {
            classifiedDetailsBinding?.popularTv?.visibility = View.GONE
        }

        classifiedDetailsBinding?.classifiedCategoryTv?.text =
            "Category: ${data.category}  |  Sub Category: ${data.subCategory}"
        classifiedDetailsBinding?.locationClassifiedTv?.text = data.adLocation
        classifiedDetailsBinding?.adZipCode?.text = data.adZip


        if (data.adEmail.isNotEmpty()) {
            isEmailAvailable = data.adEmail
            isPhoneAvailable = data.adPhone
            classifiedDetailsBinding?.emailTv?.text = "Email"
            classifiedDetailsBinding?.classifiedSellerEmail?.text = data.adEmail
        } else {
            isEmailAvailable = data.adEmail
            isPhoneAvailable = data.adPhone
            classifiedDetailsBinding?.emailTv?.text = "Phone"
            classifiedDetailsBinding?.classifiedSellerEmail?.text = data.adPhone
        }

        classifiedViewModel.classifiedLikeDislikeInfoData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (response.data.toBoolean()) {
                        classifiedDetailsBinding?.likeDislikeBtn?.load(R.drawable.heart)
                    } else {
                        classifiedDetailsBinding?.likeDislikeBtn?.load(R.drawable.heart_grey)
                    }

                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }

    }

    private fun callLikeDislikeApi() {
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        classifiedViewModel.likeDislikeClassified(
            LikeDislikeClassifiedRequest(
                emailId = email.toString(),
                favourite = isClassifiedLike,
                itemId = itemId,
                service = "Classified"
            )
        )
    }

    private fun changeCardViewBorder(selectedImageIndex: Int) {

        if (selectedImageIndex == 0) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setStrokeColor(
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
                classifiedDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setStrokeColor(
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
                classifiedDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setStrokeColor(
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
                classifiedDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setStrokeColor(
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
                classifiedDetailsBinding?.image1CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setStrokeColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setStrokeColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setStrokeColor(
                    it2
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        postClassifiedViewModel.getClassifiedAdDetails(args.addId)
    }

    override fun onDestroy() {
        super.onDestroy()
        postClassifiedViewModel.classifiedAdDetailsData.value = null
    }
}