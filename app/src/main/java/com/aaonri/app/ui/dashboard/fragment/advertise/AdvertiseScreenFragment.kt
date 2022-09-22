package com.aaonri.app.ui.dashboard.fragment.advertise

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.AllAdvertiseResponseItem
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.data.dashboard.DashboardCommonViewModel
import com.aaonri.app.databinding.FragmentAdvertiseScreenBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AdvertiseScreenFragment : Fragment() {
    var binding: FragmentAdvertiseScreenBinding? = null
    var advertiseAdapter: AdvertiseAdapter? = null
    val dashboardCommonViewModel: DashboardCommonViewModel by activityViewModels()
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()

    //var advertiseIdList = mutableListOf<Int>()
    var advertisementList = mutableListOf<AllAdvertiseResponseItem>()

    var isGuestUser = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val profile =
            context?.let { PreferenceManager<String>(it)[Constant.USER_PROFILE_PIC, ""] }

        val ss = SpannableString(resources.getString(R.string.login_to_view_Advertisement))
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity?.finish()
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.underlineColor =
                    context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
                ds.color = context?.let { ContextCompat.getColor(it, R.color.blueBtnColor) }!!
            }
        }
        ss.setSpan(clickableSpan1, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        advertiseAdapter = AdvertiseAdapter {
            val action =
                AdvertiseScreenFragmentDirections.actionAdvertiseScreenFragmentToAdvertisementDetailsFragment(
                    it.advertisementId
                )
            findNavController().navigate(action)
        }

        binding = FragmentAdvertiseScreenBinding.inflate(inflater, container, false)
        binding?.apply {
            loginToViewAdvertisement.text = ss
            loginToViewAdvertisement.movementMethod = LinkMovementMethod.getInstance()
            searchViewIcon.setOnClickListener {
                SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
            }

            floatingActionBtnEvents.setOnClickListener {
                val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                startActivityForResult(intent, 3)
            }

            context?.let { Glide.with(it).load(profile).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).centerCrop().into(profilePicIv) }

            recyclerViewAdvertise.layoutManager = LinearLayoutManager(context)
            recyclerViewAdvertise.adapter = advertiseAdapter
            if (isGuestUser) {
                isGuestUser = true

                binding?.loginToViewAdvertisement?.visibility = View.VISIBLE
                binding?.yourText?.visibility = View.GONE
                binding?.postingofAdTv?.visibility = View.GONE
                binding?.recyclerViewAdvertise?.visibility = View.GONE
                binding?.floatingActionBtnEvents?.visibility = View.GONE

            } else {
                isGuestUser = false
                binding?.loginToViewAdvertisement?.visibility = View.GONE
                binding?.yourText?.visibility = View.VISIBLE
                binding?.postingofAdTv?.visibility = View.VISIBLE
                binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                binding?.floatingActionBtnEvents?.visibility = View.VISIBLE
                //classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.VISIBLE
            }

            searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (searchView.hasFocus()) {
                        if (keyword.toString().isEmpty()) {
                            cancelbutton.visibility = View.GONE
                            searchViewIcon.visibility = View.VISIBLE
                        } else {
                            cancelbutton.visibility = View.VISIBLE
                            searchViewIcon.visibility = View.GONE
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            cancelbutton.setOnClickListener {
                cancelbutton.visibility = View.GONE
                searchViewIcon.visibility = View.VISIBLE
                if (searchView.text.isNotEmpty()) {
                    searchView.setText("")
                }

            }


        }
        dashboardCommonViewModel.isGuestUser.observe(viewLifecycleOwner) {
            if (it) {
                isGuestUser = true

                binding?.loginToViewAdvertisement?.visibility = View.VISIBLE
                binding?.yourText?.visibility = View.GONE
                binding?.postingofAdTv?.visibility = View.GONE
                binding?.recyclerViewAdvertise?.visibility = View.GONE
                binding?.floatingActionBtnEvents?.visibility = View.GONE

            } else {
                isGuestUser = false
                binding?.loginToViewAdvertisement?.visibility = View.GONE
                binding?.yourText?.visibility = View.VISIBLE
                binding?.postingofAdTv?.visibility = View.VISIBLE
                binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                binding?.floatingActionBtnEvents?.visibility = View.VISIBLE
                //classifiedDetailsBinding?.bottomViewForSpace?.visibility = View.VISIBLE
            }
        }

        advertiseViewModel.allAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE

                    binding?.yourText?.text =
                        "Your Avertisement(${response.data?.size})"

                    if (response.data?.isEmpty() == true) {
                        binding?.noResultFound?.visibility = View.VISIBLE
                        binding?.emptyTextVew?.text = "You haven't listed anything yet"
                        binding?.recyclerViewAdvertise?.visibility = View.GONE

                    } else {
                        /*response.data?.forEach {
                            if (!advertiseIdList.contains(it.advertisementId)) {
                                advertiseIdList.add(it.advertisementId)
                            }
                        }*/
                        response.data?.let { advertiseAdapter?.setData(it) }
                        response.data?.let { searchAdvertisement(it) }
                        binding?.noResultFound?.visibility = View.GONE
                        binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
                    }


                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
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
                    binding?.progressBar?.visibility = View.VISIBLE

                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    /* Toast.makeText(
                         context,
                         "${response.data?.advertisementDetails?.adImage}.",
                         Toast.LENGTH_SHORT
                     ).show()*/
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
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

        return binding?.root
    }


    private fun searchAdvertisement(data: List<AllAdvertiseResponseItem>) {

        binding?.searchView?.addTextChangedListener { editable ->
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
                binding?.noResultFound?.visibility = View.VISIBLE
                binding?.emptyTextVew?.text = "Results not found"
                binding?.recyclerViewAdvertise?.visibility = View.GONE
            } else {
                binding?.noResultFound?.visibility = View.GONE
                binding?.recyclerViewAdvertise?.visibility = View.VISIBLE
            }
            advertiseAdapter?.setData(advertisementList)
            binding?.recyclerViewAdvertise?.adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}