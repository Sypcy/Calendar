package com.example.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.RecyclerView

class AdapterMonth(
    private val month: List<Int>
    ,private val itemsClick: (Int)-> Unit
): RecyclerView.Adapter<AdapterMonth.MonthViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_month,parent,false)
        return MonthViewHolder(view)

    }
    class MonthViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemsMonth)
        val composeView: ComposeView = itemView.findViewById(R.id.composeView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.textView.text = "${month[position]}月"
        holder.composeView.setContent {
            Box(Modifier
                .clickable{itemsClick(position)}
            ){
                Date(month[position],20,11)
            }
        }
    }

    override fun getItemCount(): Int = month.size
}