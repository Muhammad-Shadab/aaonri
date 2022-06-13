package com.aaonri.app.ui.dashboard.fragment.classified.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.databinding.FragmentAllClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.utils.GridSpacingItemDecoration


class AllClassifiedFragment : Fragment() {
    var allClassifiedBinding: FragmentAllClassifiedBinding? = null
    var allClassifiedAdapter: AllClassifiedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allClassifiedBinding =
            FragmentAllClassifiedBinding.inflate(inflater, container, false)

        allClassifiedAdapter = AllClassifiedAdapter()

        allClassifiedBinding?.apply {

            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 40, 40))
            recyclerViewClassified.adapter = allClassifiedAdapter

        }

        return allClassifiedBinding?.root
    }
}

