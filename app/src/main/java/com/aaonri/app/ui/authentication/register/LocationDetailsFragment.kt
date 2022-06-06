package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.AuthConstant
import com.aaonri.app.data.authentication.register.adapter.CommunityItemAdapter
import com.aaonri.app.data.authentication.register.adapter.SelectedCommunityAdapter
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLocationDetailsBinding
import com.example.newsapp.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    var locationDetailsBinding: FragmentLocationDetailsBinding? = null
    var selectedCommunityAdapter: SelectedCommunityAdapter? = null
    val registrationViewModel: RegistrationViewModel by viewModels()
    var isCommunitySelected = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationDetailsBinding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        getCommunities()

        selectedCommunityAdapter = SelectedCommunityAdapter()

        locationDetailsBinding?.apply {

            authCommonViewModel.addNavigationForStepper(AuthConstant.LOCATION_DETAILS_SCREEN)

            authCommonViewModel.apply {
                stateLocationDetails.text = locationDetails["state"].toString()
                cityLocationDetails.setText(locationDetails["city"].toString())
                selectCountryLocation.text = selectedCountry?.value?.first
                countryFlagIcon.load(selectedCountry?.value?.second)
            }

            selectMoreCommunityIv.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment_to_communityBottomFragment)
            }

            locationDetailsNextBtn.setOnClickListener {
                val imm =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                if (stateLocationDetails.text.isNotEmpty() && cityLocationDetails.text.isNotEmpty() && isCommunitySelected) {
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

            rvLocationDetails.layoutManager = FlexboxLayoutManager(context)
            rvLocationDetails.adapter = selectedCommunityAdapter
        }

        authCommonViewModel.selectedCommunityList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                isCommunitySelected = true
                selectedCommunityAdapter
                locationDetailsBinding?.selectedCommunitySizeTv?.text =
                    "Your selected community (${it.size})"
                locationDetailsBinding?.selectedCardView?.visibility = View.VISIBLE
                selectedCommunityAdapter!!.setData(it)
                locationDetailsBinding?.selectCommunityEt?.visibility = View.GONE
                locationDetailsBinding?.selectMoreCommunityIv?.visibility = View.VISIBLE
            } else {
                isCommunitySelected = false
                locationDetailsBinding?.selectCommunityEt?.visibility = View.VISIBLE
                locationDetailsBinding?.selectedCardView?.visibility = View.GONE
            }
        }


        return locationDetailsBinding?.root
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

}