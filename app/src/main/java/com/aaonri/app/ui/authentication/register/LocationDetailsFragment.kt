package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.model.UpdateProfileRequest
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLocationDetailsBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.ui.authentication.register.adapter.SelectedCommunityAdapter
import com.aaonri.app.ui.dashboard.fragment.update_profile.UpdateProfileFragmentDirections
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.aaonri.app.utils.custom.UserProfileStaticData
import com.facebook.login.LoginManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by activityViewModels()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var binding: FragmentLocationDetailsBinding? = null
    var selectedCommunityAdapter: SelectedCommunityAdapter? = null
    var isCommunitySelected = false
    var isCountrySelected = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        getCommunities()

        selectedCommunityAdapter = SelectedCommunityAdapter()


        /*authCommonViewModel.selectedCountryAddressScreen?.observe(viewLifecycleOwner)*/

        /*if (!isCountrySelected) {
            authCommonViewModel.setSelectedCountryLocationScreen(
                countryName = "USA",
                countryFlag = "https://disease.sh/assets/img/flags/us.png",
                countryCode = "US"
            )
        }*/
        binding?.apply {
            if (authCommonViewModel.originLocationDetails["originState"]?.isNotEmpty() == true) {
                stateLocationDetails.setText(authCommonViewModel.originLocationDetails["originState"])
            }

            if (authCommonViewModel.originLocationDetails["originCity"]?.isNotEmpty() == true) {
                cityLocationDetails.setText(authCommonViewModel.originLocationDetails["originCity"])
            }
            authCommonViewModel.addNavigationForStepper(AuthConstant.LOCATION_DETAILS_SCREEN)
            countryFlagIcon.visibility = View.GONE
            selectMoreCommunityIv.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment_to_communityBottomFragment)
            }

            locationDetailsNextBtn.setOnClickListener {

                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())

                if (isCommunitySelected && selectCountryLocation.text.toString().isNotEmpty()) {
                    authCommonViewModel.addOriginLocationDetails(
                        originState = stateLocationDetails.text.toString(),
                        originCity = cityLocationDetails.text.toString()
                    )
                    findNavController().navigate(R.id.action_locationDetailsFragment_to_servicesCategoryFragment)
                } else if (authCommonViewModel.isUpdateProfile && selectCountryLocation.text.toString()
                        .isNotEmpty()
                ) {
                    updateProfile()
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please complete all details", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
            selectCommunityEt.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment_to_communityBottomFragment)
            }

            selectCountryOrigin.setOnClickListener {
                if (authCommonViewModel.isUpdateProfile) {
                    val action =
                        UpdateProfileFragmentDirections.actionUpdateProfileFragmentToSelectCountryBottomFragment2(
                            false
                        )
                    findNavController().navigate(action)
                } else {
                    val action =
                        LocationDetailsFragmentDirections.actionLocationDetailsFragmentToSelectCountryBottomFragment(
                            false
                        )
                    findNavController().navigate(action)
                }
            }

            rvLocationDetails.layoutManager = FlexboxLayoutManager(context)
            rvLocationDetails.adapter = selectedCommunityAdapter

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

        getCountries()

        authCommonViewModel.selectedCountryLocationScreen?.observe(viewLifecycleOwner) { triple ->
            isCountrySelected = true
            binding?.selectCountryLocation?.text = triple.first
            if (triple.second.isEmpty()) {
                binding?.countryFlagIcon?.visibility = View.GONE
            } else {
                binding?.countryFlagIcon?.load(triple.second)
                binding?.countryFlagIcon?.visibility = View.VISIBLE
            }
        }

        authCommonViewModel.selectedCommunityList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                isCommunitySelected = true
                binding?.selectedCommunitySizeTv?.text =
                    "Your selected ${if (it.size <= 1) "community" else "communities"} (${it.size})"
                binding?.selectedCardView?.visibility = View.VISIBLE
                selectedCommunityAdapter?.setData(it)
                binding?.selectCommunityEt?.visibility = View.GONE
                binding?.selectMoreCommunityIv?.visibility = View.VISIBLE
            } else {
                isCommunitySelected = false
                binding?.selectCommunityEt?.visibility = View.VISIBLE
                binding?.selectedCardView?.visibility = View.GONE
            }
        }

        if (authCommonViewModel.isUpdateProfile) {
            UserProfileStaticData.getUserProfileDataValue()?.let {
                //binding?.selectCountryOrigin?.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.advertiseTextBgCOlor))
                binding?.selectCountryLocation?.text = it.originCountry
                //binding?.selectCountryOrigin?.isEnabled = false
                binding?.stateLocationDetails?.setText(it.originState)
                binding?.cityLocationDetails?.setText(it.originCity)
                binding?.selectCommunityEt?.visibility = View.GONE
                binding?.locationDetailsNextBtn?.text = "UPDATE"
            }
        }

        if (authCommonViewModel.isUpdateProfile) {
            UserProfileStaticData.getUserProfileDataValue()?.let {
                when (it.originCountry) {
                    "Afghanistan" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "Afghanistan",
                            "https://disease.sh/assets/img/flags/af.png",
                            ""
                        )
                    }
                    "Bangladesh" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "Bangladesh",
                            "https://disease.sh/assets/img/flags/bd.png",
                            ""
                        )
                    }
                    "Bhutan" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "Bhutan",
                            "https://disease.sh/assets/img/flags/bt.png",
                            ""
                        )
                    }
                    "India" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "India",
                            "https://disease.sh/assets/img/flags/in.png",
                            ""
                        )
                    }
                    "Maldives" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "Maldives",
                            "https://disease.sh/assets/img/flags/mv.png",
                            ""
                        )
                    }
                    "Nepal" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "Nepal",
                            "https://disease.sh/assets/img/flags/np.png",
                            ""
                        )
                    }
                    "Pakistan" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "Pakistan",
                            "https://disease.sh/assets/img/flags/pk.png",
                            ""
                        )
                    }
                    "Sri Lanka" -> {
                        authCommonViewModel.setSelectedCountryLocationScreen(
                            "Sri Lanka",
                            "https://disease.sh/assets/img/flags/lk.png",
                            ""
                        )
                    }
                }
            }
            binding?.deleteProfileBtn?.visibility = View.VISIBLE
        }


        activity?.onBackPressedDispatcher
            ?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                    /*    authCommonViewModel.selectedCommunityList.value?.clear()
                        authCommonViewModel.setSelectedCountryLocationScreen("","","")*/
                }
            })

        return binding?.root
    }

    private fun updateProfile() {
        UserProfileStaticData.getUserProfileDataValue()?.let {
            registrationViewModel.updateProfile(
                UpdateProfileRequest(
                    activeUser = true,
                    address1 = it.address1,
                    address2 = it.address2,
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
                    originCity = binding?.cityLocationDetails?.text?.toString(),
                    originCountry = binding?.selectCountryLocation?.text.toString(),
                    originState = binding?.stateLocationDetails?.text?.toString(),
                    password = it.password,
                    phoneNo = it.phoneNo,
                    regdEmailSent = false,
                    registeredBy = "manual",
                    userName = it.userName,
                    zipcode = it.zipcode,
                    state = it.state,
                    userType = it.userType,
                    country = it.country
                )
            )
        }
    }

    private fun getCommunities() {

        authCommonViewModel.getCommunities()

        authCommonViewModel.communitiesList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                }
                is Resource.Error -> {
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()

                }
                else -> {}
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}