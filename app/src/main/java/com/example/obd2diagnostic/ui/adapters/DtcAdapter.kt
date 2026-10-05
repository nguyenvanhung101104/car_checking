package com.example.obd2diagnostic.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obd2diagnostic.R
import com.example.obd2diagnostic.utils.DtcDescriptionMap

data class DtcItem(val code: String, val type: String)

class DtcAdapter(private var items: List<DtcItem>) : RecyclerView.Adapter<DtcAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCode: TextView = view.findViewById(R.id.tv_dtc_code)
        val tvDesc: TextView = view.findViewById(R.id.tv_dtc_desc)
        val tvType: TextView = view.findViewById(R.id.tv_dtc_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dtc, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvCode.text = item.code
        holder.tvDesc.text = DtcDescriptionMap.getDescription(item.code)
        holder.tvType.text = item.type
        
        // Color coding type
        if (item.type == "Confirmed") {
            holder.tvType.setTextColor(0xFFF44336.toInt()) // Red
        } else {
            holder.tvType.setTextColor(0xFFFF9800.toInt()) // Orange for Pending
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<DtcItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
