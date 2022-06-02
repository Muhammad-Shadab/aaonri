package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.SelectedCommunityAdapter
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentLocationDetailsBinding
import com.example.newsapp.utils.Resource
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    val commonViewModel: CommonViewModel by activityViewModels()
    var locationDetailsBinding: FragmentLocationDetailsBinding? = null
    var selectedCommunityAdapter: SelectedCommunityAdapter? = null
    val registrationViewModel: RegistrationViewModel by viewModels()

    var cityName: String = ""
    var stateName: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationDetailsBinding = FragmentLocationDetailsBinding.inflate(inflater, container, false)

        selectedCommunityAdapter = SelectedCommunityAdapter()

        commonViewModel.selectedCommunityList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                selectedCommunityAdapter
                locationDetailsBinding?.selectedCommunitySizeTv?.text =
                    "Your selected community ${it.size}"
                locationDetailsBinding?.selectedCardView?.visibility = View.VISIBLE
                selectedCommunityAdapter!!.setData(it)
                locationDetailsBinding?.selectCommunityEt?.visibility = View.GONE
                locationDetailsBinding?.selectMoreCommunityIv?.visibility = View.VISIBLE
            } else {
                locationDetailsBinding?.selectCommunityEt?.visibility = View.VISIBLE
                locationDetailsBinding?.selectedCardView?.visibility = View.GONE
            }
        }

        var job: Job? = null

        locationDetailsBinding?.apply {


            Toast.makeText(
                context,
                "${commonViewModel.locationDetails["city"]}",
                Toast.LENGTH_SHORT
            ).show()

            /*commonViewModel.selectedCountry?.observe(viewLifecycleOwner) {
                val countryCode = it.third
                if (it.first.isNotEmpty()) {
                    zipCodeLocationDetails.addTextChangedListener { editable ->
                        job?.cancel()
                        job = MainScope().launch {
                            delay(500L)
                            editable?.let {
                                if (editable.toString().isNotEmpty()) {
                                    registrationViewModel.getLocationByZipCode(
                                        zipCode.toString(),
                                        countryCode
                                    )
                                }
                            }
                        }
                    }
                    countryFlagIcon.load(it.second)
                    selectCountryOrigin.text = it.first
                    countryFlagIcon.visibility = View.VISIBLE
                } else {
                    countryFlagIcon.visibility = View.GONE
                }
            }*/

            selectMoreCommunityIv.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment_to_communityBottomFragment)
            }

            locationDetailsNextBtn.setOnClickListener {

//                val zipCode = zipCodeLocationDetails.text
                val state = stateLocationDetails.text
                val city = cityLocationDetails.text

                /* if (zipCode?.isNotEmpty() == true && state?.isNotEmpty() == true && city?.isNotEmpty() == true) {
                     commonViewModel.addLocationDetails(
                         zipCode.toString(),
                         state.toString(),
                         city.toString()
                     )
                 }*/
                findNavController().navigate(R.id.action_locationDetailsFragment_to_servicesCategoryFragment)
            }
            selectCommunityEt.setOnClickListener {
                findNavController().navigate(R.id.action_locationDetailsFragment_to_communityBottomFragment)
            }
            selectCountryOrigin.setOnClickListener {

            }

            rvLocationDetails.layoutManager = FlexboxLayoutManager(context)
            rvLocationDetails.adapter = selectedCommunityAdapter
        }

        /*registrationViewModel.zipCodeData.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    try {
                        cityName = response.data?.result?.get(1)?.district.toString()
                        stateName = response.data?.result?.get(1)?.state.toString()
                        locationDetailsBinding?.stateLocationDetails?.text =
                            response.data?.result?.get(1)?.state.toString()
                        locationDetailsBinding?.cityLocationDetails?.text =
                            response.data?.result?.get(1)?.district.toString()
                    } catch (e: Exception) {
                        Log.i("location", "onCreateView: ${e.localizedMessage}")
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }*/


        return locationDetailsBinding?.root
    }

}