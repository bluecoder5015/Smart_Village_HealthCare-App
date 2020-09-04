package com.example.hacathon_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.issue_item.view.*
import kotlinx.android.synthetic.main.time_item.view.*

class TimeTodoAdapter(
    private var todo: List<TimeTodos>
): RecyclerView.Adapter<TimeTodoAdapter.Todoviewholder>() {

    inner class Todoviewholder(itemview : View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Todoviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_item,parent,false)
        return Todoviewholder(view)
    }

    override fun getItemCount(): Int {
        return todo.size
    }

    override fun onBindViewHolder(holder: Todoviewholder, position: Int) {
        holder.itemView.apply {
            name_timeitem.text =todo[position].Doctor_name
            spec_timeitem.text= todo[position].Specilist
            date_timeitem.text =todo[position].Datedisplay
            time_timeitem.text = todo[position].Time
        }
    }
}