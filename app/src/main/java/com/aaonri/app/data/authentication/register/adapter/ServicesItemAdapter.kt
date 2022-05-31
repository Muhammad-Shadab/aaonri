package com.aaonri.app.data.authentication.register.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.model.services.ServicesResponseItem
import com.aaonri.app.databinding.ServicesGridItemBinding

class ServicesItemAdapter(private var selectedServices: ((value: List<ServicesResponseItem>) -> Unit)) :
    RecyclerView.Adapter<ServicesItemAdapter.CustomViewHolder>() {

    private var data = listOf<ServicesResponseItem>()

    var selectedCategoriesList: MutableList<ServicesResponseItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ServicesGridItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.apply {
            binding.apply {
                servicesGridTv.text = data[position].interestDesc

                if (selectedCategoriesList.contains(data[position])) {
                    selectedCategoriesList.add(data[position])
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
                }

                itemView.setOnClickListener {
                    if (selectedCategoriesList.contains(data[position])) {
                        selectedCategoriesList.remove(data[position])
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
                    } else {
                        selectedCategoriesList.add(data[position])
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
                    }
                    selectedServices(selectedCategoriesList)
                }
            }
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ServicesResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CustomViewHolder(
        val binding: ServicesGridItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root)

}



