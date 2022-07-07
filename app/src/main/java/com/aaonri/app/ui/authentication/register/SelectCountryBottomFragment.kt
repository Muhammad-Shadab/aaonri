package com.aaonri.app.ui.authentication.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.ui.authentication.register.adapter.CountryAdapter
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponseItem
import com.aaonri.app.data.authentication.register.model.countries.CountryInfo
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentSelectCountryBottomBinding
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SelectCountryBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val authCommonViewModel: AuthCommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by viewModels()
    var countryBottomBinding: FragmentSelectCountryBottomBinding? = null
    var countryAdapter: CountryAdapter? = null
    var tempArrayList = ArrayList<CountriesResponseItem>()
    val args: SelectCountryBottomFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        countryBottomBinding =
            FragmentSelectCountryBottomBinding.inflate(inflater, container, false)

        getCountries()

        countryAdapter = CountryAdapter { countryName, countryFlag, countryCode ->
            if (args.isAddressScreen) {
                authCommonViewModel.addCountryClicked(true)
                authCommonViewModel.setSelectedCountryAddressScreen(
                    countryName,
                    countryFlag,
                    countryCode
                )
            } else {
                authCommonViewModel.setSelectedCountryLocationScreen(
                    countryName,
                    countryFlag,
                    countryCode
                )
            }
            findNavController().navigateUp()
        }

        countryBottomBinding?.apply {

            closeCountryBtn.setOnClickListener {
                dismiss()
            }

            val southAsiaCountry = mutableListOf<CountriesResponseItem>()
            southAsiaCountry.add(CountriesResponseItem("Afghanistan", CountryInfo("https://disease.sh/assets/img/flags/af.png","")))
            southAsiaCountry.add(CountriesResponseItem("Bangladesh", CountryInfo("https://disease.sh/assets/img/flags/bd.png","")))
            southAsiaCountry.add(CountriesResponseItem("Bhutan", CountryInfo("https://disease.sh/assets/img/flags/bt.png","")))
            southAsiaCountry.add(CountriesResponseItem("India", CountryInfo("https://disease.sh/assets/img/flags/in.png","")))
            southAsiaCountry.add(CountriesResponseItem("Maldives", CountryInfo("https://disease.sh/assets/img/flags/mv.png","")))
            southAsiaCountry.add(CountriesResponseItem("Nepal", CountryInfo("https://disease.sh/assets/img/flags/np.png","")))
            southAsiaCountry.add(CountriesResponseItem("Pakistan", CountryInfo("https://disease.sh/assets/img/flags/pk.png","")))
            southAsiaCountry.add(CountriesResponseItem("Sri Lanka", CountryInfo("https://disease.sh/assets/img/flags/lk.png","")))

            countryAdapter?.setData(southAsiaCountry)
            countriesRv.layoutManager = LinearLayoutManager(context)
            countriesRv.adapter = countryAdapter
        }

        return countryBottomBinding?.root
    }

    private fun getCountries() {
//        authCommonViewModel.countriesData.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is Resource.Error -> {
//                    countryBottomBinding?.progressBarCommunityBottom?.visibility = View.GONE
//                }
//                is Resource.Loading -> {
//                    countryBottomBinding?.progressBarCommunityBottom?.visibility = View.VISIBLE
//                }
//                is Resource.Success -> {
//                    val southAsiaCountry = mutableListOf<CountriesResponseItem>()
//                    searchCountry(response.data)
//                    countryBottomBinding?.progressBarCommunityBottom?.visibility = View.GONE
//                    if (args.isAddressScreen) {
//                        response.data?.let { countryAdapter?.setData(response.data) }
//                    } else {
//                      /*  response.data?.forEach {
//                            when(it.country){
//                                "Afghanistan" -> {southAsiaCountry.add(it)}
//                                "Bangladesh" -> {southAsiaCountry.add(it)}
//                                "Bhutan" -> {southAsiaCountry.add(it)}
//                                "India" -> {southAsiaCountry.add(it)}
//                                "Maldives" -> {southAsiaCountry.add(it)}
//                                "Nepal" -> {southAsiaCountry.add(it)}
//                                "Pakistan" -> {southAsiaCountry.add(it)}
//                                "Sri Lanka" -> {southAsiaCountry.add(it)}
//                            }
//                        }*/
//                        response.data?.let { countryAdapter?.setData(southAsiaCountry) }
//                    }
//                }
//                is Resource.Empty -> {
//                    TODO()
//                }
//            }
//        }
    }

    private fun searchCountry(data: CountriesResponse?) {
        countryBottomBinding?.countrySearchView?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    data?.forEach {
                        if (it.country.lowercase(Locale.getDefault()).contains(searchText)) {
                            tempArrayList.add(it)
                        }
                    }
                    countryAdapter?.setData(tempArrayList)
                    countryBottomBinding?.countriesRv?.adapter?.notifyDataSetChanged()
                } else {
                    tempArrayList.clear()
                    data?.let { tempArrayList.addAll(it) }
                    countryAdapter?.setData(tempArrayList)
                }
                countryAdapter?.setData(tempArrayList)
                return false
            }
        })
    }
}