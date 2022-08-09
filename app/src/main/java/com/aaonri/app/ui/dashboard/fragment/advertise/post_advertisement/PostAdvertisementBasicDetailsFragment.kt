package com.aaonri.app.ui.dashboard.fragment.advertise.post_advertisement

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.advertise.AdvertiseConstant
import com.aaonri.app.data.advertise.viewmodel.PostAdvertiseViewModel
import com.aaonri.app.databinding.FragmentPostAdvertisementbasicDetailsBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostAdvertisementBasicDetailsFragment : Fragment() {
    var advertiseBinding: FragmentPostAdvertisementbasicDetailsBinding? = null
    val postAdvertiseViewModel: PostAdvertiseViewModel by activityViewModels()
    var templateImage = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        advertiseBinding =
            FragmentPostAdvertisementbasicDetailsBinding.inflate(inflater, container, false)

        advertiseBinding?.apply {

            postAdvertiseViewModel.setNavigationForStepper(AdvertiseConstant.ADVERTISE_BASIC_DETAILS)

            selectedTemplateTv.text = postAdvertiseViewModel.selectTemplateName
            chooseTemplateTv.text = postAdvertiseViewModel.selectTemplateLocation

            chooseTemplatell.setOnClickListener {
                if (templateImage.isEmpty()) {
                    ImagePicker.with(requireActivity())
                        .compress(1024)
                        .crop()
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                            progressBarBasicDetails.visibility = View.VISIBLE
                        }
                } /*else {
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
                }*/
            }

            selectAdvertiseDaysSpinner.setOnClickListener {

            }

            advertiseDetailsNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_postAdvertiseCheckout)
            }

            previewAdvertiseBtn.setOnClickListener {

                saveDataToViewModel()

                findNavController().navigate(R.id.action_postAdvertisementbasicDetailsFragment_to_reviewAdvertiseFragment)
            }

        }
        return advertiseBinding?.root
    }

    private fun saveDataToViewModel() {
        postAdvertiseViewModel
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                val fileUri = data?.data!!

                templateImage = fileUri.toString()
                advertiseBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
                setImage()

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                advertiseBinding?.progressBarBasicDetails?.visibility = View.INVISIBLE
            }
        }

    private fun setImage() {
        if (templateImage.isNotEmpty()) {
            advertiseBinding?.advertiseIv?.visibility = View.VISIBLE
            advertiseBinding?.uploadImageTv?.visibility = View.GONE
            advertiseBinding?.sizeLimitTv?.visibility = View.GONE

            advertiseBinding?.advertiseIv?.let {
                context?.let { it1 ->
                    Glide.with(it1)
                        .load(templateImage)
                        .into(it)
                }
            }
        } else {
            advertiseBinding?.advertiseIv?.visibility = View.GONE
            advertiseBinding?.uploadImageTv?.visibility = View.VISIBLE
            advertiseBinding?.sizeLimitTv?.visibility = View.VISIBLE
        }
    }

}