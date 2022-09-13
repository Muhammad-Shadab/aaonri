package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.ui.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.data.authentication.register.model.add_user.Community
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentServicesCategoryBinding
import com.aaonri.app.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesCategoryFragment : Fragment() {
    var servicesGridItemBinding: FragmentServicesCategoryBinding? = null
    private var adapter: ServicesItemAdapter? = null
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    var isServicesSelected = false
    var isJobSelected = false
    var isCompanyEmailCheckboxSelected = false
    var selectedCommunity = mutableListOf<Community>()
    var selectedServicesInterest = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog = Dialog(requireContext())
        servicesGridItemBinding =
            FragmentServicesCategoryBinding.inflate(inflater, container, false)
        getServicesInterestList()

        adapter = ServicesItemAdapter({ selectedCommunity ->
            authCommonViewModel.addServicesList(selectedCommunity as MutableList<ServicesResponseItem>)
        }) {
            isJobSelected = it
        }

        servicesGridItemBinding?.apply {

            companyEmailServices.isEnabled = false
            authCommonViewModel.addNavigationForStepper(AuthConstant.SERVICE_DETAILS_SCREEN)

            isAliasNameCheckBox.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    aliasNameServices.isEnabled = false
                    aliasNameServices.setText(authCommonViewModel.basicDetailsMap["firstName"] + " " + authCommonViewModel.basicDetailsMap["lastName"])
                } else {
                    aliasNameServices.setText("")
                    aliasNameServices.isEnabled = true
                }
            }

            isRecruiterCheckBox.setOnCheckedChangeListener { p0, p1 ->
                isCompanyEmailCheckboxSelected = p1
                companyEmailServices.isEnabled = p1
            }


            companyEmailServices.addTextChangedListener { editable ->
                editable?.let {
                    if (editable.toString().isNotEmpty() && editable.toString().length > 8) {
                        if (Validator.emailValidation(editable.toString())) {
                            invalidEmailTv.visibility = View.GONE
                        } else {
                            invalidEmailTv.visibility = View.VISIBLE
                        }
                    } else {
                        invalidEmailTv.visibility = View.GONE
                    }

                }
            }

            serviceSubmitBtn.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (isServicesSelected) {
                    val companyEmail = companyEmailServices.text
                    val aliasName = aliasNameServices.text

                    if (isCompanyEmailCheckboxSelected) {
                        if (Validator.emailValidation(companyEmail.toString()) && companyEmail.toString().length >= 8) {
                            if (aliasName.toString().isNotEmpty()) {
                                selectedCommunity.forEach {
                                    Log.i("selectedCommunity", "${it.communityName}")
                                }
                                registerUser(
                                    companyEmail.toString(),
                                    aliasName.toString(),
                                    isRecruiterCheckBox = isRecruiterCheckBox.isChecked,
                                    isAliasNameCheckBox = isAliasNameCheckBox.isChecked,
                                    //belongToCricketCheckBox = belongToCricketCheckBox.isChecked
                                )
                            } else {
                                activity?.let { it1 ->
                                    Snackbar.make(
                                        it1.findViewById(android.R.id.content),
                                        "Alias name required", Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            if (companyEmail.isNotEmpty()) {
                                invalidEmailTv.visibility = View.VISIBLE
                            } else {
                                activity?.let { it1 ->
                                    Snackbar.make(
                                        it1.findViewById(android.R.id.content),
                                        "Company email required", Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else if (aliasName.toString().isNotEmpty()) {
                        registerUser(
                            companyEmail.toString(), aliasName.toString(),
                            isRecruiterCheckBox = isRecruiterCheckBox.isChecked,
                            isAliasNameCheckBox = isAliasNameCheckBox.isChecked,
                            //belongToCricketCheckBox = belongToCricketCheckBox.isChecked
                        )
                    } else {
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "Alias name required", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please choose at least three services", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
            servicesGridRecyclerView.adapter = adapter
            servicesGridRecyclerView.layoutManager = GridLayoutManager(context, 3)
        }

        authCommonViewModel.selectedServicesList.observe(viewLifecycleOwner) { serviceResponseItem ->
            adapter?.savedCategoriesList = serviceResponseItem
            selectedServicesInterest = ""
            var i = 0
            var len: Int = serviceResponseItem.size
            serviceResponseItem.forEach {
                i++
                isJobSelected = it.interestDesc == "Jobs"
                selectedServicesInterest += "${it.id}${if (i != len) "," else ""}"
            }
            if (serviceResponseItem.size >= 3 && isJobSelected) {
                authCommonViewModel.addStepViewLastTick(true)
                isServicesSelected = true
                servicesGridItemBinding?.servicesGridRecyclerView?.margin(bottom = 0f)
                servicesGridItemBinding?.visibilityCardView?.visibility = View.VISIBLE
                servicesGridItemBinding?.aliasNameCardView?.visibility = View.VISIBLE
                servicesGridItemBinding?.serviceSubmitBtn?.setBackgroundResource(R.drawable.green_btn_shape)
//                servicesGridItemBinding?.scrollView?.fullScroll(NestedScrollView.FOCUS_DOWN)
            } else if (serviceResponseItem.size >= 3) {
                isServicesSelected = true
                authCommonViewModel.addStepViewLastTick(true)
                servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
                servicesGridItemBinding?.aliasNameCardView?.visibility = View.VISIBLE
                servicesGridItemBinding?.servicesGridRecyclerView?.margin(bottom = 0f)
                servicesGridItemBinding?.serviceSubmitBtn?.setBackgroundResource(R.drawable.green_btn_shape)
//                servicesGridItemBinding?.scrollView?.fullScroll(NestedScrollView.FOCUS_DOWN)
            } else {
                authCommonViewModel.addStepViewLastTick(false)
                isServicesSelected = false
                servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
                servicesGridItemBinding?.aliasNameCardView?.visibility = View.GONE
                servicesGridItemBinding?.servicesGridRecyclerView?.margin(bottom = 60f)
                servicesGridItemBinding?.serviceSubmitBtn?.setBackgroundResource(R.drawable.light_green_btn_shape)
            }
        }

        registrationViewModel.registerData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    servicesGridItemBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    servicesGridItemBinding?.progressBar?.visibility = View.GONE
                    if (response.data?.status.equals("true")) {

                        dialog.setContentView(R.layout.success_register_dialog)
                        dialog.window?.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.dialog_shape
                            )
                        )
                        dialog.setCancelable(false)
                        dialog.show()
                        val continueBtn = dialog.findViewById<TextView>(R.id.continueRegisterBtn)
                        continueBtn.setOnClickListener {
                            dialog.dismiss()
                            activity?.finish()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            response.data?.errorDetails.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Error -> {
                    servicesGridItemBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                }
            }
        }

        authCommonViewModel.selectedCommunityList.observe(viewLifecycleOwner) { communityList ->
            communityList.forEach { community ->
                selectedCommunity.add((Community(community.communityId, community.communityName)))
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    findNavController().navigateUp()
                }
            })
        return servicesGridItemBinding?.root
    }

    private fun registerUser(
        companyEmail: String,
        aliasName: String,
        isRecruiterCheckBox: Boolean,
        isAliasNameCheckBox: Boolean,
        //belongToCricketCheckBox: Boolean
    ) {

        authCommonViewModel.addCompanyEmailAliasName(
            companyEmail,
            aliasName
        )
        authCommonViewModel.addCompanyEmailAliasCheckBoxValue(
            isRecruiterCheckBox,
            isAliasNameCheckBox,
            //belongToCricketCheckBox
        )
        registrationViewModel.registerUser(
            RegisterRequest(
                activeUser = true,
                address1 = authCommonViewModel.addressDetails["address1"]!!,
                address2 = authCommonViewModel.addressDetails["address2"]!!,
                aliasName = if (authCommonViewModel.companyEmailAliasName?.value?.second?.isNotEmpty() == true) authCommonViewModel.companyEmailAliasName!!.value!!.second else "",
                authorized = true,
                city = authCommonViewModel.locationDetails["city"]!!,
                community = selectedCommunity,
                companyEmail = if (authCommonViewModel.companyEmailAliasName?.value?.first?.isNotEmpty() == true) authCommonViewModel.companyEmailAliasName!!.value!!.first else "",
                emailId = authCommonViewModel.basicDetailsMap["emailAddress"]!!,
                firstName = authCommonViewModel.basicDetailsMap["firstName"]!!,
                interests = selectedServicesInterest,
                isAdmin = 0,
                isFullNameAsAliasName = authCommonViewModel.companyEmailAliasCheckBoxValue["isAliasNameCheckBox"]!!,
                isJobRecruiter = authCommonViewModel.companyEmailAliasCheckBoxValue["isRecruiterCheckBox"]!!,
                isPrimeUser = false,
                isSurveyCompleted = false,
                lastName = authCommonViewModel.basicDetailsMap["lastName"]!!,
                newsletter = false,
                originCity = if(authCommonViewModel.originLocationDetails["originCity"]?.isNotEmpty() == true)authCommonViewModel.originLocationDetails["originCity"]!!else "",
                originCountry = authCommonViewModel.selectedCountryAddressScreen!!.value!!.first,
                originState =  if(authCommonViewModel.originLocationDetails["originState"]?.isNotEmpty() == true)authCommonViewModel.originLocationDetails["originState"]!!else "",
                password = authCommonViewModel.basicDetailsMap["password"]!!,
                phoneNo = authCommonViewModel.addressDetails["phoneNumber"]!!,
                picture = "",
                regdEmailSent = false,
                registeredBy = "manual",
                userName = aliasName,
                zipcode = authCommonViewModel.locationDetails["zipCode"]!!
            )
        )
    }

    private fun getServicesInterestList() {
        registrationViewModel.getServices()
        lifecycleScope.launchWhenCreated {
            registrationViewModel.service.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {
                        servicesGridItemBinding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        servicesGridItemBinding?.progressBar?.visibility = View.GONE
                        response.data?.let { servicesResponse ->
                            servicesResponse.removeAt(7)
                            servicesResponse.removeAt(0)
                            adapter?.setData(servicesResponse)
                        }
                    }
                    is Resource.Error -> {
                        servicesGridItemBinding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }


    fun View.margin(
        left: Float? = null,
        top: Float? = null,
        right: Float? = null,
        bottom: Float? = null
    ) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}