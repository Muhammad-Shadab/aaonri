package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
    var cityName: String = ""
    var stateName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addressDetailsBinding = FragmentAddressDetailsBinding.inflate(inflater, container, false)
        getCountries()

        var job: Job? = null

        addressDetailsBinding?.apply {

            authCommonViewModel.addNavigationForStepper(AuthConstant.ADDRESS_DETAILS_SCREEN)

            if (authCommonViewModel.locationDetails["zipCode"]?.isNotEmpty() == true) {
                addressDetailsBinding?.zipCodeAddressDetails?.setText(authCommonViewModel.locationDetails["zipCode"].toString())
            }


            if (!authCommonViewModel.isCountrySelected) {
                authCommonViewModel.setSelectedCountryAddressScreen(
                    countryName = "USA",
                    countryFlag = "https://disease.sh/assets/img/flags/us.png",
                    countryCode = "US"
                )
            }

            authCommonViewModel.selectedCountryAddressScreen?.observe(viewLifecycleOwner) { triple ->

                authCommonViewModel.setIsCountrySelected(true)

                if (triple.first.isNotEmpty() || triple.third.isNotEmpty()) {
                    zipCodeAddressDetails.addTextChangedListener { editable ->
                        job?.cancel()
                        job = MainScope().launch {
                            delay(300L)
                            editable?.let {
                                if (editable.toString()
                                        .isNotEmpty() && editable.toString().length >= 5
                                ) {
                                    authCommonViewModel.getLocationByZipCode(
                                        editable.toString(),
                                        triple.third
                                    )
                                } else {
                                    invalidZipCodeTv.visibility = View.GONE
                                }
                            }
                        }
                    }
                    selectCountryOriginAddress.text = triple.first
                    countryFlagIconAddress.load(triple.second)
                    countryFlagIconAddress.visibility = View.VISIBLE
                } else {
                    countryFlagIconAddress.visibility = View.GONE
                }
            }

            selectCountryOriginAddress.setOnClickListener {
                val action =
                    AddressDetailsFragmentDirections.actionAddressDetailsFragmentToSelectCountryBottomFragment(
                        true
                    )
                findNavController().navigate(action)
            }

            addressDetailsNextBtn.setOnClickListener {

                val address1 = address1.text
                val address2 = address2.text
                val phoneNumber = phoneNumberAddressDetails.text.toString().replace("-", "")
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
                    if (phoneNumber.isNotEmpty()) {
                        if (phoneNumber.length == 10) {
                            findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
                        } else {
                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    "Please complete phone number", Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
                    }
                    authCommonViewModel.addAddressDetails(
                        address1.toString(),
                        address2.toString(),
                        phoneNumber
                    )

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

        authCommonViewModel.countryClicked.observe(viewLifecycleOwner) {
            if (it) {
                addressDetailsBinding?.stateNameAddressDetails?.text = ""
                stateName = ""
                cityName = ""
                addressDetailsBinding?.cityNameAddressDetails?.setText("")
                authCommonViewModel.addLocationDetails(
                    zipCode = "",
                    state = "",
                    city = ""
                )
                addressDetailsBinding?.zipCodeAddressDetails?.setText("")
                authCommonViewModel.addCountryClicked(false)
            } else {
                if (authCommonViewModel.locationDetails["zipCode"]?.isNotEmpty() == true) {
                    addressDetailsBinding?.zipCodeAddressDetails?.setText(authCommonViewModel.locationDetails["zipCode"].toString())
                }
            }
        }

        addressDetailsBinding?.phoneNumberAddressDetails?.addTextChangedListener(object :
            TextWatcher {
            var length_before = 0
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
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

        authCommonViewModel.zipCodeData.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (response.data?.result?.isNotEmpty() == true) {
                        cityName = response.data.result.getOrNull(0)?.province.toString()
                        stateName = response.data.result.getOrNull(0)?.state.toString()

                        authCommonViewModel.addLocationDetails(
                            addressDetailsBinding?.zipCodeAddressDetails?.text.toString(),
                            stateName,
                            cityName
                        )

                        addressDetailsBinding?.cityNameAddressDetails?.setText(if (authCommonViewModel.locationDetails["city"]?.isNotEmpty() == true) authCommonViewModel.locationDetails["city"].toString() else cityName)
                        addressDetailsBinding?.stateNameAddressDetails?.text = if (authCommonViewModel.locationDetails["state"]?.isNotEmpty() == true) authCommonViewModel.locationDetails["state"].toString() else stateName

                        addressDetailsBinding?.invalidZipCodeTv?.visibility = View.GONE
                    } else {
                        addressDetailsBinding?.cityNameAddressDetails?.setText("")
                        addressDetailsBinding?.invalidZipCodeTv?.visibility = View.VISIBLE
                        addressDetailsBinding?.stateNameAddressDetails?.text = ""
                        cityName = ""
                    }

                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    authCommonViewModel.setIsCountrySelected(false)
                    authCommonViewModel.addLocationDetails("", "", "")
                    authCommonViewModel.zipCodeData.value = null
                    findNavController().navigateUp()
                }
            })


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
                is Resource.Empty -> TODO()
            }
        }
    }
}