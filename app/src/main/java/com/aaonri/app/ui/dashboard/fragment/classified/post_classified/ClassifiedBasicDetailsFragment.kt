package com.aaonri.app.ui.dashboard.fragment.classified.post_classified

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.classified.ClassifiedConstant
import com.aaonri.app.data.classified.adapter.ClassifiedCategoryArrayAdapter
import com.aaonri.app.data.classified.adapter.ClassifiedSubCategoryArrayAdapter
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem
import com.aaonri.app.data.classified.model.ClassifiedSubcategoryX
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentClassifiedBasicDetailsBinding
import com.aaonri.app.utils.DecimalDigitsInputFilter
import com.aaonri.app.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ClassifiedBasicDetailsFragment : Fragment() {
    var classifiedDetailsBinding: FragmentClassifiedBasicDetailsBinding? = null
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        classifiedDetailsBinding =
            FragmentClassifiedBasicDetailsBinding.inflate(inflater, container, false)

        postClassifiedViewModel.getClassifiedCategory()

        postClassifiedViewModel.addNavigationForStepper(ClassifiedConstant.BASIC_DETAILS_SCREEN)

        classifiedDetailsBinding?.apply {

            priceClassifiedEt.stickPrefix("$")

            priceClassifiedEt.filters = arrayOf(DecimalDigitsInputFilter(2))

            classifiedDetailsNextBtn.setOnClickListener {

                var price = ""

                if (priceClassifiedEt.text.toString().contains("$")) {
                    price = priceClassifiedEt.text.toString().replace("$", "")
                }

                if (selectCategoryClassifiedSpinner.text.toString().isNotEmpty()){
                    if (selectSubCategoryClassifiedSpinner.text.toString().isNotEmpty()){
                        if (titleClassifiedEt.text.isNotEmpty() && titleClassifiedEt.text.trim()
                                .toString().length >= 3
                        ) {
                            if (price.isNotEmpty() && price.length < 9) {
                                if (price.toDouble() < 999999999) {
                                    if (classifiedDescEt.text.isNotEmpty()) {
                                        postClassifiedViewModel.addIsProductNewCheckBox(isProductNewCheckBox.isChecked)
                                        postClassifiedViewModel.addClassifiedBasicDetails(
                                            title = titleClassifiedEt.text.trim().toString(),
                                            price = priceClassifiedEt.text.trim().toString()
                                                .replace("$", ""),
                                            adDescription = classifiedDescEt.text.trim().toString()
                                        )
                                        findNavController().navigate(R.id.action_classifiedBasicDetailsFragment_to_uploadClassifiedPicFragment)
                                    } else {
                                        showAlert("Please enter valid classified description")
                                    }
                                } else {
                                    showAlert("Please enter valid classified price")
                                }
                            } else {
                                showAlert("Please enter valid classified price")
                            }

                        } else {
                            showAlert("Please enter valid classified title")
                        }
                    }else{
                        showAlert("Please select valid sub category")
                    }
                }else{
                    showAlert("Please select valid category")
                }
            }

            classifiedDescEt.addTextChangedListener { editable ->
                descLength.text = "${editable.toString().length}/2000"
            }
            selectCategoryClassifiedSpinner.setOnClickListener {
                val action =
                    ClassifiedBasicDetailsFragmentDirections.actionClassifiedBasicDetailsFragmentToSelectClassifiedCategoryBottom()
                findNavController().navigate(action)
            }

            selectSubCategoryClassifiedSpinner.setOnClickListener {
                val action =
                    ClassifiedBasicDetailsFragmentDirections.actionClassifiedBasicDetailsFragmentToSelectClassifiedSubCategoryBottom()
                findNavController().navigate(action)
            }

        }

        postClassifiedViewModel.selectedClassifiedCategory.observe(viewLifecycleOwner) {
            classifiedDetailsBinding?.selectCategoryClassifiedSpinner?.text = it.title
            classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.text = ""
        }

        postClassifiedViewModel.selectedSubClassifiedCategory.observe(viewLifecycleOwner) {
            classifiedDetailsBinding?.selectSubCategoryClassifiedSpinner?.text = it.title
        }

        return classifiedDetailsBinding?.root
    }

    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun EditText.stickPrefix(prefix: String) {
        this.addTextChangedListener(afterTextChanged = {
            if (!it.toString().startsWith(prefix) && it?.isNotEmpty() == true) {
                this.setText(prefix + this.text)
                this.setSelection(this.length())
            }
        })
    }
}

