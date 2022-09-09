package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.ImmigrationFilterModel
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationFilterBinding
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.material.snackbar.Snackbar

class ImmigrationFilterFragment : Fragment() {
    var binding: FragmentImmigrationFilterBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()

    var startDate = ""
    var endDate = ""
    var selectedDate = ""
    var isActiveDiscussionSelected = false
    var isAtLeastOneDiscussionSelected = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImmigrationFilterBinding.inflate(layoutInflater, container, false)

        binding?.apply {

            /*selectCategorySpinner.setOnClickListener {
                val action =
                    ImmigrationFilterFragmentDirections.actionImmigrationFilterFragmentToImmigrationCategoryBottomSheet(
                        "FromFilterScreen"
                    )
                findNavController().navigate(action)
            }*/

            closeBtn.setOnClickListener {
                activity?.onBackPressed()
            }


            applyBtn.setOnClickListener {
                immigrationViewModel.setFilterData(
                    ImmigrationFilterModel(
                        fifteenDaysSelected = fifteenDaysRadioBtn.isChecked,
                        threeMonthSelected = threeMonthRadioBtn.isChecked,
                        oneYearSelected = oneYearRadioBtn.isChecked,
                        activeDiscussion = isActiveDiscussionSelected,
                        atLeastOnDiscussion = isAtLeastOneDiscussionSelected
                    )
                )
                activity?.onBackPressed()
            }

            clearAllBtn.setOnClickListener {
                fifteenDaysRadioBtn.isChecked = false
                threeMonthRadioBtn.isChecked = false
                oneYearRadioBtn.isChecked = false
                isActiveDiscussionSelected = false
                isAtLeastOneDiscussionSelected = false

                /*context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }?.let { it2 ->
                    activeDiscussion.setBackgroundColor(
                        it2
                    )
                }
                context?.getColor(R.color.black)
                    ?.let { it1 -> activeDiscussion.setTextColor(it1) }*/


                context?.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }?.let { it2 ->
                    atLeastOneResponse.setBackgroundColor(
                        it2
                    )
                }
                context?.getColor(R.color.black)
                    ?.let { it1 -> atLeastOneResponse.setTextColor(it1) }

            }

            activeDiscussion.setOnClickListener {
                /*isActiveDiscussionSelected = !isActiveDiscussionSelected
                if (isActiveDiscussionSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        activeDiscussion.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> activeDiscussion.setTextColor(it1) }
                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        activeDiscussion.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> activeDiscussion.setTextColor(it1) }
                }*/
            }

            atLeastOneResponse.setOnClickListener {
                isAtLeastOneDiscussionSelected = !isAtLeastOneDiscussionSelected
                if (isAtLeastOneDiscussionSelected) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        atLeastOneResponse.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> atLeastOneResponse.setTextColor(it1) }
                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        atLeastOneResponse.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> atLeastOneResponse.setTextColor(it1) }
                }
            }

            immigrationViewModel.immigrationFilterData.observe(viewLifecycleOwner) {
                isActiveDiscussionSelected = it.activeDiscussion
                isAtLeastOneDiscussionSelected = it.atLeastOnDiscussion

                fifteenDaysRadioBtn.isChecked = it.fifteenDaysSelected
                threeMonthRadioBtn.isChecked = it.threeMonthSelected
                oneYearRadioBtn.isChecked = it.oneYearSelected

                /*if (it.activeDiscussion) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        activeDiscussion.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> activeDiscussion.setTextColor(it1) }
                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        activeDiscussion.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> activeDiscussion.setTextColor(it1) }
                }*/
                if (it.atLeastOnDiscussion) {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.blueBtnColor
                        )
                    }?.let { it2 ->
                        atLeastOneResponse.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.white)
                        ?.let { it1 -> atLeastOneResponse.setTextColor(it1) }
                } else {
                    context?.let { it1 ->
                        ContextCompat.getColor(
                            it1,
                            R.color.white
                        )
                    }?.let { it2 ->
                        atLeastOneResponse.setBackgroundColor(
                            it2
                        )
                    }
                    context?.getColor(R.color.black)
                        ?.let { it1 -> atLeastOneResponse.setTextColor(it1) }
                }
            }

            fifteenDaysRadioBtn.setOnCheckedChangeListener { p0, p1 ->
                activity?.let { view?.let { it1 -> SystemServiceUtil.closeKeyboard(it, it1) } }
                if (p1) {
                    threeMonthRadioBtn.isChecked = false
                    oneYearRadioBtn.isChecked = false
                    fifteenDaysTv.setTextColor(Color.parseColor("#333333"))
                } else {
                    fifteenDaysTv.setTextColor(Color.parseColor("#979797"))
                }
            }

            threeMonthRadioBtn.setOnCheckedChangeListener { p0, p1 ->
                activity?.let { view?.let { it1 -> SystemServiceUtil.closeKeyboard(it, it1) } }
                if (p1) {
                    fifteenDaysRadioBtn.isChecked = false
                    oneYearRadioBtn.isChecked = false
                    threeMonthTv.setTextColor(Color.parseColor("#333333"))
                } else {
                    threeMonthTv.setTextColor(Color.parseColor("#979797"))
                }
            }

            oneYearRadioBtn.setOnCheckedChangeListener { p0, p1 ->
                activity?.let { view?.let { it1 -> SystemServiceUtil.closeKeyboard(it, it1) } }
                if (p1) {
                    fifteenDaysRadioBtn.isChecked = false
                    threeMonthRadioBtn.isChecked = false
                    oneYearTv.setTextColor(Color.parseColor("#333333"))
                } else {
                    oneYearTv.setTextColor(Color.parseColor("#979797"))
                }
            }
        }


        /* immigrationViewModel.selectedImmigrationFilterCategory.observe(viewLifecycleOwner) {
             binding?.selectCategorySpinner?.text = it.discCatValue
         }*/


        return binding?.root
    }


    private fun showAlert(text: String) {
        activity?.let { it1 ->
            Snackbar.make(
                it1.findViewById(android.R.id.content),
                text, Snackbar.LENGTH_LONG
            ).show()
        }
    }

}
