package com.aaonri.app.ui.dashboard.fragment.advertise.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.R
import com.aaonri.app.data.advertise.viewmodel.AdvertiseViewModel
import com.aaonri.app.databinding.FragmentUpdateAndDeleteBottomBinding
import com.aaonri.app.ui.dashboard.fragment.advertise.AdvertiseScreenActivity
import com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement.AdvertisementDetailsFragmentArgs
import com.aaonri.app.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UpdateAndDeleteBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentUpdateAndDeleteBottomBinding? = null
    val args: AdvertisementDetailsFragmentArgs by navArgs()
    val advertiseViewModel: AdvertiseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentUpdateAndDeleteBottomBinding.inflate(inflater, container, false)


        binding?.apply {

            cancelAdvrstBtn.setOnClickListener {
                //dismiss()
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure you want to Delete?")
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
                startActivity(intent)
            }

            updateAdvrst.setOnClickListener {
                dismiss()
                val intent = Intent(requireContext(), AdvertiseScreenActivity::class.java)
                intent.putExtra("isUpdateAdvertise", true)
                intent.putExtra("advertiseId", args.advertiseId)
                startActivity(intent)
            }

        }

        advertiseViewModel.cancelAdvertiseData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    dismiss()
                    findNavController().navigateUp()
                    //advertiseViewModel.callAdvertiseApiAfterCancel(true)
                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }

        return binding?.root


    }
}