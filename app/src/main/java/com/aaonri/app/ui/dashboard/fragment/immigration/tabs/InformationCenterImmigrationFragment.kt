package com.aaonri.app.ui.dashboard.fragment.immigration.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaonri.app.data.immigration.model.ImmigrationCenterModelItem
import com.aaonri.app.data.immigration.viewmodel.ImmigrationViewModel
import com.aaonri.app.databinding.FragmentInformationCenterImmigrationBinding
import com.aaonri.app.ui.dashboard.fragment.immigration.adapter.ImmigrationAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

@AndroidEntryPoint
class InformationCenterImmigrationFragment : Fragment() {
    var binding: FragmentInformationCenterImmigrationBinding? = null
    var immigrationAdapter: ImmigrationAdapter? = null
    val immigrationViewModel: ImmigrationViewModel by activityViewModels()
    var immigartinList = mutableListOf<ImmigrationCenterModelItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInformationCenterImmigrationBinding.inflate(inflater, container, false)

        immigrationAdapter = ImmigrationAdapter()
        immigrationAdapter?.itemClickListener =
            { view, item, position, updateImmigration, deleteImmigration ->
                if (item is ImmigrationCenterModelItem) {
                    immigrationViewModel.setNavigateFromImmigrationCenterToCenterDetailScreen(true)
                    immigrationViewModel.setSelectedImmigrationCenterItem(item)
                }
            }
        binding?.apply {

            immigrationcenterRv.layoutManager = LinearLayoutManager(context)
            immigrationcenterRv.adapter = immigrationAdapter


            val userArray = JSONObject(loadJSONFromAsset()).getJSONArray("immigrationcenterlist")
            val gson = Gson()

            for (i in 0 until userArray.length()) {
                if(!immigartinList.contains( gson.fromJson(
                        userArray.getString(i),
                        ImmigrationCenterModelItem::class.java
                    ))) {
                    immigartinList.add(

                        gson.fromJson(
                            userArray.getString(i),
                            ImmigrationCenterModelItem::class.java
                        )
                    )
                }

            }

            immigrationAdapter?.setData(immigartinList)


        }
        return binding?.root
    }

    private fun loadJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream = context?.assets?.open("informationcenter.json")
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


}