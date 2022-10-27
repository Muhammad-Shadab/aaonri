package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseStaticData
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentUpdateAndDeleteBottomBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.AdvertiseScreenActivity
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AdvertiseUpdateDeleteFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentUpdateAndDeleteBottomBinding? = null
    val args: AdvertiseUpdateDeleteFragmentArgs by navArgs()
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentUpdateAndDeleteBottomBinding.inflate(inflater, container, false)

        if (args.isNavigateFromAllAdvertise) {
            advertiseViewModel.getAdvertiseDetailsById(args.advertiseId)
        }

        binding?.apply {

            if (AdvertiseStaticData.getAddDetails()?.approved == true) {
                updateAdvrst.visibility = View.GONE
                cancelAdvrstBtn.visibility = View.GONE
            }

            if (args.isAdvertiseExpired) {
                Toast.makeText(context, "${args.isAdvertiseExpired}", Toast.LENGTH_SHORT).show()
                renevAdvrstBtn.visibility = View.VISIBLE
            }

            cancelAdvrstBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to Cancel?")
                builder.setPositiveButton("OK") { dialog, which ->
                    advertiseViewModel.cancelAdvertise(args.advertiseId)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->

                }
                builder.show()
                //
            }

            renevAdvrstBtn.setOnClickListener {
                dismiss()
                val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                intent.putExtra("isRenewAdvertise", true)
                intent.putExtra("advertiseId", args.advertiseId)
                startActivityForResult(intent, 4)
            }

            updateAdvrst.setOnClickListener {
                dismiss()
                val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                intent.putExtra("isUpdateAdvertise", true)
                intent.putExtra("advertiseId", args.advertiseId)
                startActivityForResult(intent, 5)
            }

        }

        advertiseViewModel.cancelAdvertiseData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                when (response) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        dismiss()
                        findNavController().navigateUp()
                        advertiseViewModel.cancelAdvertiseData.postValue(null)
                        //advertiseViewModel.callAdvertiseApiAfterCancel(true)
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }

        advertiseViewModel.advertiseDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    AdvertiseStaticData.updateAdvertiseDetails(response.data)
                    binding?.progressBar?.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        advertiseViewModel.advertiseDetailsData.value = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}