package com.example.kjfilemanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kjfilemanager.FileOpener.openFile
import java.io.File
import java.io.IOException

class CategoryViewAdapter(
    private val context: Context,
    private val fileList : List<File>,
    private val category : String
    ) : RecyclerView.Adapter<CategoryViewAdapter.CategoryViewHolder>()  {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var img : ImageView = itemView.findViewById(R.id.ivCategoryView)
        var txt : TextView = itemView.findViewById(R.id.tvCategoryView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewAdapter.CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_category_view,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewAdapter.CategoryViewHolder, position: Int) {
        val model = fileList[position]
        holder.txt.text = model.name
        setImage(holder,model)
        holder.itemView.setOnClickListener{
            try {
                openFile(context,model)
            }
            catch (e : IOException){
                e.printStackTrace()
            }
        }
    }

    private fun setImage(categoryViewHolder:CategoryViewHolder,singleFile: File){
        when(category){
            "Image" -> categoryViewHolder.img.setImageResource(R.drawable.ic_baseline_image_24)
            "Video" -> categoryViewHolder.img.setImageResource(R.drawable.ic_baseline_ondemand_video_24)
            "Audio" -> categoryViewHolder.img.setImageResource(R.drawable.ic_baseline_audiotrack_24)
            "Document" -> categoryViewHolder.img.setImageResource(R.drawable.ic_baseline_insert_drive_file_24)

        }
    }

    override fun getItemCount(): Int {
        return fileList.size
    }
}