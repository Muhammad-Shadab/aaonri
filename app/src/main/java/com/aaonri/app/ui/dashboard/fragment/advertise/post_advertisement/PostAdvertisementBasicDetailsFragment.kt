package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentPostAdvertisementbasicDetailsBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class PostAdvertisementBasicDetailsFragment : Fragment() {
    var advertiseBinding: FragmentPostAdvertisementbasicDetailsBinding? = null
    var templateImage = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        advertiseBinding =
            FragmentPostAdvertisementbasicDetailsBinding.inflate(inflater, container, false)

        advertiseBinding?.apply {

            chooseTemplatell.setOnClickListener {
                if (profile.isEmpty()) {
                    ImagePicker.with(requireActivity())
                        .compress(1024)
                        .crop()
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                            progressBarBasicDetails.visibility = View.VISIBLE
                        }
                } else {
                    val materialAlertDialogBuilder =
                        context?.let { it1 -> MaterialAlertDialogBuilder(it1) }
                    materialAlertDialogBuilder?.setTitle("Profile Photo")
                        ?.setMessage("Change profile photo? ")
                        ?.setPositiveButton("CHANGE") { dialog, _ ->
                            ImagePicker.with(requireActivity())
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .crop()
                                .createIntent { intent ->
                                    progressBarBasicDetails.visibility = View.VISIBLE
                                    startForProfileImageResult.launch(intent)
                                }
                            dialog.dismiss()
                        }
                        ?.setNegativeButton("REMOVE") { dialog, _ ->
                            dialog.dismiss()
                        }
                        ?.show()
                }
            }

            advertiseDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)
            }

            previewAdvertiseBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_reviewAdvertiseFragment)

            }

        }
        return advertiseBinding?.root
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                val fileUri = data?.data!!

                templateImage = fileUri.toString()
                advertiseBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
                //setImage()
                //basicDetailsBinding?.addProfileIv?.setImageURI(fileUri)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                advertiseBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
            }
        }

    /*private fun setImage() {
        if (profile.isNotEmpty()) {
            advertiseBinding?.addProfileIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(profile)
                        .circleCrop()
                        .into(it)
                }
            }
        } else {

        }
    }*/

}