package com.aaonri.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.data.authentication.register.adapter.CommunityItemAdapter
import com.aaonri.app.databinding.FragmentCommunityBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommunityBottomFragment : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    var communityBottomBinding: FragmentCommunityBottomBinding? = null
    private val customCommunityItemAdapter = CommunityItemAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        communityBottomBinding = FragmentCommunityBottomBinding.inflate(inflater, container, false)

        communityBottomBinding?.apply {
            closeCommunityBts.setOnClickListener {
                dismiss()
            }
            customCommunityItemAdapter.setData(
                data = listOf("assad", "asdasd", "Asdasd", "Asdad", "frfe")
            )
            rv.layoutManager = GridLayoutManager(view?.context, 3)
            rv.adapter = customCommunityItemAdapter
        }

        return communityBottomBinding?.root
    }
}