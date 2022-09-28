package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.AdvertiseActivePageResponseItem
import com.aaonri.app.databinding.AdvertiseTemplateItemBinding
import com.aaonri.app.utils.PreferenceManager

class AdvertiseTemplateAdapter(private var selectedServices: ((value: AdvertiseActivePageResponseItem) -> Unit)) :
    RecyclerView.Adapter<AdvertiseTemplateAdapter.AdvertiseViewHolder>() {

    private var advertisePageList = listOf<AdvertiseActivePageResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertiseTemplateItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.apply {

            holder.itemView.setOnClickListener {
                context?.let { it1 -> PreferenceManager<Int>(it1) }
                    ?.set("selectedTemplatePage", position)
                notifyDataSetChanged()
            }

            /*  Glide.with(context)
                  .load("${BuildConfig.BASE_URL}/assets/img/advertisingpage/${advertisePageList[position].imageName}")
                  .into(websiteTemplateIv)*/

            /*Glide.with(context)
                .load("${BuildConfig.BASE_URL}/assets/img/advertisingpage/${advertisePageList[position].imageName}")
                .into(mobileTemplateImageView)*/

            if (context?.let { PreferenceManager<Int>(it)["selectedTemplatePage", -1] } == position) {
                websiteTemplateSuccessTick.visibility = View.VISIBLE
                mobileTemplateSuccessTick.visibility = View.VISIBLE
                context.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.selectedAdvertiseTemplateStroke
                    )
                }.let { it2 ->
                    websiteTemplateCv.strokeColor = it2
                }
                context.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.selectedAdvertiseTemplateStroke
                    )
                }.let { it2 ->
                    mobileTemplateCv.strokeColor = it2
                }
                selectedServices(advertisePageList[position])
            } else {
                websiteTemplateSuccessTick.visibility = View.GONE
                mobileTemplateSuccessTick.visibility = View.GONE
                context.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }.let { it2 ->
                    websiteTemplateCv.strokeColor = it2
                }
                context.let { it1 ->
                    ContextCompat.getColor(
                        it1,
                        R.color.white
                    )
                }.let { it2 ->
                    mobileTemplateCv.strokeColor = it2
                }
            }

            when (advertisePageList[position].pageCode) {
                "HM" -> {
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.home_page_ads
                        )
                    )

                    websiteTemplateIv.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.web_home_page_ads
                        )
                    )


                }
                "DB" -> {
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.home_page_ads
                        )
                    )

                    websiteTemplateIv.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.web_dashboard_landing_page
                        )
                    )
                }
                "LP" -> {
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_classified_top_listing
                        )
                    )
                    websiteTemplateIv.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.web_ads_landing_page
                        )
                    )
                }
                "PD" -> {
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                    websiteTemplateIv.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.web_details_page_ads
                        )
                    )
                }
            }
        }
    }

    @JvmName("setData1")
    fun setData(advertisePageList: List<AdvertiseActivePageResponseItem>) {
        this.advertisePageList = advertisePageList
        notifyDataSetChanged()
    }

    override fun getItemCount() = advertisePageList.size

    inner class AdvertiseViewHolder(val binding: AdvertiseTemplateItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}



