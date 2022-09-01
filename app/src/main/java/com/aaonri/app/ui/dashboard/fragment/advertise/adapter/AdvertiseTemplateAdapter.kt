package com.aaonri.app.ui.dashboard.fragment.advertise.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.advertise.model.AdvertiseActivePageResponseItem
import com.aaonri.app.data.advertise.model.AdvertisePageLocationResponseItem
import com.aaonri.app.databinding.AdvertiseTemplateItemBinding
import com.aaonri.app.utils.PreferenceManager
import com.bumptech.glide.Glide

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

            Glide.with(context)
                .load("${BuildConfig.BASE_URL}/assets/img/advertisingpage/${advertisePageList[position].imageName}")
                .into(websiteTemplateIv)

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
                }
                "DB" -> {
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.home_page_ads
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
                }
                "PD" -> {
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
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


class AdvertiseTemplateLocationAdapter(private var selectedServices: ((value: AdvertisePageLocationResponseItem) -> Unit)) :
    RecyclerView.Adapter<AdvertiseTemplateLocationAdapter.AdvertiseViewHolder>() {

    private var advertisePageList = listOf<AdvertisePageLocationResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertiseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdvertiseTemplateItemBinding.inflate(inflater, parent, false)
        return AdvertiseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertiseViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.binding.apply {

            holder.itemView.setOnClickListener {
                context?.let { it1 -> PreferenceManager<Int>(it1) }
                    ?.set("selectedTemplateLocation", position)
                selectedServices(advertisePageList[position])
                notifyDataSetChanged()
            }

            Glide.with(context)
                .load("${BuildConfig.BASE_URL}/assets/img/advertisingpage/${advertisePageList[position].imageName}")
                .into(websiteTemplateIv)

            /*Glide.with(context)
                .load("${BuildConfig.BASE_URL}/assets/img/advertisingpage/${advertisePageList[position].imageName}")
                .into(mobileTemplateImageView)*/

            if (context?.let { PreferenceManager<Int>(it)["selectedTemplateLocation", -1] } == position) {
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

            when (advertisePageList[position].locationCode) {
                "HPCIA" -> {
                    //Classifieds Section Inline"
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_below_first_section
                        )
                    )
                }
                "HPEIA" -> {
                    //Events Section Inline
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                "HPAFI" -> {
                    //Home Page Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                "HPAFT" -> {
                    //Home Page Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                "HPLTB" -> {
                    //Home Page Long Term Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_below_first_section
                        )
                    )
                }
                "HPLTBD" -> {
                    //Home Page Long Term Bottom Down
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_below_first_section
                        )
                    )
                }
                "HPLTBU" -> {
                    //Home Page Long Term Bottom Upper
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_below_first_section
                        )
                    )
                }
                "HPTB" -> {
                    //Home Page Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_below_first_section
                        )
                    )
                }
                "HPIIA" -> {
                    //Immigration  Section Inline
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                "HPJIA" -> {
                    //Jobs Section Inline
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                //page 2
                "DBBDA" -> {
                    //Dashboard Below Discover Aaonri
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                "DBBIA" -> {
                    //Dashboard Below Interest Area
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_below_first_section
                        )
                    )
                }
                "DBAFI" -> {
                    //Dashboard Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                "DBAFT" -> {
                    //Dashboard Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_popular_items
                        )
                    )
                }
                "DBTB" -> {
                    //Dashboard Top Banner(Below Search Bar)
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_below_first_section
                        )
                    )
                }
                //page 3
                "LPCAFI" -> {
                    //Classifieds Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_bottom
                        )
                    )
                }
                "LPCAFT" -> {
                    //Classifieds Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_bottom
                        )
                    )
                }
                "LPCMS" -> {
                    //Classifieds LHS of Main Section
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_bottom
                        )
                    )
                }
                "LPCTB" -> {
                    //Classifieds Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_classified_top_listing
                        )
                    )
                }
                "LPEAFI" -> {
                    //Events Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPEAFT" -> {
                    //Events Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPEMS" -> {
                    //Events LHS of Main Section
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPETB" -> {
                    //Events Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_header_top_slider
                        )
                    )
                }
                "LPIAFI" -> {
                    //Immigration Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPIAFT" -> {
                    //Immigration Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPIMS" -> {
                    //Immigration LHS of Main Section
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPITB" -> {
                    //Immigration Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_header_top_slider
                        )
                    )
                }
                "LPJAFI" -> {
                    //Jobs Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPJAFT" -> {
                    //Jobs Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPJMS" -> {
                    //Jobs LHS of Main Section
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "LPJTB" -> {
                    //Jobs Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_header_top_slider
                        )
                    )
                }
                //page 4
                "PDCABH" -> {
                    //Classifieds Above Browsing History
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_bottom
                        )
                    )
                }
                "PDCAFI" -> {
                    //Classifieds Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_bottom
                        )
                    )
                }
                "PDCAFT" -> {
                    //Classifieds Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_bottom
                        )
                    )
                }
                "PDCTB" -> {
                    //Classifieds Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_just_above_bottom
                        )
                    )
                }
                "PDEAFI" -> {
                    //Events Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "PDEAFT" -> {
                    //Events Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "PDETB" -> {
                    //Events Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "PDJAFI" -> {
                    //Jobs Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "PDJAFT" -> {
                    //Jobs Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "PDJMS" -> {
                    //Jobs LHS of Main Section
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }
                "PDJTB" -> {
                    //Jobs Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ads_event_bottom_listing
                        )
                    )
                }






                else -> {
                    //Classifieds Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
            }

        }

    }

    @JvmName("setData1")
    fun setData(advertisePageList: List<AdvertisePageLocationResponseItem>) {
        this.advertisePageList = advertisePageList
        notifyDataSetChanged()
    }

    override fun getItemCount() = advertisePageList.size

    inner class AdvertiseViewHolder(val binding: AdvertiseTemplateItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )
}
