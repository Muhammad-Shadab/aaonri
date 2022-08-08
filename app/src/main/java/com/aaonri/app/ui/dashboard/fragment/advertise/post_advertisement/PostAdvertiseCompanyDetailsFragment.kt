package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertiseCompanyDetailsFrgamentBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.RichTextEditor
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.aaonri.app.utils.Validator

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

        val action =
            PostAdvertiseCompanyDetailsFragmentDirections.actionPostAdvertiseCompanyDetailsFrgamentToSelectAdvertiseTemplate()
        findNavController().navigate(action)

        detailsBinding?.apply {

            companyEmailEt.setText(email)
            companyMobileEt.setText(phone)

            advertiseDescEt.setOnClickListener {
                val intent = Intent(context, RichTextEditor::class.java)
                intent.putExtra("data", description)
                intent.putExtra("placeholder", "Please describe product/service")
                resultLauncher.launch(intent)
            }

            advertiseDetailsNextBtn.setOnClickListener {
                if (companyProfessionEt.text.toString().length < 3) {
                    showAlert("Please enter valid Product / Services")
                } else if (companyLinkEt.text.toString().length < 10) {
                    showAlert("Please enter valid Link")
                } else {
                    if (companyNameEt.text.toString().length >= 3) {
                        if (companyAddress.text.toString().length >= 3) {
                            if (companyMobileEt.text.toString().length == 10) {
                                if (Validator.emailValidation(companyEmailEt.text.toString())) {
                                    if (advertiseDescEt.text.toString().length >= 3) {
                                        description?.let { it1 ->
                                            postAdvertiseViewModel.advertiseBasicDetails(
                                                companyName = companyNameEt.text.toString(),
                                                location = companyAddress.text.toString(),
                                                phoneNumber = companyMobileEt.text.toString(),
                                                email = companyEmailEt.text.toString(),
                                                services = companyProfessionEt.text.toString(),
                                                link = companyLinkEt.text.toString(),
                                                description = it1
                                            )
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

        setData()

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

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

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

        return detailsBinding?.root
    }

    private fun setData() {
        detailsBinding?.advertiseDescEt?.fromHtml(if (postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_DESCRIPTION]?.isNotEmpty() == true) postAdvertiseViewModel.companyBasicDetailsMap[AdvertiseConstant.ADVERTISE_DESCRIPTION] else "")

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