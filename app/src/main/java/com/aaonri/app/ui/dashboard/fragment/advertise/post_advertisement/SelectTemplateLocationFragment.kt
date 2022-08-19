package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentSelectTemplateLocationBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateLocationAdapter
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar

class SelectTemplateLocationFragment : Fragment() {
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
            binding?.tv3?.text = it.title
            binding?.tv3?.visibility = View.VISIBLE
            postAdvertiseViewModel.setTemplateLocation(it)
            postAdvertiseViewModel.setAdvertiseImage("")

        }

        binding?.apply {

            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_TEMPLATE_LOCATION)

            advertiseTemplatesNextBtn.setOnClickListener {
                if (context?.let { PreferenceManager<Int>(it)["selectedTemplateLocation", -1] } != -1) {
                    val action =
                        SelectTemplateLocationFragmentDirections.actionSelectTemplateLocationToPostAdvertisementbasicDetailsFragment()
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

        }

        postAdvertiseViewModel.advertisePageLocationData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { advertiseTemplateLocationAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }


        return binding?.root
    }
}