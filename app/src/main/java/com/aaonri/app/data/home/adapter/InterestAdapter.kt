package com.aaonri.app.data.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.home.model.InterestResponseItem
import com.aaonri.app.databinding.InterestItemBinding

class InterestAdapter(private var selectedServices: ((value: InterestResponseItem) -> Unit)) :
    RecyclerView.Adapter<InterestAdapter.InterestViewHolder>() {

    private var data = listOf<InterestResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = InterestItemBinding.inflate(inflater, parent, false)
        return InterestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            interestText.text = data[position].interestDesc

            when (data[position].interestDesc) {
                "Classifieds" -> {
                    interestIcon.load(R.drawable.ic_classified)
                    interestIcon.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.blueBtnColor
                        )
                    )
                }
                "Events" -> {
                    interestIcon.load(R.drawable.ic_event)
                }
                "Jobs" -> {
                    interestIcon.load(R.drawable.ic_job)
                }
                "Immigration" -> {
                    interestIcon.load(R.drawable.ic_immigration)
                }
                "Astrology" -> {
                    interestIcon.load(R.drawable.ic_astrology)
                }
                "Sports" -> {
                    interestIcon.load(R.drawable.ic_sports)
                }
                "Community Connect" -> {
                    interestIcon.load(R.drawable.ic_community)
                }
                "Foundation & Donations" -> {
                    interestIcon.load(R.drawable.ic_donation)
                }
                "Student Services" -> {
                    interestIcon.load(R.drawable.ic_education)
                }
                "Legal Services" -> {
                    interestIcon.load(R.drawable.ic_legal_service)
                }
                "Matrimony & Weddings" -> {
                    interestIcon.load(R.drawable.ic_matrimony)
                }
                "Medical Care" -> {
                    interestIcon.load(R.drawable.ic_medical)
                }
                "Real Estate" -> {
                    interestIcon.load(R.drawable.ic_real_state)
                }
                "Shop With Us" -> {
                    interestIcon.load(R.drawable.ic_shop)
                    interestIcon.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.blueBtnColor
                        )
                    )
                }
                "Travel and Stay" -> {
                    interestIcon.load(R.drawable.ic_travel)
                }
                "Home Needs" -> {
                    interestIcon.load(R.drawable.ic_home_need)
                }
                "Business Needs" -> {
                    interestIcon.load(R.drawable.ic_business_need)
                }
                "Advertise With Us" -> {
                    interestIcon.load(R.drawable.ic_advertise)
                    interestIcon.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.blueBtnColor
                        )
                    )
                }
            }


        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    @JvmName("setData1")
    fun setData(data: List<InterestResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class InterestViewHolder(val binding: InterestItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}