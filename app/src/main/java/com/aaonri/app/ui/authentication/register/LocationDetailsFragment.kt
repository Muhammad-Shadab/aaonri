package com.aaonri.app.ui.authentication.register

import com.aaonri.app.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLocationDetailsBinding
import com.aaonri.app.ui.authentication.register.adapter.SelectedCommunityAdapter
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    var binding: FragmentLocationDetailsBinding? = null
    var selectedCommunityAdapter: SelectedCommunityAdapter? = null
    val registrationViewModel: RegistrationViewModel by viewModels()
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
              if(authCommonViewModel.originLocationDetails["originState"]?.isNotEmpty() == true)
              {
                  stateLocationDetails.setText(authCommonViewModel.originLocationDetails["originState"])
              }

            if(authCommonViewModel.originLocationDetails["originCity"]?.isNotEmpty() == true)
            {
                cityLocationDetails.setText(authCommonViewModel.originLocationDetails["originCity"])
            }
            authCommonViewModel.addNavigationForStepper(AuthConstant.LOCATION_DETAILS_SCREEN)
            countryFlagIcon.visibility= View.GONE
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
                val action =
                    LocationDetailsFragmentDirections.actionLocationDetailsFragmentToSelectCountryBottomFragment(
                        false
                    )
                findNavController().navigate(action)
            }

            rvLocationDetails.layoutManager = FlexboxLayoutManager(context)
            rvLocationDetails.adapter = selectedCommunityAdapter
        }

        getCountries()

        authCommonViewModel.selectedCountryLocationScreen?.observe(viewLifecycleOwner) { triple ->
            isCountrySelected = true
            binding?.selectCountryLocation?.text = triple.first


            if(triple.second.isEmpty())
            {
                binding?.countryFlagIcon?.visibility = View.GONE
            }
            else{
                binding?.countryFlagIcon?.load(triple.second)
                binding?.countryFlagIcon?.visibility = View.VISIBLE
            }
        }

        authCommonViewModel.selectedCommunityList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                isCommunitySelected = true
                binding?.selectedCommunitySizeTv?.text =
                    "Your selected ${if(it.size<=1)"community" else "communities" } (${it.size})"
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

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                /*    authCommonViewModel.selectedCommunityList.value?.clear()
                    authCommonViewModel.setSelectedCountryLocationScreen("","","")*/
                }
            })
        return binding?.root
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
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}