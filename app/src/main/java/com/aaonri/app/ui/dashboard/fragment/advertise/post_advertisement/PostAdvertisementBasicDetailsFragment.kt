package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.model.AdvertisePageLocationResponseItem
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertisementbasicDetailsBinding
import com.aaonri.app.ui.dashboard.fragment.classified.RichTextEditorActivity
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class PostAdvertisementBasicDetailsFragment : Fragment(), AdapterView.OnItemClickListener {
    var binding: FragmentPostAdvertisementbasicDetailsBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var advertiseImage: String? = ""
    var description: String? = ""
    var flashingAdvertiseDesc: String? = ""
    var emailPromotionalDesc: String? = ""
    var spinnerTemplateCode: String? = ""
    var advertisePageLocationResponseItem: AdvertisePageLocationResponseItem? = null
    var openPreview = false
    var openRichTextEditor = false
    var isImageOnly = false
    var isTextOnly = false
    var isBoth = false
    var chooseTemplateSelected = false

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("result")
                if (data?.isNotEmpty() == true) {
                    binding?.advertiseDescEt?.fromHtml(data.trim())
                    //binding?.descLength?.text = data.toString().trim().length.toString()
                    description = data.trim()
                } else {
                    binding?.advertiseDescEt?.text = ""
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentPostAdvertisementbasicDetailsBinding.inflate(inflater, container, false)

        binding?.apply {
            advertiseDescEt.movementMethod = ScrollingMovementMethod()
            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_BASIC_DETAILS)

            description?.let {
                if (it.isNotEmpty()) {
                    advertiseDescEt.fromHtml(it)
                }
            }

            chooseTemplatell.setOnClickListener {

                if (advertisePageLocationResponseItem?.type != "TXTONLY") {
                    ImagePicker.with(requireActivity())
                        .compress(1024)
                        .cropSquare()
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                            progressBarBasicDetails.visibility = View.VISIBLE
                        }
                    /*if (advertiseImage?.isEmpty() == true || postAdvertiseViewModel.isUpdateAdvertise) {
                        ImagePicker.with(requireActivity())
                            .compress(1024)
                            .cropSquare()
                            .maxResultSize(1080, 1080)
                            .createIntent { intent ->
                                startForProfileImageResult.launch(intent)
                                progressBarBasicDetails.visibility = View.VISIBLE
                            }
                    }*/
                }
            }

            advertiseDetailsNextBtn.setOnClickListener {

                if (titleAdvertisedEt.text.toString().length >= 3) {

                    if (!chooseTemplateSelected) {
                        if (isBoth) {
                            if (advertiseImage?.isNotEmpty() == true) {
                                if (advertiseDescEt.text.trim().toString().length >= 3) {
                                    spinnerTemplateCode?.let { it1 ->
                                        saveDataToViewModel(
                                            titleAdvertisedEt.text.toString(),
                                            selectedPage.text.toString(),
                                            selectAdvertiseDaysSpinner.toString(),
                                            planChargeEt.text.toString(),
                                            costOfvalueEt.text.toString(),
                                            emailPromotionalCheckbox.isChecked,
                                            flashingAdvertiseCheckbox.isChecked,
                                            it1,
                                        )
                                    }
                                    findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)
                                } else {
                                    showAlert("Please enter valid description")
                                }
                            } else {
                                showAlert("Please upload ad photo")
                            }
                        } else if (isTextOnly) {
                            if (advertiseDescEt.text.trim().toString().length >= 3) {
                                spinnerTemplateCode?.let { it1 ->
                                    saveDataToViewModel(
                                        titleAdvertisedEt.text.toString(),
                                        selectedPage.text.toString(),
                                        selectAdvertiseDaysSpinner.toString(),
                                        planChargeEt.text.toString(),
                                        costOfvalueEt.text.toString(),
                                        emailPromotionalCheckbox.isChecked,
                                        flashingAdvertiseCheckbox.isChecked,
                                        it1,
                                    )
                                }
                                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)
                            } else {
                                showAlert("Please enter valid description")
                            }
                        } else if (isImageOnly) {
                            if (advertiseImage?.isNotEmpty() == true) {
                                spinnerTemplateCode?.let { it1 ->
                                    saveDataToViewModel(
                                        titleAdvertisedEt.text.toString(),
                                        selectedPage.text.toString(),
                                        selectAdvertiseDaysSpinner.toString(),
                                        planChargeEt.text.toString(),
                                        costOfvalueEt.text.toString(),
                                        emailPromotionalCheckbox.isChecked,
                                        flashingAdvertiseCheckbox.isChecked,
                                        it1,
                                    )
                                }
                                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)
                            } else {
                                showAlert("Please upload ad photo")
                            }
                        }
                    } else {
                        showAlert("Please choose template")
                    }
                } else {
                    showAlert("Please enter valid title")
                }
            }

            previewAdvertiseBtn.setOnClickListener {
                if (openPreview) {
                    spinnerTemplateCode?.let { it1 ->
                        saveDataToViewModel(
                            titleAdvertisedEt.text.toString(),
                            selectedPage.text.toString(),
                            selectAdvertiseDaysSpinner.toString(),
                            planChargeEt.text.toString(),
                            costOfvalueEt.text.toString(),
                            emailPromotionalCheckbox.isChecked,
                            flashingAdvertiseCheckbox.isChecked,
                            it1,
                        )
                    }
                    findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_reviewAdvertiseFragment)
                }
            }

            advertiseDescEt.setOnClickListener {
                if (openRichTextEditor) {
                    postAdvertiseViewModel.setIsAdvertise(true)
                    val intent = Intent(context, RichTextEditorActivity::class.java)
                    intent.putExtra("isFromAdvertiseBasicDetails", true)
                    intent.putExtra("data", description)
                    intent.putExtra("placeholder", "Ad description (Max 30 characters)")
                    resultLauncher.launch(intent)
                }
            }

            flashingAdvertiseTv.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Flashing Advertisement")
                builder.setMessage(flashingAdvertiseDesc)
                builder.setPositiveButton("OK") { dialog, which ->

                }
                builder.show()
            }

            emailPromotionalTv.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Email Promotional Ads")
                builder.setMessage(emailPromotionalDesc)
                builder.setPositiveButton("OK") { dialog, which ->

                }
                builder.show()
            }

        }

        binding?.selectAdvertiseTemplateSpinner?.customSetOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setPersistedItem(position)
                when (binding?.selectAdvertiseTemplateSpinner?.selectedItem.toString()) {
                    "Choose Template" -> {
                        if (!postAdvertiseViewModel.isUpdateAdvertise) {
                            binding?.advertiseDescEt?.setText("")
                            description = ""
                            spinnerTemplateCode = ""
                            chooseTemplateSelected = true
                            openRichTextEditor = false
                            isImageOnly = false
                            isBoth = false
                            isTextOnly = false
                        }
                    }
                    "Image Only" -> {
                        binding?.advertiseDescEt?.setText("")
                        description = ""
                        chooseTemplateSelected = false
                        spinnerTemplateCode = "IMON"
                        openRichTextEditor = false
                        isImageOnly = true
                        isBoth = false
                        isTextOnly = false
                    }
                    "Image with text on bottom" -> {
                        spinnerTemplateCode = "IMTB"
                        chooseTemplateSelected = false
                        openRichTextEditor = true
                        isBoth = true
                        isImageOnly = false
                        isTextOnly = false
                    }
                    "Image with text on left side" -> {
                        spinnerTemplateCode = "IMTL"
                        openRichTextEditor = true
                        chooseTemplateSelected = false
                        isBoth = true
                        isImageOnly = false
                        isTextOnly = false
                    }
                    "Text Only" -> {
                        binding?.advertiseDescEt?.setText("")
                        description = ""
                        spinnerTemplateCode = "TXON"
                        chooseTemplateSelected = false
                        openRichTextEditor = true
                        isTextOnly = true
                        isImageOnly = false
                        isBoth = false
                    }
                }
                enableDisableBtn()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })

        binding?.titleAdvertisedEt?.addTextChangedListener { editable ->
            enableDisableBtn()
        }

        binding?.advertiseDescEt?.addTextChangedListener { editable ->
            enableDisableBtn()
        }

        postAdvertiseViewModel.advertiseActiveVasData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (!response.data.isNullOrEmpty()) {
                        response.data.forEach {
                            when (it.code) {
                                "EPAD" -> {
                                    // Email Promotional Ads
                                    binding?.emailPromotionalLl?.visibility = View.VISIBLE
                                    binding?.emailPromotionalTv?.text = it.name
                                    emailPromotionalDesc = it.description
                                }
                                "FLAD" -> {
                                    // Flashing Advertisement
                                    binding?.flashingAdvertiseLl?.visibility = View.VISIBLE
                                    binding?.flashingAdvertiseTv?.text = it.name
                                    flashingAdvertiseDesc = it.description
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {

                }
            }
        }

        postAdvertiseViewModel.selectedTemplateLocation.observe(viewLifecycleOwner) {
            advertisePageLocationResponseItem = it
            binding?.selectedPage?.text = it.title
        }

        postAdvertiseViewModel.activeTemplateDataForSpinner.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val templateName = mutableListOf<String>()

                    if (!templateName.contains("Choose Template")) {
                        templateName.add("Choose Template")
                    }

                    if (advertisePageLocationResponseItem?.type == "BOTH") {
                        binding?.advertiseDescEt?.isEnabled = true
                        response.data?.forEach {
                            if (!templateName.contains(it.name)) {
                                templateName.add(it.name)
                            }
                        }
                        templateName.remove("Text Only")
                        binding?.chooseTemplatell?.visibility = View.VISIBLE
                        binding?.selectAdvertiseTemplateSpinner?.isEnabled = true
                    } else if (advertisePageLocationResponseItem?.type == "TXTONLY") {
                        binding?.advertiseDescEt?.isEnabled = true
                        if (!templateName.contains("Text Only")) {
                            templateName.add("Text Only")
                        }
                        binding?.selectAdvertiseTemplateSpinner?.isEnabled = false
                        binding?.chooseTemplatell?.visibility = View.GONE
                    } else if (advertisePageLocationResponseItem?.type == "IMGONLY") {
                        binding?.advertiseDescEt?.isEnabled = false
                        if (!templateName.contains("Image Only")) {
                            templateName.add("Image Only")
                        }
                        binding?.selectAdvertiseTemplateSpinner?.isEnabled = false
                        binding?.chooseTemplatell?.visibility = View.VISIBLE
                    }

                    val arrayAdapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            templateName
                        )
                    }

                    arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding?.selectAdvertiseTemplateSpinner?.adapter = arrayAdapter
                    getPersistedItem()?.let {
                        binding?.selectAdvertiseTemplateSpinner?.setSelection(
                            it,
                            true
                        )
                    }


                    when(advertisePageLocationResponseItem?.type){
                        "TXTONLY" -> {
                            binding?.selectAdvertiseTemplateSpinner?.setSelection(
                                1,
                                true
                            )
                        }
                        "IMGONLY" -> {
                            binding?.selectAdvertiseTemplateSpinner?.setSelection(
                                1,
                                true
                            )
                        }
                    }

                }
                is Resource.Error -> {

                }
            }
        }

        postAdvertiseViewModel.advertiseImage.observe(viewLifecycleOwner) {
            advertiseImage = it
        }

        /** This method is for setting filled data again when user goes next screen and navigate back  **/
        setData()


        if (postAdvertiseViewModel.isUpdateAdvertise) {

            setDataForUpdating()

            binding?.templateSpinnerConstraintLayout?.visibility = View.GONE
            binding?.previewAdvertiseBtn?.visibility = View.GONE
            binding?.emailPromotionalLl?.visibility = View.VISIBLE
            binding?.flashingAdvertiseLl?.visibility = View.VISIBLE
            binding?.emailPromotionalCheckbox?.isChecked = true
            binding?.flashingAdvertiseCheckbox?.isChecked = true

            if (AdvertiseStaticData.getAddDetails()?.template?.code == "TXON") {
                binding?.chooseTemplatell?.visibility = View.GONE
                chooseTemplateSelected = false
                openPreview = false
                openRichTextEditor = false
                isImageOnly = false
                isTextOnly = true
                isBoth = false
            }

            if (AdvertiseStaticData.getAddDetails()?.template?.code == "IMON") {
                binding?.advertiseDescEtNestedScroll?.visibility = View.GONE
                openPreview = false
                chooseTemplateSelected = false
                openRichTextEditor = false
                isImageOnly = true
                isTextOnly = false
                isBoth = false
            }

            if (AdvertiseStaticData.getAddDetails()?.template?.code == "IMTB") {
                binding?.advertiseDescEtNestedScroll?.visibility = View.VISIBLE
                openPreview = true
                chooseTemplateSelected = false
                openRichTextEditor = true
                isImageOnly = false
                isTextOnly = false
                isBoth = true
            }

            if (AdvertiseStaticData.getAddDetails()?.template?.code == "IMTL") {
                binding?.advertiseDescEtNestedScroll?.visibility = View.VISIBLE
                openPreview = true
                chooseTemplateSelected = false
                openRichTextEditor = true
                isImageOnly = false
                isTextOnly = false
                isBoth = true
            }

        }


        return binding?.root
    }

    private fun enableDisableBtn() {

        if (isBoth) {
            binding?.advertiseDescEtNestedScroll?.visibility = View.VISIBLE
            /*binding?.advertiseDescEtNestedScroll?.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.white))*/
            if (binding?.titleAdvertisedEt?.text.toString().length >= 3 && binding?.advertiseDescEt?.text.toString().length >= 3) {
                if (advertiseImage?.isNotEmpty() == true) {
                    if (binding?.advertiseDescEt?.text.toString().length >= 3) {
                        binding?.advertiseDetailsNextBtn?.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.greenBtnColor))
                        binding?.previewAdvertiseBtn?.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.blueBtnColor))
                        openPreview = true
                        binding?.previewAdvertiseBtn?.isEnabled = true
                    } else {
                        openPreview = false
                        binding?.previewAdvertiseBtn?.isEnabled = false
                        binding?.advertiseDetailsNextBtn?.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.lightGreenBtnColor))
                        binding?.previewAdvertiseBtn?.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.lightBlueBtnColor))
                    }
                }
            } else {
                openPreview = false
                binding?.previewAdvertiseBtn?.isEnabled = false
                binding?.advertiseDetailsNextBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightGreenBtnColor))
                binding?.previewAdvertiseBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightBlueBtnColor))
            }
        } else if (isTextOnly) {
            binding?.advertiseDescEtNestedScroll?.visibility = View.VISIBLE
            /*binding?.advertiseDescEtNestedScroll?.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.white))*/

            if (binding?.titleAdvertisedEt?.text.toString().length >= 3 && binding?.advertiseDescEt?.text.toString().length >= 3) {
                if (binding?.advertiseDescEt?.text.toString().length >= 3) {
                    binding?.advertiseDetailsNextBtn?.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.greenBtnColor))
                } else {
                    binding?.previewAdvertiseBtn?.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.lightBlueBtnColor))
                    binding?.previewAdvertiseBtn?.isEnabled = false
                }
            } else {
                openPreview = false
                binding?.previewAdvertiseBtn?.isEnabled = false
                binding?.advertiseDetailsNextBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightGreenBtnColor))
                binding?.previewAdvertiseBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightBlueBtnColor))
            }
            openPreview = false
        } else if (isImageOnly) {
            binding?.advertiseDescEtNestedScroll?.visibility = View.GONE
            /* binding?.advertiseDescEtNestedScroll?.backgroundTintList =
                 ColorStateList.valueOf(resources.getColor(R.color.graycolor))*/
            if (binding?.titleAdvertisedEt?.text.toString().length >= 3) {
                if (advertiseImage?.isNotEmpty() == true) {
                    binding?.advertiseDetailsNextBtn?.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.greenBtnColor))
                } else {
                    binding?.previewAdvertiseBtn?.isEnabled = false
                    binding?.advertiseDetailsNextBtn?.backgroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.lightGreenBtnColor))
                }
            } else {
                openPreview = false
                binding?.previewAdvertiseBtn?.isEnabled = false
                binding?.advertiseDetailsNextBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightGreenBtnColor))
                binding?.previewAdvertiseBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightBlueBtnColor))
            }
            openPreview = false
        }
    }

    private fun setData() {
        setImage()
    }

    private fun setDataForUpdating() {
        val advertiseData = AdvertiseStaticData.getAddDetails()
        binding?.apply {
            titleAdvertisedEt.setText(advertiseData?.advertisementDetails?.adTitle)
            selectedPage.text = advertiseData?.advertisementPageLocation?.locationName
            /*when (advertiseData?.locationPlanRate?.days) {
                7 -> {
                    selectAdvertiseDaysSpinner.setSelection(0, true)
                }
                15 -> {
                    selectAdvertiseDaysSpinner.setSelection(1, true)
                }
                30 -> {
                    selectAdvertiseDaysSpinner.setSelection(2, true)
                }
            }*/
            when (advertiseData?.advertisementPageLocation?.type) {
                "IMGONLY" -> {
                    spinnerTemplateCode = "IMON"
                    openRichTextEditor = false
                    isImageOnly = true
                    isBoth = false
                    isTextOnly = false
                }
                "TXTONLY" -> {
                    spinnerTemplateCode = "TXON"
                    openRichTextEditor = true
                    isImageOnly = false
                    isBoth = false
                    isTextOnly = true
                }
                else -> {
                    openRichTextEditor = true
                    isImageOnly = false
                    isBoth = true
                    isTextOnly = false
                }
            }

            advertiseDescEt.setText(
                Html.fromHtml(if (advertiseData?.advertisementDetails?.adDescription != null) advertiseData.advertisementDetails.adDescription else "")
            )
            description = advertiseData?.advertisementDetails?.adDescription
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
        templateCode: String,
    ) {
        postAdvertiseViewModel.addCompanyBasicDetailsMap(
            addTitle = addTitle,
            templateName = templateName,
            advertiseValidity = advertiseValidity,
            planCharges = planCharges,
            costOfValue = costOfValue,
            isEmailPromotional = isEmailPromotional,
            isFlashingAdvertisement = isFlashingAdvertisement,
            templateCode = templateCode,
            advertiseImageUri = if (advertiseImage?.isNotEmpty() == true) advertiseImage!! else "",
            description = if (description?.isNotEmpty() == true) description?.trim()!! else binding?.advertiseDescEt?.text.toString()
                .trim(),
        )
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                val fileUri = data?.data!!

                advertiseImage = fileUri.toString()
                binding?.progressBarBasicDetails?.visibility = View.INVISIBLE
                setImage()

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                binding?.progressBarBasicDetails?.visibility = View.INVISIBLE
            }
        }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        Toast.makeText(context, p0?.getItemAtPosition(p2).toString(), Toast.LENGTH_SHORT).show()
    }

    private fun setImage() {

        postAdvertiseViewModel.setAdvertiseImage(if (advertiseImage?.isNotEmpty() == true) advertiseImage!! else "")

        if (advertiseImage?.isNotEmpty() == true) {
            binding?.advertiseIv?.visibility = View.VISIBLE
            binding?.uploadImageTv?.visibility = View.GONE
            binding?.sizeLimitTv?.visibility = View.GONE
            binding?.advertiseIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(advertiseImage)
                        .error(R.drawable.small_image_placeholder)
                        .into(it)

                }
            }
        } else {
            binding?.advertiseIv?.visibility = View.GONE
            binding?.uploadImageTv?.visibility = View.VISIBLE
            binding?.sizeLimitTv?.visibility = View.VISIBLE
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

    fun Spinner.customSetOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener) {
        onItemSelectedListener = listener
    }

    private fun getPersistedItem(): Int? {
        return context?.let { PreferenceManager<Int>(it)["selectedTemplateSpinnerItem", 0] }
    }

    protected fun setPersistedItem(position: Int) {
        context?.let { it1 -> PreferenceManager<Int>(it1) }
            ?.set("selectedTemplateSpinnerItem", position)
    }

    override fun onResume() {
        super.onResume()
        enableDisableBtn()
        setImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}