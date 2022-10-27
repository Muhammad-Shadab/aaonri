package com.aaonri.app.ui.dashboard.fragment.report_user

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentReportUserBinding
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportUserFragment : Fragment() {
    var binding: FragmentReportUserBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    val args: ReportUserFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReportUserBinding.inflate(layoutInflater, container, false)

        val blockedUsersId =
            context?.let { PreferenceManager<String>(it)[Constant.BLOCKED_USER_ID, ""] }

        val reportUserDialog = Dialog(requireContext())
        reportUserDialog.setContentView(R.layout.report_user_dialog)
        reportUserDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dialog_shape
            )
        )

        reportUserDialog.setCancelable(false)
        val window: Window? = reportUserDialog.window
        val wlp: WindowManager.LayoutParams? = window?.attributes

        wlp?.gravity = Gravity.BOTTOM
        window?.attributes = wlp

        val blockUserTv =
            reportUserDialog.findViewById<TextView>(R.id.blockUserTv)
        val reportUserTv =
            reportUserDialog.findViewById<TextView>(R.id.reportUserTv)
        val cancelDialogTv =
            reportUserDialog.findViewById<TextView>(R.id.cancelDialogTv)

        blockUserTv.setOnClickListener {
            reportUserDialog.dismiss()
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure you want to block this user? You will not able to see any posts or comments from this user.")
            builder.setPositiveButton("Block") { dialog, which ->

                immigrationViewModel.getAllImmigrationDiscussion(
                    GetAllImmigrationRequest(
                        categoryId = "1",
                        createdById = "",
                        keywords = ""
                    )
                )

                val action = ReportUserFragmentDirections.actionReportUserFragmentToImmigrationScreenFragment()
                findNavController().navigate(action)

                context?.let { it1 -> PreferenceManager<String>(it1) }
                    ?.set(Constant.BLOCKED_USER_ID, if (blockedUsersId?.isNotEmpty() == true) blockedUsersId+","+args.userId.toString() else args.userId.toString()+",")
            }
            builder.setNegativeButton("Cancel") { dialog, which ->

            }
            builder.show()
        }
        reportUserTv.setOnClickListener {
            reportUserDialog.dismiss()
            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            selectorIntent.data = Uri.parse("mailto:")

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("admin@aaonri.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report User!")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I would like to report this abusive user. Please take necessary action. \n\nUser Email: ${args.email}")
            emailIntent.selector = selectorIntent

            activity?.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }
        cancelDialogTv.setOnClickListener {
            reportUserDialog.dismiss()
        }


        val nameParts = args.userName.split(" ").toTypedArray()
        val firstName = nameParts[0]
        val firstNameChar = firstName[0]
        var lastName = ""
        var lastNameChar = ""
        if (nameParts.size > 1) {
            lastName = nameParts[nameParts.size - 1]
            lastNameChar = lastName[0].toString()
        }

        binding?.apply {

            firstNameBasicDetails.text = firstName
            lastNameBasicDetails.text = lastName
            emailAddressBasicDetails.text = args.email

            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            moreBtn.setOnClickListener {
                reportUserDialog.show()
            }

            if (args.userProfile.isNotEmpty()) {
                context?.let {
                    Glide.with(it)
                        .load("${BuildConfig.BASE_URL}/api/v1/common/profileFile/${args.userProfile}")
                        .error(R.drawable.profile_pic_placeholder).into(userProfile)
                }
                userNameTv.visibility = View.GONE
                userProfile.visibility = View.VISIBLE
            } else {
                userNameTv.text = firstNameChar + lastNameChar
                userNameTv.visibility = View.VISIBLE
                userProfile.visibility = View.GONE
            }


        }


        return binding?.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}