package com.example.kjfilemanager

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class HomeArrayListAdapter(var modelArrayList: ArrayList<Model>, var context: Context)
    : RecyclerView.Adapter<HomeArrayListAdapter.HomeViewHolder>(){

        inner class HomeViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
            var img : ImageView = itemview.findViewById(R.id.ivShowHome)
            var txt : TextView = itemview.findViewById(R.id.tvShowHome)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_home_grid_box,parent,false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val model = modelArrayList[position]
        holder.img.setImageResource(model.image)
        holder.txt.setText(model.text)

        holder.itemView.setOnClickListener {
            Toast.makeText(context,"Fetching ${holder.txt.text}s", Toast.LENGTH_SHORT).show()
            val intent = Intent(context,HomeCategory::class.java)
            intent.putExtra("category",model.text)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }
}