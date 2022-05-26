package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.os.strictmode.UnsafeIntentLaunchViolation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.ServicesModel
import com.aaonri.app.databinding.FragmentServicesCategoryBinding
import com.aaonri.app.databinding.ServicesGridItemBinding

class ServicesItemAdapter(private var showLayout: ((value: Boolean) -> Unit)) :
    RecyclerView.Adapter<ServicesItemAdapter.CustomViewHolder>() {

    private var data = listOf<ServicesModel>()


    var selectedCategoriesList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ServicesGridItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.apply {
            binding.apply {
                servicesGridIv.load(data[position].image)
                servicesGridTv.text = data[position].title

                itemView.setOnClickListener {
                    if (selectedCategoriesList.contains(data[position].title)) {
                        servicesGridIv.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.blueBtnColor
                            )
                        )
                        servicesGridIv.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.serviceCardLightBlue
                            )
                        )

                    }
                    if (selectedCategoriesList.isEmpty() || !selectedCategoriesList.contains(data[position].title)) {
                        servicesGridIv.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        )
                        servicesGridIv.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.blueBtnColor
                            )
                        )
                        selectedCategoriesList.add(data[position].title)
                    }

                    if (selectedCategoriesList.size >= 3) {
                        showLayout(true)
                    } else {
                        showLayout(false)
                    }
                }
            }
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ServicesModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(
        val binding: ServicesGridItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

}



