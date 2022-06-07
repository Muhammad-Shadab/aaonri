package com.aaonri.app.ui.authentication.register

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.databinding.FragmentAddressDetailsBinding
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressDetailsFragment : Fragment() {
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    var addressDetailsBinding: FragmentAddressDetailsBinding? = null
    var isCountrySelected = false
    var cityName: String = ""
    var stateName: String = ""
    var zipCode: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addressDetailsBinding = FragmentAddressDetailsBinding.inflate(inflater, container, false)
        getCountries()

        var job: Job? = null

        addressDetailsBinding?.apply {
            val zipCode = zipCodeAddressDetails.text

            authCommonViewModel.addNavigationForStepper(AuthConstant.ADDRESS_DETAILS_SCREEN)

            if (!isCountrySelected)
                authCommonViewModel.addSelectedCountry(
                    countryName = "USA",
                    countryFlag = "https://disease.sh/assets/img/flags/us.png",
                    countryCode = "US"
                )

            authCommonViewModel.selectedCountry?.observe(viewLifecycleOwner) { triple ->
                isCountrySelected = true
                if (triple.first.isNotEmpty() || triple.third.isNotEmpty()) {
                    zipCodeAddressDetails.addTextChangedListener { editable ->
                        job?.cancel()
                        job = MainScope().launch {
                            delay(500L)
                            editable?.let {
                                if (editable.toString()
                                        .isNotEmpty() && editable.toString().length >= 5
                                ) {
                                    authCommonViewModel.getLocationByZipCode(
                                        zipCode.toString(),
                                        triple.third
                                    )
                                } else {
                                    invalidZipCodeTv.visibility = View.GONE
                                }
                            }
                        }
                    }
                    countryFlagIconAddress.load(triple.second)
                    selectCountryOriginAddress.text = triple.first
                    countryFlagIconAddress.visibility = View.VISIBLE
                } else {
                    countryFlagIconAddress.visibility = View.GONE
                }
            }

            selectCountryOriginAddress.setOnClickListener {
                findNavController().navigate(R.id.action_addressDetailsFragment_to_selectCountryBottomFragment)
            }

            addressDetailsNextBtn.setOnClickListener {

                val address1 = address1.text
                val address2 = address2.text
                val phoneNumber = phoneNumberAddressDetails.text
                val userEnteredCity = cityNameAddressDetails.text
                val zipCode = zipCodeAddressDetails.text

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (stateName.isNotEmpty() && userEnteredCity.toString()
                        .isNotEmpty() && zipCode.toString()
                        .isNotEmpty() && zipCode.toString().length >= 4
                ) {

                    authCommonViewModel.addLocationDetails(
                        zipCode = zipCode.toString(),
                        state = stateName,
                        city = userEnteredCity.toString()
                    )
                    authCommonViewModel.addAddressDetails(
                        address1.toString(),
                        address2.toString(),
                        phoneNumber.toString()
                    )
                    findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please complete all details", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        authCommonViewModel.zipCodeData.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    try {
                        cityName = response.data?.result?.get(0)?.province.toString()
                        stateName = response.data?.result?.get(0)?.state.toString()
                        zipCode = addressDetailsBinding?.zipCodeAddressDetails?.text.toString()
                        addressDetailsBinding?.stateNameAddressDetails?.text = stateName
                        addressDetailsBinding?.cityNameAddressDetails?.setText(cityName)
                        if (stateName.isNotEmpty()) {
                            addressDetailsBinding?.invalidZipCodeTv?.visibility = View.GONE
                        } else {
                            addressDetailsBinding?.invalidZipCodeTv?.visibility = View.VISIBLE
                        }

                    } catch (e: Exception) {
                        Log.i("location", "onCreateView: ${e.localizedMessage}")
                    }
                    if (stateName.isBlank()) {
                        addressDetailsBinding?.invalidZipCodeTv?.visibility = View.VISIBLE
                    } else {
                        addressDetailsBinding?.invalidZipCodeTv?.visibility = View.GONE
                    }

                   addressDetailsBinding?.cityNameAddressDetails?.setText(if (authCommonViewModel.locationDetails["city"]?.isNotEmpty() == true) authCommonViewModel.locationDetails["city"].toString() else cityName)
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }

        return addressDetailsBinding?.root
    }

    private fun getCountries() {
        authCommonViewModel.getCountries()
        authCommonViewModel.countriesData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    try {

                    } catch (e: Exception) {
                        Log.i("location", "onCreateView: ${e.localizedMessage}")
                    }

                }
            }
        }
    }

}