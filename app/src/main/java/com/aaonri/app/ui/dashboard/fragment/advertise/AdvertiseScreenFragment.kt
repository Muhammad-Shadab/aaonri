package com.aaonri.app.ui.dashboard.fragment.advertise

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentAdvertiseScreenBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.ui.dashboard.fragment.event.EventScreenActivity
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvertiseScreenFragment : Fragment() {
    var advertiseBinding: FragmentAdvertiseScreenBinding? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
        startActivityForResult(intent, 3)

        advertiseAdapter = AdvertiseAdapter {
            val action =
                AdvertiseScreenFragmentDirections.actionAdvertiseScreenFragmentToAdvertisementDetailsFragment(
                    it.advertisementId
                )
            findNavController().navigate(action)
        }

        advertiseBinding = FragmentAdvertiseScreenBinding.inflate(inflater, container, false)
        advertiseBinding?.apply {

            floatingActionBtnEvents.setOnClickListener {
                val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                startActivityForResult(intent, 3)
            }

            recyclerViewAdvertise.layoutManager = LinearLayoutManager(context)
            recyclerViewAdvertise.adapter = advertiseAdapter

        }


        advertiseViewModel.allAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    advertiseBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    advertiseBinding?.progressBar?.visibility = View.GONE
                    /*response.data?.forEach {
                        Toast.makeText(context, "${it.advertisementId}", Toast.LENGTH_SHORT).show()
                        advertiseViewModel.getAdvertiseDetailsById(it.advertisementId)
                    }*/
                    response.data?.let { advertiseAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    advertiseBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "Error ${response.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                }
            }
        }

        advertiseViewModel.advertiseDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    advertiseBinding?.progressBar?.visibility = View.VISIBLE

                }
                is Resource.Success -> {
                    advertiseBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "${response.data?.advertisementDetails?.adImage}.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> {
                    advertiseBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        return advertiseBinding?.root
    }
}