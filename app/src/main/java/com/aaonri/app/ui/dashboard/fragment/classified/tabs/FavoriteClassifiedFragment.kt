package com.aaonri.app.ui.dashboard.fragment.classified.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.UserAds
import com.aaonri.app.data.classified.viewmodel.ClassifiedViewModel
import com.aaonri.app.data.classified.viewmodel.PostClassifiedViewModel
import com.aaonri.app.databinding.FragmentFavoriteClassifiedBinding
import com.aaonri.app.databinding.FragmentMyClassifiedBinding
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.AllClassifiedAdapter
import com.aaonri.app.ui.dashboard.fragment.classified.adapter.FavoriteClassifiedAdapter
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.GridSpacingItemDecoration
import com.aaonri.app.utils.PreferenceManager
import com.aaonri.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteClassifiedFragment : Fragment() {
    var favoriteClassifiedBinding: FragmentFavoriteClassifiedBinding? = null
    var favoriteClassifiedAdapter: FavoriteClassifiedAdapter? = null
    val classifiedViewModel: ClassifiedViewModel by viewModels()
    val postClassifiedViewModel: PostClassifiedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoriteClassifiedBinding =
            FragmentFavoriteClassifiedBinding.inflate(inflater, container, false)

        favoriteClassifiedAdapter = FavoriteClassifiedAdapter {
            // postClassifiedViewModel.setSendDataToClassifiedDetailsScreen(it)
            // postClassifiedViewModel.setNavigateToClassifiedDetailsScreen(true)
        }

        favoriteClassifiedBinding?.apply {

            loginBtn.setOnClickListener {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            }

            recyclerViewClassified.layoutManager = GridLayoutManager(context, 2)
            recyclerViewClassified.addItemDecoration(GridSpacingItemDecoration(2, 40, 40))
        }


        classifiedViewModel.favoriteClassifiedData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    favoriteClassifiedBinding?.progressBar?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    favoriteClassifiedBinding?.progressBar?.visibility = View.GONE

                    response.data?.classifieds?.let { favoriteClassifiedAdapter!!.setData(it) }

                    favoriteClassifiedBinding?.recyclerViewClassified?.adapter =
                        favoriteClassifiedAdapter
                }
                is Resource.Error -> {
                    favoriteClassifiedBinding?.progressBar?.visibility = View.GONE
                    Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }

        return favoriteClassifiedBinding?.root
    }

    override fun onResume() {
        super.onResume()
        val email = context?.let { PreferenceManager<String>(it)[Constant.USER_EMAIL, ""] }
        if (email != null) {
            classifiedViewModel.getFavoriteClassified(email)
        }
    }
}