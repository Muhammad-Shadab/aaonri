package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.model.AdvertiseActivePageResponseItem
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.data.immigration.model.ImmigrationCenterModelItem
import com.aaonri.app.databinding.FragmentSelectAdvertiseTemplateBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateAdapter
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

@AndroidEntryPoint
class SelectAdvertiseTemplateFragment : Fragment() {
    var binding: FragmentSelectAdvertiseTemplateBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()

    var advertiseTemplateAdapter1: AdvertiseTemplateAdapter? = null
    var advertiseTemplateList = mutableListOf<AdvertiseActivePageResponseItem>()

    //var advertiseTemplateAdapter2: AdvertiseTemplateAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectAdvertiseTemplateBinding.inflate(inflater, container, false)

        advertiseTemplateAdapter1 = AdvertiseTemplateAdapter {
            binding?.advertiseTemplatesNextBtn?.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.greenBtnColor))
            postAdvertiseViewModel.setTemplatePageName(it)
            binding?.pageNameTv?.text = it.title
            binding?.selectedPageDescTv?.text = it.description
            binding?.pageNameTv?.visibility = View.VISIBLE
            binding?.selectedPageDescTv?.visibility = View.VISIBLE
        }

        /* advertiseTemplateAdapter2 = AdvertiseTemplateAdapter {

         }*/

        binding?.apply {

            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_TEMPLATE)
            val userArray = JSONObject(loadJSONFromAsset()).getJSONArray("AdvertisePageInfo")
            val gson = Gson()
            advertiseTemplatesNextBtn.setOnClickListener {
                if (context?.let { PreferenceManager<Int>(it)["selectedTemplatePage", -1] } != -1) {
                    val action =
                        SelectAdvertiseTemplateFragmentDirections.actionSelectAdvertiseTemplateToSelectTemplateLocation()
                    findNavController().navigate(action)
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please choose template", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            for (i in 0 until userArray.length()) {
                if (!advertiseTemplateList.contains(
                        gson.fromJson(
                            userArray.getString(i),
                            AdvertiseActivePageResponseItem::class.java
                        )
                    )
                ) {
                    advertiseTemplateList.add(
                        gson.fromJson(
                            userArray.getString(i),
                            AdvertiseActivePageResponseItem::class.java
                        )
                    )
                }
            }
            advertiseTemplateAdapter1?.setData(advertiseTemplateList)
            horizontalRv1.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            horizontalRv1.adapter = advertiseTemplateAdapter1

        }


        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    private fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream = context?.assets?.open("pageid.json")
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

}