package com.example.hacathon_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.issue_item.view.*

class TodoAdapter(
    private var todo: List<Todos>
): RecyclerView.Adapter<TodoAdapter.Todoviewholder>() {

    inner class Todoviewholder(itemview : View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Todoviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.issue_item,parent,false)
        return Todoviewholder(view)
    }

    override fun getItemCount(): Int {
        return todo.size
    }

    override fun onBindViewHolder(holder: Todoviewholder, position: Int) {
        holder.itemView.apply {
            name_item.text =todo[position].Name
            age_item.text =todo[position].Age
            symptom_item.text= todo[position].Symptoms
            intake_item.text =todo[position].Medicalintake
            injury_item.text = todo[position].Injury
        }
    }
}