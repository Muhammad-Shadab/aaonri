package com.aaonri.app.ui.authentication.register

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.model.UpdateProfileRequest
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentAddressDetailsBinding
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.aaonri.app.utils.custom.UserProfileStaticData
import com.google.android.material.snackbar.Snackbar
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class AddressDetailsFragment : Fragment(), CountryCodePicker.OnCountryChangeListener {
    var binding: FragmentAddressDetailsBinding? = null
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    var cityName: String = ""
    var stateName: String = ""
    private var countryCode: String? = null
    private var countryName: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressDetailsBinding.inflate(inflater, container, false)
        getCountries()

        var job: Job? = null

        binding?.apply {

            if (authCommonViewModel.countryFlagBmp != null) {
                countryCodePicker.imageViewFlag.setImageBitmap(authCommonViewModel.countryFlagBmp)
            }
            countryCodePicker.setOnCountryChangeListener(this@AddressDetailsFragment)
            selectedCountryName.setOnClickListener {
                countryCodePicker.launchCountrySelectionDialog()
            }

            authCommonViewModel.addNavigationForStepper(AuthConstant.ADDRESS_DETAILS_SCREEN)

            if (authCommonViewModel.locationDetails["zipCode"]?.isNotEmpty() == true) {
                binding?.zipCodeAddressDetails?.setText(authCommonViewModel.locationDetails["zipCode"].toString())
                stateName = authCommonViewModel.locationDetails["state"].toString()
                cityName = authCommonViewModel.locationDetails["city"].toString()
                binding?.selectedCountryName?.text =
                    authCommonViewModel.selectedCountryAddressScreen?.value?.first
                countryCodePicker.imageViewFlag.setImageBitmap(authCommonViewModel.countryFlagBmp)
                if (authCommonViewModel.addressDetails["address1"]?.isNotEmpty() == true) {
                    binding?.address1?.setText(authCommonViewModel.addressDetails.get("address1"))
                }
                if (authCommonViewModel.addressDetails["address2"]?.isNotEmpty() == true) {
                    binding?.address2?.setText(authCommonViewModel.addressDetails.get("address2"))

                }
                if (authCommonViewModel.addressDetails["phoneNumber"]?.isNotEmpty() == true) {
                    binding?.phoneNumberAddressDetails?.setText(
                        authCommonViewModel.addressDetails?.get(
                            "phoneNumber"
                        )?.replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
                    )
                }
            }


            stateNameAddressDetails.text = stateName
            cityNameAddressDetails.setText(cityName)


            if (stateName.isEmpty()) {
                binding?.selectedCountryName?.text = "United States"
                authCommonViewModel.setSelectedCountryAddressScreen(
                    countryName = "USA",
                    countryFlag = "",
                    countryCode = "US"
                )
            }

            /*if (!authCommonViewModel.isCountrySelected) {
                authCommonViewModel.setSelectedCountryAddressScreen(
                    countryName = "USA",
                    countryFlag = "https://disease.sh/assets/img/flags/us.png",
                    countryCode = "US"
                )
            }*/

            authCommonViewModel.selectedCountryAddressScreen?.observe(viewLifecycleOwner) { triple ->

                authCommonViewModel.setIsCountrySelected(true)

                if (triple.first.isNotEmpty() || triple.third.isNotEmpty()) {
                    zipCodeAddressDetails.addTextChangedListener { editable ->
                        if (authCommonViewModel.isUpdateProfile) {

                        } else {
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
                    }
                    selectedCountryName.text = triple.first
                    /*
                    countryFlagIconAddress.load(triple.second)
                    countryFlagIconAddress.visibility = View.VISIBLE*/
                } else {
                    /*countryFlagIconAddress.visibility = View.GONE*/
                }
            }

            /*selectCountryOriginAddress.setOnClickListener {
                val action =
                    AddressDetailsFragmentDirections.actionAddressDetailsFragmentToSelectCountryBottomFragment(
                        true
                    )
                findNavController().navigate(action)
            }
*/
            addressDetailsNextBtn.setOnClickListener {

                val address1 = address1.text
                val address2 = address2.text
                val phoneNumber = phoneNumberAddressDetails.text.toString().replace("-", "")
                val userEnteredCity = cityNameAddressDetails.text
                val zipCode = zipCodeAddressDetails.text

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                countryCodePicker.imageViewFlag.invalidate()
                val drawable = countryCodePicker.imageViewFlag.drawable
                authCommonViewModel.countryFlagBmp(drawable.toBitmap())
                if (stateName.isNotEmpty() && userEnteredCity.toString().length >= 2 && zipCode.toString()
                        .isNotEmpty() && zipCode.toString().length >= 4
                ) {
                    if (cityName.isEmpty()) {
                        cityName = userEnteredCity.toString()
                    }
                    authCommonViewModel.addLocationDetails(
                        zipCode = zipCode.toString(),
                        state = stateName,
                        city = userEnteredCity.toString()
                    )
                    if (phoneNumber.isNotEmpty()) {
                        if (phoneNumber.length == 10) {
                            if (authCommonViewModel.isUpdateProfile) {
                                updateProfile()
                            } else {
                                findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
                            }
                        } else {
                            activity?.let { it1 ->
                                Snackbar.make(
                                    it1.findViewById(android.R.id.content),
                                    "Please complete phone number", Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        if (authCommonViewModel.isUpdateProfile) {
                            updateProfile()
                        } else {
                            findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
                        }
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
                binding?.stateNameAddressDetails?.text = ""
                stateName = ""
                cityName = ""
                binding?.cityNameAddressDetails?.setText("")
                authCommonViewModel.addLocationDetails(
                    zipCode = "",
                    state = "",
                    city = ""
                )
                binding?.zipCodeAddressDetails?.setText("")
                authCommonViewModel.addCountryClicked(false)
            } else {
                if (authCommonViewModel.locationDetails["zipCode"]?.isNotEmpty() == true) {
                    binding?.zipCodeAddressDetails?.setText(authCommonViewModel.locationDetails["zipCode"].toString())
                }
            }
        }

        binding?.phoneNumberAddressDetails?.addTextChangedListener(object :
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
                        var cityChangedName = cityName
                        cityName = response.data.result.getOrNull(0)?.district.toString()

                        stateName = response.data.result.getOrNull(0)?.state.toString()
                        if (cityName.isEmpty()) {
                            cityName = cityChangedName
                        }
                        authCommonViewModel.addLocationDetails(
                            binding?.zipCodeAddressDetails?.text.toString(),
                            stateName,
                            cityName
                        )

                        binding?.cityNameAddressDetails?.setText(if (authCommonViewModel.locationDetails["city"]?.isNotEmpty() == true) authCommonViewModel.locationDetails["city"].toString() else cityName)
                        binding?.stateNameAddressDetails?.text =
                            if (authCommonViewModel.locationDetails["state"]?.isNotEmpty() == true) authCommonViewModel.locationDetails["state"].toString() else stateName


                        binding?.invalidZipCodeTv?.visibility = View.GONE
                    } else {
                        binding?.cityNameAddressDetails?.setText("")
                        binding?.invalidZipCodeTv?.visibility = View.VISIBLE
                        binding?.stateNameAddressDetails?.text = ""
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

        if (authCommonViewModel.isUpdateProfile) {
            UserProfileStaticData.getUserProfileDataValue()?.let {
                cityName = it.city
                stateName = if (it.state != null) it.state.toString() else ""
                //binding?.countryCodePicker?.setCountryForNameCode(getCountryCode(it.originCountry))
                binding?.countryPickerLl?.visibility = View.GONE
                binding?.address1?.setText(it.address1)
                binding?.address2?.setText(it.address2)
                binding?.zipCodeAddressDetails?.setText(it.zipcode)
                binding?.zipCodeAddressDetails?.isEnabled = false
                binding?.zipCodeAddressDetails?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.cityNameAddressDetails?.setText(it.city)
                binding?.cityNameAddressDetails?.isEnabled = false
                binding?.cityNameAddressDetails?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.stateNameAddressDetails?.isEnabled = false
                binding?.stateNameAddressDetails?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.stateNameAddressDetails?.text = stateName
                //if (it.state != null) it.state.toString() else ""
                binding?.phoneNumberAddressDetails?.setText(it.phoneNo)
                binding?.addressDetailsNextBtn?.text = "UPDATE"
            }
        }


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                    /*  stateName = ""
                      cityName = ""
                      addressDetailsBinding?.cityNameAddressDetails?.setText("")
                      authCommonViewModel.addLocationDetails(
                          zipCode = "",
                          state = "",
                          city = ""
                      )
                      authCommonViewModel.setIsCountrySelected(false)
                      authCommonViewModel.zipCodeData.value = null
                      authCommonViewModel.countryFlagBmp(null)*/
                }
            })

        return binding?.root
    }

    private fun updateProfile() {
        if (authCommonViewModel.isUpdateProfile) {
            UserProfileStaticData.getUserProfileDataValue()?.let {
                registrationViewModel.updateProfile(
                    UpdateProfileRequest(
                        activeUser = true,
                        address1 = binding?.address1?.text?.toString(),
                        address2 = binding?.address2?.text?.toString(),
                        aliasName = it.aliasName,
                        authorized = true,
                        city = it.city,
                        community = it.community,
                        companyEmail = it.companyEmail,
                        emailId = it.emailId,
                        firstName = it.firstName,
                        interests = it.interests,
                        isAdmin = 0,
                        isFullNameAsAliasName = it.isFullNameAsAliasName,
                        isJobRecruiter = it.isJobRecruiter,
                        isPrimeUser = false,
                        isSurveyCompleted = false,
                        lastName = it.lastName,
                        newsletter = false,
                        originCity = it.originCity,
                        originCountry = it.originCountry,
                        originState = it.originState,
                        password = it.password,
                        phoneNo = binding?.phoneNumberAddressDetails?.text?.toString(),
                        regdEmailSent = false,
                        registeredBy = "manual",
                        userName = it.userName,
                        zipcode = it.zipcode,
                        state = it.state,
                        userType = it.userType,
                    )
                )
            }
        } else {
            findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
        }
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

    fun getCountryCode(countryName: String) =
        Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }

    override fun onCountrySelected() {
        countryCode = binding?.countryCodePicker?.selectedCountryCode
        countryName = binding?.countryCodePicker?.selectedCountryName
        binding?.selectedCountryName?.text = countryName

        authCommonViewModel.addCountryClicked(true)
        binding?.countryCodePicker?.selectedCountryNameCode?.let {
            binding?.countryCodePicker?.selectedCountryName?.let { it1 ->
                authCommonViewModel.setSelectedCountryAddressScreen(
                    it1, "",
                    it
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}