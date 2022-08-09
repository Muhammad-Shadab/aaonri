package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.advertise.model.homeTemplates
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentSelectAdvertiseTemplateBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAdvertiseTemplateFragment : Fragment() {
    var binding: FragmentSelectAdvertiseTemplateBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var advertiseTemplateAdapter1: AdvertiseTemplateAdapter? = null
    var advertiseTemplateAdapter2: AdvertiseTemplateAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectAdvertiseTemplateBinding.inflate(inflater, container, false)

        advertiseTemplateAdapter1 = AdvertiseTemplateAdapter {
            postAdvertiseViewModel.setTemplateName(it)
        }

        advertiseTemplateAdapter2 = AdvertiseTemplateAdapter {

        }

        binding?.apply {

            advertiseTemplatesNextBtn.setOnClickListener {
                if (postAdvertiseViewModel.selectTemplateName.isNotEmpty()) {
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

            horizontalRv1.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            horizontalRv2.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            advertiseTemplateAdapter1?.setData(homeTemplates.listOfModule)

            /*advertiseTemplateAdapter2?.setData(
                listOf(
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-just-above-footer-image-only.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-just-above-footer-text-only.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-top-banner.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-below-discover-aaonri.png",
                    "http://aaonridevnew.aaonri.com/assets/img/advertisingpage/dashboard-below-interest-area.png",
                )
            )*/

            horizontalRv1.adapter = advertiseTemplateAdapter1
            horizontalRv2.adapter = advertiseTemplateAdapter2

        }

        return binding?.root
    }
}