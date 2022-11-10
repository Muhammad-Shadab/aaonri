package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import coil.load
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.classified.ClassifiedStaticData
import com.aaonri.app.data.classified.model.LikeDislikeClassifiedRequest
import com.aaonri.app.data.classified.model.UserAdsXX
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.data.main.ActiveAdvertiseStaticData
import com.aaonri.app.data.main.adapter.AdsGenericAdapter
import com.aaonri.app.databinding.FragmentClassifiedDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class ClassifiedDetailsFragment : Fragment() {
    var binding: FragmentClassifiedDetailsBinding? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by activityViewModels()
    val args: ClassifiedDetailsFragmentArgs by navArgs()
    var adsGenericAdapter: AdsGenericAdapter? = null
    var isClassifiedLike = false
    var isUserLogin: Boolean? = null
    var itemId = 0
    var isEmailAvailable = ""
    var isPhoneAvailable = ""
    private lateinit var layoutManager: LinearLayoutManager
    var adRvposition = 0
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    var image1Link = ""
    var image2Link = ""
    var image3Link = ""
    var image4Link = ""

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentClassifiedDetailsBinding.inflate(inflater, container, false)

        isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }

        val guestUserLoginDialog = Dialog(requireContext())
        guestUserLoginDialog.setContentView(R.layout.guest_user_login_dialog)
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

        val ss = SpannableString(resources.getString(R.string.login_to_view_seller_information))
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity?.finish()
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }
        ss.setSpan(clickableSpan1, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        postClassifiedViewModel.getClassifiedAdDetails(args.addId)

        adsGenericAdapter = AdsGenericAdapter()
        adsGenericAdapter?.itemClickListener = { view, item, position ->
            if (item is FindAllActiveAdvertiseResponseItem) {

                val action =
                    ClassifiedDetailsFragmentDirections.actionClassifiedDetailsFragmentToAdvertiseWebviewFragment(
                        item.advertisementDetails.url
                    )
                findNavController().navigate(action)
            }
        }

        binding?.apply {

            loginToViewSellerInfo.text = ss
            loginToViewSellerInfo.movementMethod = LinkMovementMethod.getInstance()

            val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

            if (email != null) {
                classifiedViewModel.getClassifiedLikeDislikeInfo(email, args.addId, "Classified")
            }

            if (ActiveAdvertiseStaticData.getAdvertiseOnClassifiedDetails().isNotEmpty()) {
                adsGenericAdapter?.items =
                    ActiveAdvertiseStaticData.getAdvertiseOnClassifiedDetails()

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

            val bottomSheetOuter = BottomSheetBehavior.from(classifiedDetailsBottom)

            linear.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    linear.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val hiddenView: View = linear.getChildAt(0)
                    bottomSheetOuter.peekHeight = hiddenView.bottom
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
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    return
                }
            })

            navigateBack.setOnClickListener {
                postClassifiedViewModel.classifiedAdDetailsData.value = null
                findNavController().navigateUp()
            }

            likeDislikeBtn.setOnClickListener {
                if (isUserLogin == true) {
                    isClassifiedLike = !isClassifiedLike
                    if (isClassifiedLike) {
                        likeDislikeBtn.load(R.drawable.heart)
                        callLikeDislikeApi()
                    } else {
                        likeDislikeBtn.load(R.drawable.heart_grey)
                        callLikeDislikeApi()
                    }
                    classifiedViewModel.setCallClassifiedApiAfterDelete(true)
                } else {
                    guestUserLoginDialog.show()
                }
            }

            moreClassifiedOption.setOnClickListener {
                if (isUserLogin == true) {
                    val action =
                        ClassifiedDetailsFragmentDirections.actionClassifiedDetailsFragmentToUpdateDeleteClassifiedBottom(
                            args.addId,
                            true
                        )
                    findNavController().navigate(action)
                } else {
                    guestUserLoginDialog.show()
                }
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
                    intent.data = Uri.parse("tel:$isPhoneAvailable")
                    startActivity(intent)
                }
            }

            shareBtn.setOnClickListener {

                try {
                    val myBitmap = addImage.drawable.toBitmap()
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "Image/jpeg"
                    share.type = "text/html"
                    val baseUrl = BuildConfig.BASE_URL.replace(":8444", "")
                    val shareSub = "${baseUrl}/classified/details/${args.addId}"
                    share.putExtra(Intent.EXTRA_TEXT, shareSub)
                    val bytes = ByteArrayOutputStream()
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path = MediaStore.Images.Media.insertImage(
                        activity?.getContentResolver(),
                        myBitmap,
                        "Title",
                        null
                    )
                    val imageUri = Uri.parse(path)
                    share.putExtra(Intent.EXTRA_STREAM, imageUri)
                    activity?.startActivity(Intent.createChooser(share, "Select"))
                } catch (e: Exception) {

                }

                /*if (isUserLogin == true) {




                    *//*val bitmap = addImage.drawable.toBitmap()
                    val shareIntent: Intent
                    var path =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .toString() + "/Share.png"
                    var out: OutputStream
                    val file = File(path)
                    try {
                        out = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                        out.flush()
                        out.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    path = file.path
                    val bmpUri = Uri.parse(path)
                    shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    val baseUrl = BuildConfig.BASE_URL.replace(":8444", "")
                    val shareSub = "${baseUrl}/classified/details/${args.addId}"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareSub)
                    shareIntent.type = "image/png"
                    startActivity(Intent.createChooser(shareIntent, "Share with"))*//*
                } else {
                    guestUserLoginDialog.show()
                }*/
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
                    }/classified/details/${args.addId}"
                )
                emailIntent.selector = selectorIntent

                activity?.startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }

        }

        postClassifiedViewModel.classifiedAdDetailsData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        response.data?.let {
                            it.userAds?.let { it1 -> setClassifiedDetails(it1) }
                            ClassifiedStaticData.updateAddDetails(it)
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }

        classifiedViewModel.classifiedSellerNameData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    binding?.sellerName?.text =
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

        postClassifiedViewModel.classifiedDeleteData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        findNavController().navigateUp()
                        postClassifiedViewModel.classifiedDeleteData.postValue(null)
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }

        classifiedViewModel.callClassifiedDetailsApiAfterUpdating.observe(viewLifecycleOwner) {
            if (it) {
                /**this is used to call details api after updation classified but now it will navigate to back and when user open details page then detail api call automatically**/
                //postClassifiedViewModel.getClassifiedAdDetails(args.addId)
                classifiedViewModel.setCallClassifiedDetailsApiAfterUpdating(false)
                findNavController().navigateUp()
            }
        }

        postClassifiedViewModel.classifiedDeleteData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    findNavController().navigateUp()
                    classifiedViewModel.setCallClassifiedApiAfterDelete(true)
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setClassifiedDetails(data: UserAdsXX) {

        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        if (data.userId == email) {
            binding?.reportInappropriateTv?.visibility = View.GONE
            if (!data.approved) {
                binding?.moreClassifiedOption?.visibility = View.VISIBLE
            }
        }

        //data.userAdsImages.sortedWith(compareByDescending { it.sequenceNumber })

        data.userAdsImages.forEachIndexed { index, userAdsImage ->
            when (index) {
                0 -> {
                    image1Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}"

                    binding?.image1CardView?.visibility = View.VISIBLE

                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[0].imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it)
                        }
                    }

                    context?.let {
                        binding?.image1?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it1)
                        }
                    }

                }
                1 -> {
                    image2Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}"

                    binding?.image2CardView?.visibility = View.VISIBLE

                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[1].imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it)
                        }
                    }

                    context?.let {
                        binding?.image2?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it1)
                        }
                    }

                }
                2 -> {
                    image3Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}"

                    binding?.image3CardView?.visibility = View.VISIBLE
                    context?.let {
                        binding?.image3?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it1)
                        }
                    }
                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[2].imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it)
                        }
                    }
                }
                3 -> {
                    image4Link =
                        "${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}"

                    binding?.image4CardView?.visibility = View.VISIBLE
                    context?.let {
                        binding?.image4?.let { it1 ->
                            Glide.with(it)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it1)
                        }
                    }

                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[3].imagePath}")
                                .error(R.drawable.small_image_placeholder)
                                .into(it)
                        }
                    }
                }
            }


            /*if (userAdsImage.sequenceNumber == 1) {
                binding?.image1CardView?.visibility = View.VISIBLE

                context?.let {
                    binding?.image1?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .error(R.drawable.small_image_placeholder)
                            .into(it1)
                    }
                }
            }
            if (userAdsImage.sequenceNumber == 2) {
                binding?.image2CardView?.visibility = View.VISIBLE
                context?.let {
                    binding?.image2?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .error(R.drawable.small_image_placeholder)
                            .into(it1)
                    }
                }
            }
            if (userAdsImage.sequenceNumber == 3) {
                binding?.image3CardView?.visibility = View.VISIBLE
                context?.let {
                    binding?.image3?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .error(R.drawable.small_image_placeholder)
                            .into(it1)
                    }
                }
            }
            if (userAdsImage.sequenceNumber == 4) {
                binding?.image4CardView?.visibility = View.VISIBLE
                context?.let {
                    binding?.image4?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
                            .error(R.drawable.small_image_placeholder)
                            .into(it1)
                    }
                }
            }*/
        }

        if (data.userAdsImages.isNotEmpty()) {
            binding?.addImage?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[0].imagePath}")
                        .error(R.drawable.small_image_placeholder)
                        .into(it)
                }
            }
        }


        val random = data.askingPrice

        val df = DecimalFormat("#,###.00")
        df.roundingMode = RoundingMode.DOWN
        val roundoff = df.format(random)
        binding?.constraint1?.visibility = View.VISIBLE
        binding?.classifiedPriceTv?.text = "$$roundoff"
        binding?.addTitle?.text = data.adTitle
        binding?.addTitle?.visibility = View.VISIBLE
        binding?.navigateBack?.visibility = View.VISIBLE
        binding?.classifiedDescTv?.textSize = 14F
        if (data.adDescription != null && data.adDescription.isNotEmpty()) {
            binding?.classifiedDescTv?.text = Html.fromHtml(data.adDescription)
        }

        binding?.classifiedLocationDetails?.text =
            data.adLocation + " - " + data.adZip

        binding?.postedDate1?.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(data.createdOn.split("T")[0]))
        binding?.postedDate2?.text = DateTimeFormatter.ofPattern("MM-dd-yyyy")
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(data.adExpireDT.split("T")[0]))

        itemId = data.id

        if (data.userAdsImages.isEmpty()) {
            changeCardViewBorder(9)
        } else {
            changeCardViewBorder(0)
        }

        if (data.popularOnAaonri) {
            binding?.popularTv?.visibility = View.VISIBLE
        } else {
            binding?.popularTv?.visibility = View.GONE
        }

        binding?.classifiedCategoryTv?.text =
            "Category: ${data.category}  |  Sub Category: ${data.subCategory}"
        binding?.classifiedLocation?.visibility = View.VISIBLE
        binding?.classifiedPostDate?.visibility = View.VISIBLE
        val address =
            "${if (!data.adLocation.isNullOrEmpty()) data.adLocation else ""}"
        val text2: String = "$address ${if (!data.adZip.isNullOrEmpty()) data.adZip else ""}"

        val spannable: Spannable = SpannableString(text2)

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.zipcodecolor)),
            address.length,
            (text2).length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding?.locationClassifiedTv?.setText(
            spannable,
            TextView.BufferType.SPANNABLE
        )


        if (data.contactType == "Email") {
            if (data.adEmail.isNotEmpty()) {
                classifiedViewModel.getClassifiedSellerName(data.adEmail)
                isEmailAvailable = data.adEmail
                isPhoneAvailable = ""
                binding?.emailTv?.text = "Email"
                //binding?.classifiedSellerEmail?.text = data.adEmail
                val content = SpannableString(data.adEmail)
                content.setSpan(UnderlineSpan(), 0, data.adEmail.length, 0);
                binding?.classifiedSellerEmail?.setText(content)
            }
        } else {
            classifiedViewModel.getClassifiedSellerName(data.userId)
            isEmailAvailable = ""
            isPhoneAvailable = data.adPhone
            binding?.emailTv?.text = "Phone"
            binding?.classifiedSellerEmail?.text = data.adPhone
            val content = SpannableString(data.adPhone)
            content.setSpan(UnderlineSpan(), 0, data.adPhone.length, 0);
            binding?.classifiedSellerEmail?.setText(content)
        }

        /*if (data.adEmail.isNotEmpty()) {
            isEmailAvailable = data.adEmail
            isPhoneAvailable = data.adPhone
            binding?.emailTv?.text = "Email"
            binding?.classifiedSellerEmail?.text = data.adEmail
        } else {
            isEmailAvailable = data.adEmail
            isPhoneAvailable = data.adPhone
            binding?.emailTv?.text = "Phone"
            binding?.classifiedSellerEmail?.text = data.adPhone
        }*/

        if (isUserLogin == false) {
            binding?.sellerInformationLayout?.visibility = View.GONE
            //classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.GONE
            binding?.loginToViewSellerInfo?.visibility = View.VISIBLE
        } else {
            binding?.sellerInformationLayout?.visibility = View.VISIBLE
            //classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.VISIBLE
        }


        classifiedViewModel.classifiedLikeDislikeInfoData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (response.data.toBoolean()) {
                        binding?.likeDislikeBtn?.load(R.drawable.heart)
                    } else {
                        binding?.likeDislikeBtn?.load(R.drawable.heart_grey)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
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
        classifiedViewModel.setIsLikedButtonClicked(true)
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

    override fun onDestroy() {
        super.onDestroy()
        postClassifiedViewModel.classifiedAdDetailsData.postValue(null)
        postClassifiedViewModel.classifiedDeleteData.postValue(null)
        stopAutoScrollBanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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


}

