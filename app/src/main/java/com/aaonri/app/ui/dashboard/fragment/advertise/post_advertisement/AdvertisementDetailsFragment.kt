package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentAdvertisementDetailsBinding
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvertisementDetailsFragment : Fragment() {
    var detailsBinding: FragmentAdvertisementDetailsBinding? = null
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    val args: AdvertisementDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsBinding = FragmentAdvertisementDetailsBinding.inflate(inflater, container, false)


        detailsBinding?.apply {


        }

        advertiseViewModel.advertiseDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    detailsBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    setData(response.data)
                    detailsBinding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    detailsBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        return detailsBinding?.root
    }

    private fun setData(data: AdvertiseDetailsResponse?) {

    }

    override fun onResume() {
        super.onResume()
        advertiseViewModel.getAdvertiseDetailsById(args.advertiseId)
    }

    override fun onDestroy() {
        super.onDestroy()
        advertiseViewModel.advertiseDetailsData.value = null
    }

}