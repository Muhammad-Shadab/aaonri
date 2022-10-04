package com.aaonri.app.ui.dashboard.fragment.classified.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.BuildConfig
import com.aaonri.app.R
import com.aaonri.app.data.classified.model.Classified
import com.aaonri.app.databinding.ClassifiedCardItemsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.math.RoundingMode
import java.text.DecimalFormat

class FavoriteClassifiedAdapter(private var selectedServices: ((value: Classified) -> Unit)) :
    RecyclerView.Adapter<FavoriteClassifiedAdapter.ClassifiedViewHolder>() {

    private var data = listOf<Classified>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ClassifiedCardItemsBinding.inflate(inflater, parent, false)
        return ClassifiedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            val random = data[position].askingPrice

            val df = DecimalFormat("#,###.00")
            df.roundingMode = RoundingMode.DOWN
            val roundoff = df.format(random)
            like.visibility = View.VISIBLE
            if (data[position].userAdsImages.isEmpty()) {
                classifiedPriceTv.text = "$$roundoff"
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            } else {
                Glide.with(context)
                    .load("${BuildConfig.BASE_URL}/api/v1/common/classifiedFile/${data[position].userAdsImages[0].imagePath}")
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            classifiedItemIv.setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_image_placeholder
                                )
                            )
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(classifiedItemIv)
                classifiedPriceTv.text = "$$roundoff"
                classifiedTitleTv.text = data[position].adTitle
                locationClassifiedTv.text = data[position].adLocation + " - " + data[position].adZip
                popularTv.visibility =
                    if (data[position].popularOnAaonri) View.VISIBLE else View.GONE
            }
            val date = data[position].createdOn.subSequence(0, 10)
            val year = date.subSequence(0, 4)
            val month = date.subSequence(5, 7)
            val day = date.subSequence(8, 10)
            classifiedPostDateTv.text = "Posted On: $month-$day-$year"
        }
        holder.itemView.setOnClickListener {
            selectedServices(data[position])
        }
    }

    override fun getItemCount() = data.size

    @JvmName("setData1")
    fun setData(data: List<Classified>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class ClassifiedViewHolder(val binding: ClassifiedCardItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

}