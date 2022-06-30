package com.aaonri.app.ui.dashboard.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aaonri.app.R
import com.aaonri.app.databinding.FragmentMoreScreenBinding
import com.aaonri.app.ui.authentication.login.LoginActivity
import com.aaonri.app.utils.Constant
import com.aaonri.app.utils.PreferenceManager

class MoreScreenFragment : Fragment() {
    var moreScreenBinding: FragmentMoreScreenBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        moreScreenBinding = FragmentMoreScreenBinding.inflate(inflater, container, false)

        moreScreenBinding?.apply {
            logOutBtn.setOnClickListener {
              val builder = AlertDialog.Builder(context)
              builder.setTitle("Confirm")
              builder.setMessage("Are you sure you want to Logout")
              builder.setPositiveButton("OK") { dialog, which ->
                  context?.let { it1 -> PreferenceManager<String>(it1) }
                      ?.set(Constant.USER_EMAIL, "")
                  val intent = Intent(context, LoginActivity::class.java)
                  startActivity(intent)
                  activity?.finish()
              }
              builder.setNegativeButton("Cancel") { dialog, which ->

              }
              builder.show()
          }
        }

        return moreScreenBinding?.root
    }
}