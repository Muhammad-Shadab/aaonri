package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.ImageRequest
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.LikeDislikeClassifiedRequest
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedDetailsBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifiedDetailsFragment : Fragment() {
    var classifiedDetailsBinding: FragmentClassifiedDetailsBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    var isClassifiedLike = false
    var itemId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedDetailsBinding =
            FragmentClassifiedDetailsBinding.inflate(inflater, container, false)


        classifiedDetailsBinding?.apply {

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
        }
        postClassifiedViewModel.sendDataToClassifiedDetailsScreen.observe(viewLifecycleOwner) { userAds ->
            itemId = userAds.id
            callLikeDislikeApi()
            classifiedDetailsBinding?.apply {
                try {
                    userAds.userAdsImages.forEachIndexed { index, userAdsImage ->
                        when (index) {
                            0 -> {
                                addImage.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAdsImage.imagePath}") {
                                   // placeholder(R.drawable.ic_loading)

                                }
                                image1.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAdsImage.imagePath}") {
                                    placeholder(R.drawable.ic_image_placeholder)
                                }
                                context?.let { it1 ->
                                    ContextCompat.getColor(
                                        it1,
                                        R.color.selectedClassifiedCardViewBorder
                                    )
                                }?.let { it2 ->
                                    image1CardView.setBackgroundColor(
                                        it2
                                    )
                                }
                            }
                            1 -> {
                                image2.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAdsImage.imagePath}") {
                                    placeholder(R.drawable.ic_image_placeholder)
                                }
                            }
                            2 -> {
                                image3.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAdsImage.imagePath}") {
                                    placeholder(R.drawable.ic_image_placeholder)
                                }
                            }
                            3 -> {
                                image4.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAdsImage.imagePath}") {
                                    placeholder(R.drawable.ic_image_placeholder)
                                }
                            }
                        }
                    }
                    image1.setOnClickListener {
                        addImage.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAds.userAdsImages[0].imagePath}"){
                            //placeholder(R.drawable.ic_loading)
                        }

                        changeCardViewBg(0)

                        context?.let { it1 ->
                            ContextCompat.getColor(
                                it1,
                                R.color.selectedClassifiedCardViewBorder
                            )
                        }?.let { it2 ->
                            image1CardView.setBackgroundColor(
                                it2
                            )
                        }
                    }
                    image2.setOnClickListener {
                        addImage.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAds.userAdsImages[1].imagePath}"){
                            //placeholder(R.drawable.ic_loading)
                        }
                        changeCardViewBg(1)
                        context?.let { it1 ->
                            ContextCompat.getColor(
                                it1,
                                R.color.selectedClassifiedCardViewBorder
                            )
                        }?.let { it2 ->
                            image2CardView.setBackgroundColor(
                                it2
                            )
                        }
                    }
                    image3.setOnClickListener {
                        addImage.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAds.userAdsImages[2].imagePath}"){
                            //placeholder(R.drawable.ic_loading)
                        }
                        changeCardViewBg(2)
                        context?.let { it1 ->
                            ContextCompat.getColor(
                                it1,
                                R.color.selectedClassifiedCardViewBorder
                            )
                        }?.let { it2 ->
                            image3CardView.setBackgroundColor(
                                it2
                            )
                        }
                    }
                    image4.setOnClickListener {
                        changeCardViewBg(3)
                        addImage.load("https://www.aaonri.com/api/v1/common/classifiedFile/${userAds.userAdsImages[3].imagePath}"){
                            //placeholder(R.drawable.ic_loading)
                        }
                        context?.let { it1 ->
                            ContextCompat.getColor(
                                it1,
                                R.color.selectedClassifiedCardViewBorder
                            )
                        }?.let { it2 ->
                            image4CardView.setBackgroundColor(
                                it2
                            )
                        }
                    }

                } catch (e: Exception) {

                }
                classifiedPriceTv.text = "$" + userAds.askingPrice.toString()
                addTitle.text = userAds.adTitle
                classifiedCategoryTv.text =
                    "Category: ${userAds.category} | Sub Category: ${userAds.subCategory}"
                classifiedDescTv.text = Html.fromHtml(userAds.adDescription)
                classifiedLocationDetails.text = userAds.adLocation + " - " + userAds.adZip
                //sellerName.text = userAds.
                classifiedSellerEmail.text = userAds.adEmail

            }
        }

        classifiedViewModel.likeDislikeClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
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

        return classifiedDetailsBinding?.root
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

    private fun changeCardViewBg(selectedImageIndex: Int) {

        if (selectedImageIndex == 0) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setBackgroundColor(
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
                classifiedDetailsBinding?.image1CardView?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setBackgroundColor(
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
                classifiedDetailsBinding?.image1CardView?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image4CardView?.setBackgroundColor(
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
                classifiedDetailsBinding?.image1CardView?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image2CardView?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                classifiedDetailsBinding?.image3CardView?.setBackgroundColor(
                    it2
                )
            }
        }
    }
}