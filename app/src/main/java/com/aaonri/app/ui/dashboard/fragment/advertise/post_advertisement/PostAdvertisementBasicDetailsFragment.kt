package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertisementbasicDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.classified.RichTextEditor
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAdvertisementBasicDetailsFragment : Fragment(), AdapterView.OnItemClickListener {
    var advertiseBinding: FragmentPostAdvertisementbasicDetailsBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var templateImage = ""
    var description: String? = ""

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("result")
                if (data?.isNotEmpty() == true) {
                    advertiseBinding?.advertiseDescEt?.fromHtml(data.trim())
                    description = data.trim()
                } else {
                    advertiseBinding?.advertiseDescEt?.text = ""
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        advertiseBinding =
            FragmentPostAdvertisementbasicDetailsBinding.inflate(inflater, container, false)

        advertiseBinding?.apply {

            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_BASIC_DETAILS)

            selectedPage.text = postAdvertiseViewModel.selectedTemplatePageName?.pageName
            description?.let {
                advertiseDescEt.fromHtml(it)
            }

            chooseTemplatell.setOnClickListener {
                if (templateImage.isEmpty()) {
                    ImagePicker.with(requireActivity())
                        .compress(1024)
                        .crop()
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                            progressBarBasicDetails.visibility = View.VISIBLE
                        }
                }
            }

            advertiseDetailsNextBtn.setOnClickListener {
                if (titleAdvertisedEt.text.toString().length >= 3) {
                    saveDataToViewModel(
                        titleAdvertisedEt.text.toString(),
                        selectedPage.text.toString(),
                        selectAdvertiseDaysSpinner.toString(),
                        planChargeEt.text.toString(),
                        costOfvalueEt.text.toString(),
                        emailPromotionalCheckbox.isChecked,
                        flashingAdvertiseCheckbox.isChecked,
                        selectAdvertiseTemplateSpinner.toString(),
                        templateImage
                    )
                    findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)
                } else {
                    showAlert("Please enter valid ad title")
                }
            }

            previewAdvertiseBtn.setOnClickListener {
                if (titleAdvertisedEt.text.toString().length >= 3) {
                    saveDataToViewModel(
                        titleAdvertisedEt.text.toString(),
                        selectedPage.text.toString(),
                        selectAdvertiseDaysSpinner.toString(),
                        planChargeEt.text.toString(),
                        costOfvalueEt.text.toString(),
                        emailPromotionalCheckbox.isChecked,
                        flashingAdvertiseCheckbox.isChecked,
                        selectAdvertiseTemplateSpinner.toString(),
                        templateImage
                    )
                    findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_reviewAdvertiseFragment)
                } else {
                    showAlert("Please enter valid ad title")
                }

            }

            advertiseDescEt.setOnClickListener {
                val intent = Intent(context, RichTextEditor::class.java)
                intent.putExtra("data", description)
                intent.putExtra("placeholder", "Ad description")
                resultLauncher.launch(intent)
            }

        }

        if (postAdvertiseViewModel.isUpdateAdvertise) {
            setDataForUpdating()
        }

        return advertiseBinding?.root
    }

    private fun setDataForUpdating() {
        val advertiseData = AdvertiseStaticData.getAddDetails()
        advertiseBinding?.apply {
            titleAdvertisedEt.setText(advertiseData?.advertisementDetails?.adTitle)
            selectedPage.text = advertiseData?.advertisementPageLocation?.locationName
            when (advertiseData?.locationPlanRate?.days) {
                7 -> {
                    selectAdvertiseDaysSpinner.setSelection(0)
                }
                15 -> {
                    selectAdvertiseDaysSpinner.setSelection(1)
                }
                30 -> {
                    selectAdvertiseDaysSpinner.setSelection(2)
                }
            }
            //planChargeEt.setText()
            if (advertiseData?.advertisementDetails?.adImage.isNullOrEmpty()) {
                advertiseBinding?.advertiseIv?.visibility = View.GONE
                advertiseBinding?.uploadImageTv?.visibility = View.VISIBLE
                advertiseBinding?.sizeLimitTv?.visibility = View.VISIBLE
            } else {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load("${BuildConfig.BASE_URL}/api/v1/common/advertisementFile/${advertiseData?.advertisementDetails?.adImage}")
                        .into(advertiseIv)
                }
                advertiseBinding?.advertiseIv?.visibility = View.VISIBLE
                advertiseBinding?.uploadImageTv?.visibility = View.GONE
                advertiseBinding?.sizeLimitTv?.visibility = View.GONE
            }
            advertiseDescEt.fromHtml(advertiseData?.advertisementDetails?.companyDescription)
        }
    }

    private fun saveDataToViewModel(
        addTitle: String,
        templateName: String,
        advertiseValidity: String,
        planCharges: String,
        costOfValue: String,
        isEmailPromotional: Boolean,
        isFlashingAdvertisement: Boolean,
        templateLocation: String,
        advertiseImageUri: String
    ) {
        description?.let {
            postAdvertiseViewModel.addCompanyBasicDetailsMap(
                addTitle = addTitle,
                templateName = templateName,
                advertiseValidity = advertiseValidity,
                planCharges = planCharges,
                costOfValue = costOfValue,
                isEmailPromotional = isEmailPromotional,
                isFlashingAdvertisement = isFlashingAdvertisement,
                templateLocation = templateLocation,
                advertiseImageUri = advertiseImageUri,
                description = it
            )
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                val fileUri = data?.data!!

                templateImage = fileUri.toString()
                advertiseBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
                setImage()

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                advertiseBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
            }
        }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        Toast.makeText(context, p0?.getItemAtPosition(p2).toString(), Toast.LENGTH_SHORT).show()
    }

    private fun setImage() {
        if (templateImage.isNotEmpty()) {
            advertiseBinding?.advertiseIv?.visibility = View.VISIBLE
            advertiseBinding?.uploadImageTv?.visibility = View.GONE
            advertiseBinding?.sizeLimitTv?.visibility = View.GONE

            advertiseBinding?.advertiseIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(templateImage)
                        .into(it)
                }
            }
        } else {
            advertiseBinding?.advertiseIv?.visibility = View.GONE
            advertiseBinding?.uploadImageTv?.visibility = View.VISIBLE
            advertiseBinding?.sizeLimitTv?.visibility = View.VISIBLE
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

}