package com.aaonri.app.ui.authentication.register

import android.app.AlertDialog
import android.content.Intent
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
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.aaonri.app.utils.custom.UserProfileStaticData
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var cityName: String = ""
    var stateName: String = ""
    var zipCode: String = ""
    private var countryCode: String? = null
    private var countryName: String? = null
    var clearAllData = false

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


            stateNameAddressDetails.setText(stateName)
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
                                    //invalidZipCodeTv.visibility = View.GONE
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
                val userEnteredState = stateNameAddressDetails.text
                val zipCode = zipCodeAddressDetails.text

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                countryCodePicker.imageViewFlag.invalidate()
                val drawable = countryCodePicker.imageViewFlag.drawable

                authCommonViewModel.countryFlagBmp(drawable.toBitmap())

                if (userEnteredCity.toString().length >= 2 && zipCode.toString().isNotEmpty() && zipCode.toString().length >= 4 && userEnteredState.toString().length >= 2
                ) {
                    if (cityName.isEmpty()) {
                        cityName = userEnteredCity.toString()
                    }
                    if (stateName.isEmpty()) {
                        stateName = userEnteredState.toString()
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

            deleteProfileBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to Delete your account? This action would be irreversible and you will not be able to use any services of aaonri.")
                builder.setPositiveButton("Delete") { dialog, which ->

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_EMAIL, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_ZIP_CODE, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_CITY, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_STATE, "")

                    context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                        ?.set(Constant.IS_USER_LOGIN, false)

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_PROFILE_PIC, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.GMAIL_FIRST_NAME, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.GMAIL_LAST_NAME, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_INTERESTED_SERVICES, "")

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_NAME, "")

                    context?.let { it1 -> PreferenceManager<Boolean>(it1) }
                        ?.set(Constant.IS_JOB_RECRUITER, false)

                    context?.let { it1 -> PreferenceManager<String>(it1) }
                        ?.set(Constant.USER_PHONE_NUMBER, "")

                    context?.let { it1 -> PreferenceManager<Int>(it1) }
                        ?.set(Constant.USER_ID, 0)

                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.gmail_client_id))
                        .requestEmail()
                        .build()

                    FirebaseAuth.getInstance().signOut()
                    LoginManager.getInstance().logOut()
                    mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!
                    mGoogleSignInClient.signOut()

                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                builder.setNegativeButton("Cancel") { dialog, which ->

                }
                builder.show()
            }
        }

        authCommonViewModel.countryClicked.observe(viewLifecycleOwner) {
            if (it) {
                binding?.stateNameAddressDetails?.setText("")
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
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
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
                        binding?.stateNameAddressDetails?.setText(if (authCommonViewModel.locationDetails["state"]?.isNotEmpty() == true) authCommonViewModel.locationDetails["state"].toString() else stateName)

                        //binding?.invalidZipCodeTv?.visibility = View.GONE
                    } else {
                        binding?.cityNameAddressDetails?.setText("")
                        //binding?.invalidZipCodeTv?.visibility = View.VISIBLE
                        binding?.stateNameAddressDetails?.setText("")
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
                zipCode = it.zipcode
                if (it.country != null) {
                    binding?.countryCodePicker?.setCountryForNameCode(getCountryCode(it.country))
                }
                //binding?.countryPickerLl?.visibility = View.GONE
                binding?.address1?.setText(it.address1)
                binding?.address2?.setText(it.address2)
                binding?.zipCodeAddressDetails?.setText(it.zipcode)
                //binding?.zipCodeAddressDetails?.isEnabled = false
                //binding?.zipCodeAddressDetails?.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.cityNameAddressDetails?.setText(it.city)
                //binding?.cityNameAddressDetails?.isEnabled = false
                //binding?.cityNameAddressDetails?.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                //binding?.stateNameAddressDetails?.isEnabled = false
                //binding?.stateNameAddressDetails?.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.stateNameAddressDetails?.setText(stateName)
                //if (it.state != null) it.state.toString() else ""

                binding?.phoneNumberAddressDetails?.setText(
                    it.phoneNo.replace("""[(,), ]""".toRegex(), "")
                        .replace("-", "").replaceFirst("(\\d{3})(\\d{3})(\\d+)".toRegex(), "$1-$2-$3")
                )
                binding?.addressDetailsNextBtn?.text = "UPDATE"
            }
            binding?.deleteProfileBtn?.visibility = View.VISIBLE
        }


        activity?.onBackPressedDispatcher
            ?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
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
                        city = binding?.cityNameAddressDetails?.text.toString(),
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
                        zipcode = binding?.zipCodeAddressDetails?.text.toString(),
                        state = binding?.stateNameAddressDetails?.text.toString(),
                        userType = it.userType,
                        country = binding?.selectedCountryName?.text.toString()
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
        if (authCommonViewModel.isUpdateProfile) {
            if (clearAllData) {
                authCommonViewModel.addCountryClicked(true)
            }
            clearAllData = true
        } else {
            authCommonViewModel.addCountryClicked(true)
        }
        binding?.countryCodePicker?.selectedCountryNameCode?.let {
            binding?.countryCodePicker?.selectedCountryName?.let { it1 ->
                authCommonViewModel.setSelectedCountryAddressScreen(
                    it1, "",
                    it
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}