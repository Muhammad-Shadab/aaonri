package com.aaonri.app.ui.dashboard.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.R
import com.aaonri.app.data.authentication.register.viewmodel.AuthCommonViewModel
import com.aaonri.app.data.home.model.InterestResponseItem
import com.aaonri.app.databinding.HomeInterestedServiceItemsBinding

class HomeInterestsServiceAdapter(
    private var selectedServices: ((value: String) -> Unit)
) :
    RecyclerView.Adapter<HomeInterestsServiceAdapter.ClassifiedViewHolder>() {


    private var data = listOf<InterestResponseItem>()
    var rowIndex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassifiedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeInterestedServiceItemsBinding.inflate(inflater, parent, false)
        return ClassifiedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassifiedViewHolder, position: Int) {
        val context = holder.itemView.context


        holder.binding.apply {
            serviceTv.text = data[position].interestDesc


            holder.itemView.setOnClickListener {
                rowIndex = position
                notifyDataSetChanged()
            }


            if (rowIndex == position) {
                selectedServices(data[position].interestDesc)
                serviceTv.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.selected_tab_background
                    )
                )
                serviceTv.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            } else {
                serviceTv.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.tab_background
                    )
                )
                serviceTv.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.darkGrayColor
                    )
                )
            }
        }
    }

    @JvmName("setData1")
    fun setData(data: MutableList<InterestResponseItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class ClassifiedViewHolder(val binding: HomeInterestedServiceItemsBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}