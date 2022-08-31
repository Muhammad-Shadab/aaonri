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
                            R.drawable.home_page_ads
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
                            R.drawable.home_classified_section_inline
                        )
                    )
                }
                "HPEIA" -> {
                    //Events Section Inline
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.home_page_ads
                        )
                    )
                }
                "HPAFI" -> {
                    //Home Page Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.home_page_ads
                        )
                    )
                }
                "HPAFT" -> {
                    //Home Page Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "HPLTB" -> {
                    //Home Page Long Term Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "HPLTBD" -> {
                    //Home Page Long Term Bottom Down
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "HPLTBU" -> {
                    //Home Page Long Term Bottom Upper
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "HPTB" -> {
                    //Home Page Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "HPIIA" -> {
                    //Home Page Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "HPJIA" -> {
                    //Jobs Section Inline
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                //page 2
                "DBBDA" -> {
                    //Dashboard Below Discover Aaonri
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "DBBIA" -> {
                    //Dashboard Below Interest Area
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "DBAFI" -> {
                    //Dashboard Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "DBAFT" -> {
                    //Dashboard Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "DBTB" -> {
                    //Dashboard Top Banner(Below Search Bar)
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                //page 3
                "LPCAFI" -> {
                    //Classifieds Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "LPCAFT" -> {
                    //Classifieds Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "LPCMS" -> {
                    //Classifieds LHS of Main Section
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "LPCTB" -> {
                    //Classifieds Top Banner
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "LPEAFI" -> {
                    //Events Just Above Footer Image Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
                        )
                    )
                }
                "LPEAFT" -> {
                    //Events Just Above Footer Text Only
                    mobileTemplateImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.detail_page_ads
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
