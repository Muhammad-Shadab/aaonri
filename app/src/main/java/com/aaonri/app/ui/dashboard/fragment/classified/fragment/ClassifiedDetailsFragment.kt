package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
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
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
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


                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                bottomAdvertiseRv.layoutManager = layoutManager
                bottomAdvertiseRv.adapter = adsGenericAdapter
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
                } else {
                    showSnckBar()
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
                    intent.type = "text/plain"
                    val baseUrl = BuildConfig.BASE_URL.replace(":8444", "")
                    val shareSub = "${baseUrl}/classified/details/${args.addId}"
                    intent.putExtra(Intent.EXTRA_TEXT, shareSub)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        postClassifiedViewModel.classifiedAdDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE

                    response.data?.let {
                        setClassifiedDetails(it.userAds)
                        ClassifiedStaticData.updateAddDetails(it)
                    }
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
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    findNavController().navigateUp()
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        classifiedViewModel.callClassifiedDetailsApiAfterUpdating.observe(viewLifecycleOwner) {
            if (it) {
                postClassifiedViewModel.getClassifiedAdDetails(args.addId)
                classifiedViewModel.setCallClassifiedDetailsApiAfterUpdating(false)
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
            if (!data.approved) {
                binding?.moreClassifiedOption?.visibility = View.VISIBLE
            }
        }
        data.userAdsImages.sortedWith(compareByDescending { it.sequenceNumber })
        data.userAdsImages.forEachIndexed { index, userAdsImage ->

            when (index) {
                0 -> {
                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[0].imagePath}")
                                .into(it)
                        }
                    }

                }
                1 -> {
                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[1].imagePath}")
                                .into(it)
                        }
                    }
                }
                2 -> {
                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[2].imagePath}")
                                .into(it)
                        }
                    }
                }
                3 -> {
                    binding?.addImage?.let {
                        context?.let { it1 ->
                            Glide.with(it1)
                                .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[3].imagePath}")
                                .into(it)
                        }
                    }
                }
            }


            if (userAdsImage.sequenceNumber == 1) {
                binding?.image1CardView?.visibility = View.VISIBLE

                context?.let {
                    binding?.image1?.let { it1 ->
                        Glide.with(it)
                            .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${userAdsImage.imagePath}")
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
                            .into(it1)
                    }
                }
            }
        }

        if (data.userAdsImages.isNotEmpty()) {
            binding?.addImage?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data.userAdsImages[0].imagePath}")
                        .into(it)
                }
            }
        }

        binding?.image1?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 0) {

                    binding?.addImage?.let {
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



        binding?.image2?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 1) {
                    binding?.addImage?.let {
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
        binding?.image3?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 2) {
                    binding?.addImage?.let {
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
        binding?.image4?.setOnClickListener {
            data.userAdsImages.forEachIndexed { index, userAdsImage ->
                if (index == 3) {
                    binding?.addImage?.let {
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

        val df = DecimalFormat("#,###.00")
        df.roundingMode = RoundingMode.DOWN
        val roundoff = df.format(random)
        binding?.constraint1?.visibility = View.VISIBLE
        binding?.classifiedPriceTv?.text = "$$roundoff"
        binding?.addTitle?.text = data.adTitle
        binding?.addTitle?.visibility = View.VISIBLE
        binding?.navigateBack?.visibility = View.VISIBLE
        binding?.classifiedDescTv?.textSize = 14F
        binding?.classifiedDescTv?.text = Html.fromHtml(data.adDescription)
        binding?.classifiedLocationDetails?.text =
            data.adLocation + " - " + data.adZip
        binding?.sellerName?.text =
            classifiedViewModel.getClassifiedSellerName(data.adEmail).toString()

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


        if (data.adEmail.isNotEmpty()) {
            isEmailAvailable = data.adEmail
            isPhoneAvailable = data.adPhone
            binding?.emailTv?.text = "Email"
            binding?.classifiedSellerEmail?.text = data.adEmail
        } else {
            isEmailAvailable = data.adEmail
            isPhoneAvailable = data.adPhone
            binding?.emailTv?.text = "Phone"
            binding?.classifiedSellerEmail?.text = data.adPhone
        }

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

    fun getScreenWidth(): Int {
        return Resources.getSystem().getDisplayMetrics().widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().getDisplayMetrics().heightPixels
    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.getResources().getDisplayMetrics().density
    }


    override fun onDestroy() {
        super.onDestroy()
        postClassifiedViewModel.classifiedAdDetailsData.value = null
        postClassifiedViewModel.classifiedDeleteData.value = null
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
                        binding?.bottomAdvertiseRv?.scrollToPosition(adRvposition)

                    } else {
                        adRvposition += 2
                        binding?.bottomAdvertiseRv?.smoothScrollToPosition(adRvposition)
                    }
                }
            }
            timer!!.schedule(timerTask, 4000, 4000)
        }


    }

    fun showSnckBar() {
        val snackbar =
            binding?.let {
                Snackbar.make(it.mainCl, "Please Login First", Snackbar.LENGTH_SHORT)
                    .setActionTextColor(
                        resources.getColor(
                            R.color
                                .lightRedColor
                        )
                    )
                    .setAction(
                        "Login"
                    ) {
                        activity?.finish()
                    }
            }
        snackbar?.show()
    }

}

