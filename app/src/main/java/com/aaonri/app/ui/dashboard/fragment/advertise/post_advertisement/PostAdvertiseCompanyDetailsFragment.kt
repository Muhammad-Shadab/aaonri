package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertiseCompanyDetailsFrgamentBinding
import com.aaonri.app.ui.dashboard.fragment.classified.RichTextEditor
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAdvertiseCompanyDetailsFragment : Fragment() {
    var detailsBinding: FragmentPostAdvertiseCompanyDetailsFrgamentBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()

    var description: String? = ""
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("result")
                if (data?.isNotEmpty() == true) {
                    detailsBinding?.advertiseDescEt?.fromHtml(data.trim())
                    description = data.trim()
                } else {
                    detailsBinding?.advertiseDescEt?.text = ""
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsBinding =
            FragmentPostAdvertiseCompanyDetailsFrgamentBinding.inflate(inflater, container, false)

        val email =
            context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }

        val phone =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PHONE_NUMBER, ""] }

        detailsBinding?.apply {
            advertiseDescEt.movementMethod = ScrollingMovementMethod()
            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_COMPANY_DETAILS)

            companyEmailEt.setText(email)
            companyMobileEt.setText(phone)

            advertiseDescEt.setOnClickListener {
                val intent = Intent(context, RichTextEditor::class.java)
                intent.putExtra("data", description)
                intent.putExtra("placeholder", "Please describe product/service")
                resultLauncher.launch(intent)
            }

            advertiseDetailsNextBtn.setOnClickListener {

                val phoneNumber = companyMobileEt.text.toString().replace("-", "")

                if (companyProfessionEt.text.toString()
                        .isNotEmpty() && companyProfessionEt.text.toString().length < 3
                ) {
                    showAlert("Please enter valid Product / Services")
                } else if (companyLinkEt.text.toString().isNotEmpty() && !Validator.urlValidation(
                        companyLinkEt.text.toString()
                    )
                ) {
                    showAlert("Please enter valid Link")
                } else {
                    if (companyNameEt.text.toString().length >= 3) {
                        if (companyAddress.text.toString().length >= 3) {
                            if (phoneNumber.length == 10) {
                                if (Validator.emailValidation(companyEmailEt.text.toString())) {
                                    if (advertiseDescEt.text.toString().length >= 3) {
                                        postAdvertiseViewModel.addCompanyContactDetails(
                                            companyName = companyNameEt.text.toString(),
                                            location = companyAddress.text.toString(),
                                            phoneNumber = phoneNumber,
                                            email = companyEmailEt.text.toString(),
                                            services = companyProfessionEt.text.toString(),
                                            link = companyLinkEt.text.toString(),
                                            description = if (description?.isNotEmpty() == true) description!! else advertiseDescEt.text.toString()
                                        )

                                        if (postAdvertiseViewModel.isUpdateAdvertise) {
                                            val action =
                                                PostAdvertiseCompanyDetailsFragmentDirections.actionPostAdvertiseCompanyDetailsFrgamentToPostAdvertisementbasicDetailsFragment()
                                            findNavController().navigate(action)
                                        } else {
                                            val action =
                                                PostAdvertiseCompanyDetailsFragmentDirections.actionPostAdvertiseCompanyDetailsFrgamentToSelectAdvertiseTemplate()
                                            findNavController().navigate(action)
                                        }

                                    } else {
                                        showAlert("Please enter valid Advertise Description")
                                    }
                                } else {
                                    showAlert("Please enter valid Email")
                                }
                            } else {
                                showAlert("Please enter valid Mobile Number")
                            }
                        } else {
                            showAlert("Please enter valid Address")
                        }
                    } else {
                        showAlert("Please enter valid Company Name")
                    }
                }
            }
        }

        detailsBinding?.companyMobileEt?.addTextChangedListener(object :
            TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                length_before = s.length
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {

                if (length_before < s.length) {
                    if (s.length == 3 || s.length == 7) s.append("-")
                    if (s.length > 3) {
                        if (Character.isDigit(s[3])) s.insert(3, "-")
                    }
                    if (s.length > 7) {
                        if (Character.isDigit(s[7])) s.insert(7, "-")
                    }
                }
            }
        })

        setData()

        if (postAdvertiseViewModel.isUpdateAdvertise) {
            setDataForUpdating()
        }



        return detailsBinding?.root
    }

    private fun setDataForUpdating() {
        val advertiseData = AdvertiseStaticData.getAddDetails()

        detailsBinding?.apply {
            companyNameEt.setText(advertiseData?.advertisementDetails?.companyName)
            companyAddress.setText(advertiseData?.advertisementDetails?.location)
            companyMobileEt.setText(advertiseData?.advertisementDetails?.contactNo)
            companyEmailEt.setText(advertiseData?.advertisementDetails?.emailId)
            companyProfessionEt.setText(advertiseData?.advertisementDetails?.productServices)
            companyLinkEt.setText(advertiseData?.advertisementDetails?.url)
            advertiseDescEt.fromHtml(advertiseData?.advertisementDetails?.companyDescription)
            description = advertiseData?.advertisementDetails?.companyDescription
        }
    }

    private fun setData() {
        detailsBinding?.advertiseDescEt?.fromHtml(if (postAdvertiseViewModel.companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_DESCRIPTION]?.isNotEmpty() == true) postAdvertiseViewModel.companyContactDetailsMap[AdvertiseConstant.ADVERTISE_COMPANY_DESCRIPTION] else "")

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