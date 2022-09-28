package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.model.AdvertiseActivePageResponseItem
import com.aaonri.app.data.advertise.model.AdvertisementPageLocationXXX
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentSelectTemplateLocationBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateLocationAdapter
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class SelectTemplateLocationFragment : Fragment() {
    var binding: FragmentSelectTemplateLocationBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var advertiseTemplateLocationAdapter: AdvertiseTemplateLocationAdapter? = null
    var advertiseTemplateList = mutableListOf<AdvertisementPageLocationXXX>()

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
            binding?.advertiseTemplatesNextBtn?.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.greenBtnColor))
            /*binding?.tv3?.text = it.title
            binding?.tv3?.visibility = View.VISIBLE*/
            advertiseTemplateList.forEach { advertiseTemplate ->
                if (advertiseTemplate.locationId == it.locationId) {
                    binding?.tv3?.text = advertiseTemplate.title
                    binding?.tv3?.visibility = View.VISIBLE
                }
            }
            postAdvertiseViewModel.setTemplateLocation(it)
            //postAdvertiseViewModel.setAdvertiseImage("")
        }

        binding?.apply {

            val userArray = JSONObject(loadJSONFromAsset()).getJSONArray("AdvertiseTemplateInfo")
            val gson = Gson()

            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_TEMPLATE_LOCATION)
            tv1.text = "Select a location on the selected ${
                postAdvertiseViewModel.selectedTemplatePageName.value?.pageName + "," + System.getProperty(
                    "line.separator"
                )
            }for your advertisement display"
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

            for (i in 0 until userArray.length()) {
                if (!advertiseTemplateList.contains(
                        gson.fromJson(
                            userArray.getString(i),
                            AdvertisementPageLocationXXX::class.java
                        )
                    )
                ) {
                    advertiseTemplateList.add(
                        gson.fromJson(
                            userArray.getString(i),
                            AdvertisementPageLocationXXX::class.java
                        )
                    )
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

    private fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream = context?.assets?.open("locationtext.json")
            val size = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            val charset: Charset = Charsets.UTF_8
            inputStream?.read(buffer)
            inputStream?.close()
            json = buffer?.let { String(it, charset) }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}