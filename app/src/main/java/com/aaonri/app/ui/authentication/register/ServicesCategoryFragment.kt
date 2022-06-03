package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.data.authentication.register.model.add_user.Community
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentServicesCategoryBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        servicesGridItemBinding =
            FragmentServicesCategoryBinding.inflate(inflater, container, false)
        getServicesInterestList()

        adapter = ServicesItemAdapter { selectedCommunity ->
            commonViewModel.addServicesList(selectedCommunity as MutableList<ServicesResponseItem>)
        }

        commonViewModel.selectedServicesList.observe(viewLifecycleOwner) { serviceResponseItem ->
            adapter?.savedCategoriesList = serviceResponseItem
            if (serviceResponseItem.size >= 3) {
                isServicesSelected = true
                serviceResponseItem.forEach {
                    if (it.id == 3 || it.interestDesc == "Jobs") {
                        servicesGridItemBinding?.visibilityCardView?.visibility = View.VISIBLE
                    } else {
                        servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
                    }
                }
            } else {
                isServicesSelected = false
                servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
            }
        }

        servicesGridItemBinding?.apply {
                serviceSubmitBtn.setOnClickListener {
                    if (isServicesSelected) {
                    val companyEmail = companyEmailServices.text
                    val aliasName = aliasNameServices.text
                    if (aliasName.toString().isNotEmpty()) {
                        commonViewModel.addCompanyEmailAliasName(
                            companyEmail.toString(),
                            aliasName.toString()
                        )
                        commonViewModel.addCompanyEmailAliasCheckBoxValue(
                            isRecruiterCheckBox.isChecked,
                            isAliasNameCheckBox.isChecked,
                            belongToCricketCheckBox.isChecked
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
                    } else {
                        activity?.let { it1 ->
                            Snackbar.make(
                                it1.findViewById(android.R.id.content),
                                "Alias name required", Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                else {
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
                        context?.let { it1 ->
                            MaterialAlertDialogBuilder(it1).setTitle("Registration Success")
                                .setMessage("Check your email for verification")
                                .setBackground(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.dialog_shape
                                    )
                                )
                                .setPositiveButton(
                                    "OK"
                                ) { p0, p1 ->

                                }
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            response.data?.message.toString(),
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
                        response.data?.let { adapter?.setData(it) }
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