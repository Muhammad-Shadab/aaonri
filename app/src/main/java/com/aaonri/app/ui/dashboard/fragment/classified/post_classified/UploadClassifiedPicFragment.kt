package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentUploadClassifiedPicBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadClassifiedPicFragment : Fragment() {
    var uploadClassifiedBinding: FragmentUploadClassifiedPicBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    val listOfImagesUri = mutableListOf<Uri>()
    var image1 = true
    var image2 = true
    var image3 = true
    var image4 = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uploadClassifiedBinding =
            FragmentUploadClassifiedPicBinding.inflate(inflater, container, false)

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.UPLOAD_PIC_SCREEN)

        uploadClassifiedBinding?.apply {

            uploadPicBtn.setOnClickListener {
                if (listOfImagesUri.size < 4) {
                    ImagePicker.with(activity!!)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                } else {

                }
            }

            deleteImage1.setOnClickListener {
                deleteImage(index = 0)
            }
            deleteImage2.setOnClickListener {
                deleteImage(index = 1)
            }
            deleteImage3.setOnClickListener {
                deleteImage(index = 2)
            }
            deleteImage4.setOnClickListener {
                deleteImage(index = 3)
            }



            uploadedImage1.setOnClickListener {
                listOfImagesUri.forEachIndexed { index, _ ->
                    if (index == 0) changeCardViewBg(index)
                }
            }
            uploadedImage2.setOnClickListener {
                listOfImagesUri.forEachIndexed { index, uri ->
                    if (index == 1) changeCardViewBg(index)
                }
            }
            uploadedImage3.setOnClickListener {
                listOfImagesUri.forEachIndexed { index, uri ->
                    if (index == 2) changeCardViewBg(index)
                }
            }
            uploadedImage4.setOnClickListener {
                listOfImagesUri.forEachIndexed { index, uri ->
                    if (index == 3) changeCardViewBg(index)
                }
            }


            /*uploadedImage1.setOnClickListener {
                if (listOfImagesUri.size == 1){
                    selectedImage.load(listOfImagesUri[0])
                }
            }
            uploadedImage2.setOnClickListener {
                if (listOfImagesUri.size == 2){
                    selectedImage.load(listOfImagesUri[1])
                }
            }
            uploadedImage3.setOnClickListener {
                if (listOfImagesUri.size == 3){
                    selectedImage.load(listOfImagesUri[2])
                }
            }
            uploadedImage4.setOnClickListener {
                if (listOfImagesUri.size == 4){
                    selectedImage.load(listOfImagesUri[3])
                }
            }*/
            classifiedUploadPicNextBtn.setOnClickListener {
                findNavController().navigate(R.id.action_uploadClassifiedPicFragment_to_addressDetailsClassifiedFragment)
            }
        }

        return uploadClassifiedBinding?.root
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!

                uploadClassifiedBinding?.selectedImage?.setImageURI(fileUri)

                listOfImagesUri.add(fileUri)

                setImage()

                if (listOfImagesUri.size < 4) {
                    uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.ic_add_image_icon
                        )
                    })
                } else {
                    uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.ic_disabled_add_btn
                        )
                    })
                }

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {

            }
        }

    private fun setImage() {
        if (image1 && listOfImagesUri[0].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage1?.setImageURI(listOfImagesUri[0])
            image1 = false
            uploadClassifiedBinding?.deleteImage1?.visibility = View.VISIBLE
        } else if (image2 && listOfImagesUri[1].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage2?.setImageURI(listOfImagesUri[1])
            image2 = false
            uploadClassifiedBinding?.deleteImage2?.visibility = View.VISIBLE
        } else if (image3 && listOfImagesUri[2].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage3?.setImageURI(listOfImagesUri[2])
            image3 = false
            uploadClassifiedBinding?.deleteImage3?.visibility = View.VISIBLE
        } else if (image4 && listOfImagesUri[3].toString().isNotEmpty()) {
            uploadClassifiedBinding?.uploadedImage4?.setImageURI(listOfImagesUri[3])
            image4 = false
            uploadClassifiedBinding?.deleteImage4?.visibility = View.VISIBLE
        }

    }

    private fun deleteImage(index: Int) {
        if (listOfImagesUri[index].toString().isNotEmpty() && index == 0) {
            uploadClassifiedBinding?.uploadedImage1?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage1?.visibility = View.GONE
            listOfImagesUri.removeAt(index)
            image1 = true
        } else if (listOfImagesUri[index].toString().isNotEmpty() && index == 1) {
            uploadClassifiedBinding?.uploadedImage2?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage2?.visibility = View.GONE
            listOfImagesUri.removeAt(index)
            image2 = true
        } else if (listOfImagesUri[index].toString().isNotEmpty() && index == 2) {
            uploadClassifiedBinding?.uploadedImage3?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage3?.visibility = View.GONE
            listOfImagesUri.removeAt(index)
            image3 = true
        } else if (listOfImagesUri[index].toString().isNotEmpty() && index == 3) {
            uploadClassifiedBinding?.uploadedImage4?.load(R.drawable.ic_uplaoded_image)
            uploadClassifiedBinding?.deleteImage4?.visibility = View.GONE
            listOfImagesUri.removeAt(index)
            image4 = true
        }

        if (listOfImagesUri.size < 4) {
            uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_add_image_icon
                )
            })
        } else {
            uploadClassifiedBinding?.uploadPicBtn?.setImageDrawable(context?.let { it1 ->
                ContextCompat.getDrawable(
                    it1, R.drawable.ic_disabled_add_btn
                )
            })
        }
    }

    private fun changeCardViewBg(selectedImageIndex: Int) {

        if (selectedImageIndex == 0) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 1) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 2) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        } else if (selectedImageIndex == 3) {
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage1?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage2?.setBackgroundColor(
                    it2
                )
            }

            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.white
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage3?.setBackgroundColor(
                    it2
                )
            }
            context?.let { it1 ->
                ContextCompat.getColor(
                    it1,
                    R.color.selectedClassifiedCardViewBorder
                )
            }?.let { it2 ->
                uploadClassifiedBinding?.uploadedImage4?.setBackgroundColor(
                    it2
                )
            }
        }
    }
}