package com.aaonri.app.ui.authentication.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.databinding.FragmentAddressDetailsBinding
import com.example.newsapp.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class AddressDetailsFragment : Fragment() {
    val commonViewModel: CommonViewModel by activityViewModels()
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

            val zipCode = zipCodeAddressDetails.text


            commonViewModel.addSelectedCountry(
                countryName = "USA",
                countryFlag = "https://disease.sh/assets/img/flags/us.png",
                countryCode = "US"
            )

            commonViewModel.selectedCountry?.observe(viewLifecycleOwner) { triple ->

                if (triple.first.isNotEmpty() || triple.third.isNotEmpty()) {
                    zipCodeAddressDetails.addTextChangedListener { editable ->
                        job?.cancel()
                        job = MainScope().launch {
                            delay(500L)
                            editable?.let {
                                if (editable.toString().isNotEmpty()) {
                                    commonViewModel.getLocationByZipCode(
                                        zipCode.toString(),
                                        triple.third
                                    )
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

                if (address1.toString().isNotEmpty() && address2.toString()
                        .isNotEmpty() && phoneNumber.toString().isNotEmpty()
                ) {
                    commonViewModel.addAddressDetails(
                        address1.toString(),
                        address2.toString(),
                        phoneNumber.toString()
                    )
                }
                findNavController().navigate(R.id.action_addressDetailsFragment_to_locationDetailsFragment)
            }
        }

        commonViewModel.zipCodeData.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    try {
                        cityName = response.data?.result?.get(1)?.district.toString()
                        stateName = response.data?.result?.get(1)?.state.toString()
                        addressDetailsBinding?.stateNameAddressDetails?.text =
                            response.data?.result?.get(1)?.state.toString()
                        addressDetailsBinding?.cityNameAddressDetails?.setText(
                            response.data?.result?.get(
                                1
                            )?.district.toString()
                        )
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
        }

        return addressDetailsBinding?.root
    }

    private fun getCountries() {
        commonViewModel.getCountries()
        commonViewModel.countriesData.observe(viewLifecycleOwner) { response ->
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