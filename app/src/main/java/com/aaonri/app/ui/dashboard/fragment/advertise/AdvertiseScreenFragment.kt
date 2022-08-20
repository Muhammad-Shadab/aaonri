package com.aaonri.app.ui.dashboard.fragment.advertise

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentAdvertiseScreenBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvertiseScreenFragment : Fragment() {
    var advertiseBinding: FragmentAdvertiseScreenBinding? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    var advertiseIdList = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.PROFILE_USER, ""] }

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

            context?.let { Glide.with(it).load(profile).into(profilePicIv) }

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
                    response.data?.forEach {
                        if (!advertiseIdList.contains(it.advertisementId)) {
                            advertiseIdList.add(it.advertisementId)
                        }
                    }
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

        if (advertiseIdList.isNotEmpty()) {
            advertiseIdList.forEachIndexed { index, i ->
                if (index == 0) {
                    advertiseViewModel.getAdvertiseDetailsById(i)
                }
            }
        }

        return advertiseBinding?.root
    }
}