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
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentSelectAdvertiseTemplateBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.adapter.AdvertiseTemplateAdapter
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAdvertiseTemplateFragment : Fragment() {
    var binding: FragmentSelectAdvertiseTemplateBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()

    var advertiseTemplateAdapter1: AdvertiseTemplateAdapter? = null

    //var advertiseTemplateAdapter2: AdvertiseTemplateAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSelectAdvertiseTemplateBinding.inflate(inflater, container, false)

        advertiseTemplateAdapter1 = AdvertiseTemplateAdapter {
            postAdvertiseViewModel.setTemplatePageName(it)
            binding?.pageNameTv?.text = it.pageName
            binding?.selectedPageDescTv?.text = it.description
            binding?.pageNameTv?.visibility = View.VISIBLE
            binding?.selectedPageDescTv?.visibility = View.VISIBLE
        }

        /* advertiseTemplateAdapter2 = AdvertiseTemplateAdapter {

         }*/

        binding?.apply {

            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_TEMPLATE)

            advertiseTemplatesNextBtn.setOnClickListener {
                val action =
                    SelectAdvertiseTemplateFragmentDirections.actionSelectAdvertiseTemplateToSelectTemplateLocation()
                findNavController().navigate(action)
                /*if (postAdvertiseViewModel.selectedTemplatePageName?.pageName?.isNotEmpty() == true) {
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
                }*/
            }

            horizontalRv1.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            horizontalRv1.adapter = advertiseTemplateAdapter1

        }

        postAdvertiseViewModel.advertisePageData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    response.data?.let { data -> advertiseTemplateAdapter1?.setData(data.filter { it.pageName != "Dashboard" }) }
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