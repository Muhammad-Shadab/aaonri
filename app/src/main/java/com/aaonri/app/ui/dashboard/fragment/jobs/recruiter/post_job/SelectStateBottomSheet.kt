package com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.post_job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.jobs.recruiter.model.StateListResponseItem
import com.aaonri.app.data.jobs.recruiter.viewmodel.JobRecruiterViewModel
import com.aaonri.app.databinding.FragmentJobPostDetailsScreenBottomSheetBinding
import com.aaonri.app.ui.dashboard.fragment.jobs.recruiter.adapter.StateAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

@AndroidEntryPoint
class SelectStateBottomSheet : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var binding: FragmentJobPostDetailsScreenBottomSheetBinding? = null
    val jobRecruiterViewModel: JobRecruiterViewModel by activityViewModels()
    var stateList = mutableListOf<StateListResponseItem>()
    var stateAdapter: StateAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        isCancelable = false

        binding = FragmentJobPostDetailsScreenBottomSheetBinding.inflate(
            layoutInflater,
            container,
            false
        )

        stateAdapter = StateAdapter {
            jobRecruiterViewModel.setUserSelectedState(it.name)
            dismiss()
        }

        binding?.apply {

            stateRv.layoutManager = LinearLayoutManager(context)
            stateRv.adapter = stateAdapter

            closeBtn.setOnClickListener {
                dismiss()
            }

            val userArray = JSONObject(loadJSONFromAsset()).getJSONArray("StateList")
            val gson = Gson()

            for (i in 0 until userArray.length()) {
                if (!stateList.contains(
                        gson.fromJson(
                            userArray.getString(i),
                            StateListResponseItem::class.java
                        )
                    )
                ) {
                    stateList.add(
                        gson.fromJson(
                            userArray.getString(i),
                            StateListResponseItem::class.java
                        )
                    )
                }
            }

            stateAdapter?.setData(stateList)


        }

        return binding?.root
    }

    private fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream = context?.assets?.open("usa_states.json")
            val size = inputStream?.available()
            val buffer = size?.let { ByteArray(it) }
            val charset: Charset = Charsets.UTF_8
            inputStream?.read(buffer)
            inputStream?.close()
            json = buffer?.let { String(it, charset) }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}