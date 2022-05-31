package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.community.Community
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponse
import com.aaonri.app.data.authentication.register.model.countries.CountriesResponseItem
import com.aaonri.app.databinding.CountryLayoutItemBinding

class CountryAdapter(private var countryClicked: ((countryName: String, countryFlag: String) -> Unit)? = null) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private var data = listOf<CountriesResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            CountryLayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val currentFlag = data[position].countryInfo
        with(holder) {
            itemView.setOnClickListener {
                countryClicked?.let { it1 -> it1(data[position].country, currentFlag.flag) }
            }
            with(binding) {
                countryIcon.load(currentFlag.flag)
                countryTv.text = data[position].country
            }
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<CountriesResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(val binding: CountryLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}