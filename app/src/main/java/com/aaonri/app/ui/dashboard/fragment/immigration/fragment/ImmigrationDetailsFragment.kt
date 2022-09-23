package com.aaonri.app.ui.dashboard.fragment.immigration.fragment

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.immigration.model.Discussion
import com.aaonri.app.data.immigration.model.ReplyDiscussionRequest
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentImmigrationDetailsFrgamentBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import com.aaonri.app.utils.SystemServiceUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ImmigrationDetailsFragment : Fragment() {
    var binding: FragmentImmigrationDetailsFrgamentBinding? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigrationAdapter: ImmigrationAdapter? = null
    val args: ImmigrationDetailsFragmentArgs by navArgs()
    var discussion: Discussion? = null
    var callAllImmigrationApi = false

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            FragmentImmigrationDetailsFrgamentBinding.inflate(layoutInflater, container, false)


        val isUserLogin =
            context?.let { PreferenceManager<Boolean>(it)[Constant.IS_USER_LOGIN, false] }

        immigrationAdapter = ImmigrationAdapter()

        val userId =
            context?.let { PreferenceManager<Int>(it)[Constant.USER_ID, 0] }

        binding?.apply {

            if (isUserLogin == false) {
                binding?.postReplyEt?.isFocusable = false
                binding?.postReplyEt?.isEnabled = false
                binding?.postReplyEt?.isCursorVisible = false
                binding?.postReplyEt?.keyListener = null
                binding?.postReplyEtLl?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightGrey))
                binding?.postReplyBtn?.isEnabled = false
            }

            immigrationAdapter?.itemClickListener =
                { view, item, position, updateImmigration, deleteImmigration ->
                    postReplyEt.requestFocus()
                    val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(postReplyEt, InputMethodManager.SHOW_IMPLICIT)

                }
            navigateBack.setOnClickListener {
                findNavController().navigateUp()
            }

            postReplyBtn.setOnClickListener {
                if (postReplyEt.text.toString().length >= 3) {
                    immigrationViewModel.replyDiscussion(
                        ReplyDiscussionRequest(
                            createdByUserId = userId ?: 0,
                            discussionId = if (discussion?.discussionId != null) discussion?.discussionId.toString() else "",
                            id = 0,
                            parentId = 0,
                            replyDesc = postReplyEt.text.toString(),
                        )
                    )
                    callAllImmigrationApi = true
                    postReplyEt.setText("")
                    SystemServiceUtil.closeKeyboard(requireActivity(), requireView())
                } else {
                    activity?.let { it1 ->
                        Snackbar.make(
                            it1.findViewById(android.R.id.content),
                            "Please enter valid reply", Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }

            allReplyRv.layoutManager = LinearLayoutManager(context)
            allReplyRv.adapter = immigrationAdapter

            immigrationViewModel.selectedDiscussionItem.observe(viewLifecycleOwner) {
                if (it != null) {
                    discussion = it
                    if (discussion?.approved == false) {
                        binding?.postReplyEt?.isFocusable = false
                        binding?.postReplyEt?.isEnabled = false
                        binding?.postReplyEt?.isCursorVisible = false
                        binding?.postReplyEt?.keyListener = null
                        binding?.postReplyEtLl?.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.lightGrey))
                        binding?.postReplyBtn?.isEnabled = false
                    }
                    discussionTitle.text = it.discussionTopic
                    immigrationViewModel.getDiscussionDetailsById(it.discussionId.toString())
                    discussionNameTv.text = it.discussionTopic
                    postedByTv.text = "Posted by: ${it.createdBy} on ${
                        DateTimeFormatter.ofPattern("MM-dd-yyyy")
                            .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy").parse(it.createdOn))
                    }"
                    discussionDesc.text = it.discussionDesc
                    noOfReply.text = it.noOfReplies.toString()
                    discussionDetailsLl.visibility = View.VISIBLE
                    immigrationViewModel.selectedDiscussionItem.postValue(null)
                }
            }
        }

        binding?.postReplyEt?.addTextChangedListener { editable ->
            if (editable.toString().length >= 3) {
                binding?.postReplyBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.greenBtnColor))
            } else {
                binding?.postReplyBtn?.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.lightGreenBtnColor))
            }
        }

        immigrationViewModel.replyDiscussionData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    immigrationViewModel.getDiscussionDetailsById(discussion?.discussionId.toString())
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        immigrationViewModel.discussionDetailsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    binding?.noOfReply?.text = response.data?.size.toString()
                    response.data?.let { immigrationAdapter?.setData(it) }
                }
                is Resource.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                }
            }
        }

        /* requireActivity()
             .onBackPressedDispatcher
             .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                 override fun handleOnBackPressed() {
                     if (args.isFromAllDiscussionScreen) {
                         immigrationViewModel.setIsNavigateBackFromAllImmigrationDetailScreen(
                             DoNotCallImmigrationApi
                         )
                     } else {
                         immigrationViewModel.setIsNavigateBackFromMyImmigrationDetailScreen(
                             DoNotCallImmigrationApi
                         )
                     }
                     findNavController().navigateUp()
                 }
            })*/

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        immigrationViewModel.discussionDetailsData.value = null
        immigrationViewModel.setCallImmigrationApi(callAllImmigrationApi)
        binding = null
    }

}