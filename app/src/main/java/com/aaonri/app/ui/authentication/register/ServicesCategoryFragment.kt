package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.SelectedCommunityAdapter
import com.aaonri.app.data.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.data.authentication.register.model.add_user.Community
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentServicesCategoryBinding
import com.aaonri.app.util.Constant
import com.aaonri.app.utils.Validator
import com.example.newsapp.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesCategoryFragment : Fragment() {
    var servicesGridItemBinding: FragmentServicesCategoryBinding? = null
    private var adapter: ServicesItemAdapter? = null
    val registrationViewModel: RegistrationViewModel by viewModels()
    val commonViewModel: CommonViewModel by activityViewModels()
    var isServicesSelected = false
    var isJobSelected = false
    var isCompanyEmailCheckboxSelected = false

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        servicesGridItemBinding =
            FragmentServicesCategoryBinding.inflate(inflater, container, false)
        getServicesInterestList()

        adapter = ServicesItemAdapter({ selectedCommunity ->
            commonViewModel.addServicesList(selectedCommunity as MutableList<ServicesResponseItem>)
        }) {
            isJobSelected = it
        }

        commonViewModel.selectedServicesList.observe(viewLifecycleOwner) { serviceResponseItem ->
            adapter?.savedCategoriesList = serviceResponseItem
            if (serviceResponseItem.size >= 3 && isJobSelected) {
                isServicesSelected = true
                servicesGridItemBinding?.visibilityCardView?.visibility = View.VISIBLE
            } else if (serviceResponseItem.size >= 3) {
                isServicesSelected = true
                servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
            } else {
                isServicesSelected = false
                servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
            }
        }

        val dialog = Dialog(requireContext())


        servicesGridItemBinding?.apply {

            commonViewModel.addNavigationForStepper(Constant.SERVICE_DETAILS_SCREEN)

            isAliasNameCheckBox.setOnCheckedChangeListener { p0, p1 ->


                if (p1) {
                    aliasNameServices.isEnabled = false
                    aliasNameServices.setText(commonViewModel.basicDetailsMap["firstName"] + " " + commonViewModel.basicDetailsMap["lastName"])
                } else {
                    aliasNameServices.setText("")
                    aliasNameServices.isEnabled = true
                }
            }

            isRecruiterCheckBox.setOnCheckedChangeListener { p0, p1 ->
                isCompanyEmailCheckboxSelected = p1
            }


            serviceSubmitBtn.setOnClickListener {
                if (isServicesSelected) {
                    val companyEmail = companyEmailServices.text
                    val aliasName = aliasNameServices.text

                    if (isCompanyEmailCheckboxSelected) {
                        if (Validator.emailValidation(companyEmail.toString())) {
                            invalidEmailTv.visibility = View.GONE
                            if (aliasName.toString().isNotEmpty()) {
                                registerUser(
                                    companyEmail.toString(),
                                    aliasName.toString(),
                                    isRecruiterCheckBox = isRecruiterCheckBox.isChecked,
                                    isAliasNameCheckBox = isAliasNameCheckBox.isChecked,
                                    belongToCricketCheckBox = belongToCricketCheckBox.isChecked
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
                            invalidEmailTv.visibility = View.VISIBLE
                        }
                    } else if (aliasName.toString().isNotEmpty()) {
                        registerUser(
                            companyEmail.toString(), aliasName.toString(),
                            isRecruiterCheckBox = isRecruiterCheckBox.isChecked,
                            isAliasNameCheckBox = isAliasNameCheckBox.isChecked,
                            belongToCricketCheckBox = belongToCricketCheckBox.isChecked
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
                            commonViewModel.addNavigateToLoginScreen(true)
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
        return servicesGridItemBinding?.root
    }

    private fun registerUser(
        companyEmail: String,
        aliasName: String,
        isRecruiterCheckBox: Boolean,
        isAliasNameCheckBox: Boolean,
        belongToCricketCheckBox: Boolean
    ) {
        commonViewModel.addCompanyEmailAliasName(
            companyEmail,
            aliasName
        )
        commonViewModel.addCompanyEmailAliasCheckBoxValue(
            isRecruiterCheckBox,
            isAliasNameCheckBox,
            belongToCricketCheckBox
        )
        registrationViewModel.registerUser(
            RegisterRequest(
                activeUser = true,
                address1 = commonViewModel.addressDetails["address1"]!!,
                address2 = commonViewModel.addressDetails["address2"]!!,
                aliasName = if (commonViewModel.companyEmailAliasName?.value?.second?.isNotEmpty() == true) commonViewModel.companyEmailAliasName!!.value!!.second else "",
                authorized = true,
                city = commonViewModel.locationDetails["city"]!!,
                community = listOf(
                    Community(1, "Home Needs"),
                    Community(2, "Foundation & Donations")
                ),
                companyEmail = if (commonViewModel.companyEmailAliasName?.value?.first?.isNotEmpty() == true) commonViewModel.companyEmailAliasName!!.value!!.first else "",
                emailId = commonViewModel.basicDetailsMap["emailAddress"]!!,
                firstName = commonViewModel.basicDetailsMap["firstName"]!!,
                interests = "1,4,19",
                isAdmin = 0,
                isFullNameAsAliasName = commonViewModel.companyEmailAliasCheckBoxValue["isAliasNameCheckBox"]!!,
                isJobRecruiter = commonViewModel.companyEmailAliasCheckBoxValue["isRecruiterCheckBox"]!!,
                isPrimeUser = false,
                isSurveyCompleted = false,
                lastName = commonViewModel.basicDetailsMap["lastName"]!!,
                newsletter = false,
                originCity = commonViewModel.locationDetails["city"]!!,
                originCountry = commonViewModel.selectedCountry!!.value!!.first,
                originState = commonViewModel.locationDetails["state"]!!,
                password = commonViewModel.basicDetailsMap["password"]!!,
                phoneNo = commonViewModel.addressDetails["phoneNumber"]!!,
                picture = "",
                regdEmailSent = false,
                registeredBy = "manual",
                userName = "asjdas sdaksd",
                zipcode = commonViewModel.locationDetails["zipCode"]!!
            )
        )
    }

    private fun getServicesInterestList() {
        registrationViewModel.getServices()
        lifecycleScope.launchWhenCreated {
            registrationViewModel.service.collect { response ->
                when (response) {
                    is Resource.Loading -> {
                        servicesGridItemBinding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        servicesGridItemBinding?.progressBar?.visibility = View.GONE
                        response.data?.let { servicesResponse ->
                            servicesResponse.removeAt(7)
                            servicesResponse.removeAt(0)
                            //val filterList = servicesResponse.remove(servicesResponse.)
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
}