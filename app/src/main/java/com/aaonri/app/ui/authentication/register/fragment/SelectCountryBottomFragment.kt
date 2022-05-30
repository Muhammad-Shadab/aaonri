package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.adapter.CountryAdapter
import com.aaonri.app.data.authentication.register.viewmodel.CommonViewModel
import com.aaonri.app.data.authentication.register.viewmodel.RegistrationViewModel
import com.aaonri.app.databinding.FragmentSelectCountryBottomBinding
import com.example.newsapp.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCountryBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    val commonViewModel: CommonViewModel by activityViewModels()
    val registrationViewModel: RegistrationViewModel by viewModels()
    var countryBottomBinding: FragmentSelectCountryBottomBinding? = null
    var countryAdapter: CountryAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        countryBottomBinding =
            FragmentSelectCountryBottomBinding.inflate(inflater, container, false)
        registrationViewModel.getCountriesList()
        countryAdapter = CountryAdapter {
            commonViewModel.selectCountry(it)
            findNavController().navigate(R.id.action_selectCountryBottomFragment_to_locationDetailsFragment2)
        }

        lifecycleScope.launchWhenCreated {
            registrationViewModel.countriesList.collect { response ->
                when (response) {
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                        countryBottomBinding?.progressBarCommunityBottom?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        countryBottomBinding?.progressBarCommunityBottom?.visibility = View.GONE
                        response.data?.let { countryAdapter?.setData(it) }
                    }
                }
            }
        }

        countryBottomBinding?.apply {
            closeCountryBtn.setOnClickListener {
                dismiss()
            }
            countriesRv.layoutManager = LinearLayoutManager(context)
            countriesRv.adapter = countryAdapter
        }

        return countryBottomBinding?.root
    }
}