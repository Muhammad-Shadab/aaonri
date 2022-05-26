package com.aaonri.app.ui.authentication.register.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.data.authentication.register.adapter.ServicesItemAdapter
import com.aaonri.app.data.authentication.register.model.listOfService
import com.aaonri.app.databinding.FragmentServicesCategoryBinding

class ServicesCategoryFragment : Fragment() {
    var servicesGridItemBinding: FragmentServicesCategoryBinding? = null
    private var adapter: ServicesItemAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        servicesGridItemBinding =
            FragmentServicesCategoryBinding.inflate(inflater, container, false)

        adapter = ServicesItemAdapter { showLayout ->
            if (showLayout) {
                servicesGridItemBinding?.visibilityCardView?.visibility = View.VISIBLE
            } else {
                servicesGridItemBinding?.visibilityCardView?.visibility = View.GONE
            }
        }

        adapter?.setData(data = listOfService)
        servicesGridItemBinding?.apply {
            servicesGridRecyclerView.adapter = adapter
            servicesGridRecyclerView.layoutManager = GridLayoutManager(context, 3)
        }

        return servicesGridItemBinding?.root
    }
}