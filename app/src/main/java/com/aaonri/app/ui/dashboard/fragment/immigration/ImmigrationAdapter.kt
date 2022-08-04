package com.aaonri.app.ui.dashboard.fragment.immigration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aaonri.app.databinding.ImmigrationsItemBinding

class ImmigrationAdapter(private var selectedServices: ((value: String) -> Unit)) :
    RecyclerView.Adapter<ImmigrationAdapter.ImmigrationViewHolder>() {

    private var data = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImmigrationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImmigrationsItemBinding.inflate(inflater, parent, false)
        return ImmigrationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImmigrationViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            noOfCahtTv.text = data[position]
        }
    }

    @JvmName("setData1")
    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    inner class ImmigrationViewHolder(val binding: ImmigrationsItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

}