package com.aaonri.app.ui.dashboard.fragment.advertise

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.model.AllAdvertiseResponseItem
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentAdvertiseScreenBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AdvertiseScreenFragment : Fragment() {
    var advertiseBinding: FragmentAdvertiseScreenBinding? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()

    //var advertiseIdList = mutableListOf<Int>()
    var advertisementList = mutableListOf<AllAdvertiseResponseItem>()

    @SuppressLint("SetTextI18n")
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

            searchViewIcon.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
            }

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

                    advertiseBinding?.yourText?.text =
                        "Yout Avertisement(${response.data?.size})"

                    if (response.data?.isEmpty() == true) {
                        advertiseBinding?.noResultFound?.visibility = View.VISIBLE
                        advertiseBinding?.emptyTextVew?.text = "You haven't listed anything yet"
                        advertiseBinding?.recyclerViewAdvertise?.visibility = View.GONE

                    } else {
                        /*response.data?.forEach {
                            if (!advertiseIdList.contains(it.advertisementId)) {
                                advertiseIdList.add(it.advertisementId)
                            }
                        }*/
                        response.data?.let { advertiseAdapter?.setData(it) }
                        response.data?.let { searchAdvertisement(it) }
                        advertiseBinding?.noResultFound?.visibility = View.GONE
                        advertiseBinding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                    }


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
                    /* Toast.makeText(
                         context,
                         "${response.data?.advertisementDetails?.adImage}.",
                         Toast.LENGTH_SHORT
                     ).show()*/
                }
                is Resource.Error -> {
                    advertiseBinding?.progressBar?.visibility = View.GONE
                }
                else -> {}
            }
        }

        /*if (advertiseIdList.isNotEmpty()) {
            advertiseIdList.forEachIndexed { index, i ->
                if (index == 0) {
                    //advertiseViewModel.getAdvertiseDetailsById(i)
                }
            }
        }*/

        return advertiseBinding?.root
    }


    private fun searchAdvertisement(data: List<AllAdvertiseResponseItem>) {
        advertiseBinding?.searchView?.addTextChangedListener { editable ->
            advertisementList.clear()
            val searchText = editable.toString().lowercase(Locale.getDefault())
            if (searchText.isNotEmpty()) {
                data.forEach {
                    if (it.title.lowercase(Locale.getDefault()).contains(searchText)) {
                        advertisementList.add(it)
                    }
                }


            } else {
                advertisementList.clear()
                data.let { advertisementList.addAll(it) }
            }
            if (advertisementList.isEmpty()) {
                advertiseBinding?.noResultFound?.visibility = View.VISIBLE
                advertiseBinding?.emptyTextVew?.text = "Results not found"
                advertiseBinding?.recyclerViewAdvertise?.visibility = View.GONE
            } else {
                advertiseBinding?.noResultFound?.visibility = View.GONE
                advertiseBinding?.recyclerViewAdvertise?.visibility = View.VISIBLE
            }
            advertiseAdapter?.setData(advertisementList)
            advertiseBinding?.recyclerViewAdvertise?.adapter?.notifyDataSetChanged()
        }
    }
}