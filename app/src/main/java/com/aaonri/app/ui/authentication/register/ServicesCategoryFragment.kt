package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.data.authentication.register.model.add_user.Community
import com.aaonri.app.data.authentication.register.model.add_user.RegisterRequest
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.data.authentication.register.model.services.listOfService
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentServicesCategoryBinding
import com.example.newsapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ServicesCategoryFragment : Fragment() {
    var servicesGridItemBinding: FragmentServicesCategoryBinding? = null
    private var adapter: ServicesItemAdapter? = null
    val registrationViewModel: RegistrationViewModel by viewModels()
    val commonViewModel: CommonViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        servicesGridItemBinding =
            FragmentServicesCategoryBinding.inflate(inflater, container, false)
        getServicesInterestList()

        val companyEmail = servicesGridItemBinding?.companyEmailServices?.text
        val aliasName = servicesGridItemBinding?.aliasNameServices?.text

        adapter = ServicesItemAdapter { selectedCommunity ->
            commonViewModel.addServicesList(selectedCommunity as MutableList<ServicesResponseItem>)
        }

        commonViewModel.selectedServicesList.observe(viewLifecycleOwner) {
            if (it.size >= 3) {
                servicesGridItemBinding?.visibilityCardView?.visibility = View.VISIBLE
            } else {
                servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
            }
        }

        servicesGridItemBinding?.apply {
            serviceSubmitBtn.setOnClickListener {
                if (companyEmail.toString().isNotEmpty() && aliasName.toString().isNotEmpty()) {
                    commonViewModel.addCompanyEmailAliasName(
                        companyEmail.toString(),
                        aliasName.toString()
                    )
                    registrationViewModel.registerUser(
                        RegisterRequest(
                            activeUser = true,
                            address1 = commonViewModel.addressDetails["address1"]!!,
                            address2 = commonViewModel.addressDetails["address2"]!!,
                            aliasName = commonViewModel.companyEmailAliasName!!.value!!.first,
                            authorized = true,
                            city = "bulandshahr",
                            community = listOf(
                                Community(1, "Home Needs"),
                                Community(2, "Foundation & Donations")
                            ),
                            commonViewModel.companyEmailAliasName!!.value!!.second,
                            emailId = commonViewModel.basicDetailsMap["emailAddress"]!!,
                            firstName = commonViewModel.basicDetailsMap["firstName"]!!,
                            interests = "1,4,19",
                            isAdmin = 0,
                            isFullNameAsAliasName = true,
                            isJobRecruiter = false,
                            isPrimeUser = false,
                            isSurveyCompleted = false,
                            lastName = commonViewModel.basicDetailsMap["lastName"]!!,
                            newsletter = false,
                            originCity = "",
                            originCountry = "",
                            originState = "",
                            password = commonViewModel.basicDetailsMap["password"]!!,
                            phoneNo = commonViewModel.addressDetails["phoneNumber"]!!,
                            picture = "",
                            regdEmailSent = false,
                            registeredBy = "manual",
                            userName = "asjdas sdaksd",
                            zipcode = "203001"
                        )
                    )
                } else {
                    Toast.makeText(context, "All fields are mandatory", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "Successfully Registered", Toast.LENGTH_LONG)
                            .show()
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