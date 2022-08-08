package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.databinding.FragmentSelectAdvertiseTemplateBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAdvertiseTemplateFragment : Fragment() {
    var binding: FragmentSelectAdvertiseTemplateBinding? = null
    var advertiseTemplateAdapter1: AdvertiseTemplateAdapter? = null
    var advertiseTemplateAdapter2: AdvertiseTemplateAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectAdvertiseTemplateBinding.inflate(inflater, container, false)

        advertiseTemplateAdapter1 = AdvertiseTemplateAdapter {

        }

        advertiseTemplateAdapter2 = AdvertiseTemplateAdapter {

        }

        binding?.apply {


            advertiseTemplatesNextBtn.setOnClickListener {
                val action =
                    SelectAdvertiseTemplateFragmentDirections.actionSelectAdvertiseTemplateToPostAdvertisementbasicDetailsFragment()
                findNavController().navigate(action)
            }

            horizontalRv1.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            horizontalRv2.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            advertiseTemplateAdapter1?.setData(
                listOf(
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png"
                )
            )

            advertiseTemplateAdapter2?.setData(
                listOf(
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/immigration-just-above-footer-image-only1.png"
                )
            )

            horizontalRv1.adapter = advertiseTemplateAdapter1
            horizontalRv2.adapter = advertiseTemplateAdapter2

        }

        return binding?.root

    }
}