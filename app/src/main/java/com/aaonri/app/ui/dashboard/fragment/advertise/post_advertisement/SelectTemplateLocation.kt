package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.model.dashboardLocationTemplate
import com.aaonri.app.data.advertise.model.landingPageLocationTemplate
import com.aaonri.app.data.advertise.model.productDetailsLocationTemplate
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentSelectTemplateLocationBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateLocationAdapter
import com.google.android.material.snackbar.Snackbar

class SelectTemplateLocation : Fragment() {
    var binding: FragmentSelectTemplateLocationBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var advertiseTemplateLocationAdapter: AdvertiseTemplateLocationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectTemplateLocationBinding.inflate(
            inflater,
            container,
            false
        )

        advertiseTemplateLocationAdapter = AdvertiseTemplateLocationAdapter {
            binding?.tv3?.text = it
            binding?.tv3?.visibility = View.VISIBLE
            postAdvertiseViewModel.setTemplateLocation(it)
        }

        binding?.apply {

            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_TEMPLATE_LOCATION)

            advertiseTemplatesNextBtn.setOnClickListener {
                if (postAdvertiseViewModel.selectTemplateLocation.isNotEmpty()) {
                    val action =
                        SelectTemplateLocationDirections.actionSelectTemplateLocationToPostAdvertisementbasicDetailsFragment()
                    findNavController().navigate(action)
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please choose template location", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            horizontalRv1.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            horizontalRv1.adapter = advertiseTemplateLocationAdapter

            horizontalRv2.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }

        when (postAdvertiseViewModel.selectTemplateName) {
            "Landing Page" -> {
                advertiseTemplateLocationAdapter?.setData(landingPageLocationTemplate.listOfModule)
            }
            "Product Details" -> {
                advertiseTemplateLocationAdapter?.setData(productDetailsLocationTemplate.listOfModule)
            }
            "Dashboard" -> {
                advertiseTemplateLocationAdapter?.setData(dashboardLocationTemplate.listOfModule)
            }
        }

        return binding?.root
    }
}