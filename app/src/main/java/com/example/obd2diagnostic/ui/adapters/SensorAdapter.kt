package com.example.obd2diagnostic.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obd2diagnostic.R

data class SensorItem(val name: String, val value: String)

class SensorAdapter(private var items: List<SensorItem>) : RecyclerView.Adapter<SensorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_dtc_code) // Reuse DTC code style
        val tvValue: TextView = view.findViewById(R.id.tv_dtc_desc) // Reuse DTC desc style
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dtc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvName.setTextColor(0xFF2196F3.toInt()) // Set to primary blue
        holder.tvValue.text = item.value
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<SensorItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
