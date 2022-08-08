package com.aaonri.app.ui.dashboard.fragment.advertise

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aaonri.app.R
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


        advertiseAdapter = AdvertiseAdapter {

        }

        advertiseBinding = FragmentAdvertiseScreenBinding.inflate(inflater, container, false)
        advertiseBinding?.apply {
            floatingActionBtnEvents.setOnClickListener {
                val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                startActivityForResult(intent, 3)
            }
        }

        advertiseViewModel.allAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    advertiseBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    advertiseBinding?.progressBar?.visibility = View.GONE

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

        return advertiseBinding?.root
    }
}