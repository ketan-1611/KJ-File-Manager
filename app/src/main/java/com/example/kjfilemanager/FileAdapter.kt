package com.example.kjfilemanager

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*

class FileAdapter(
    private val context: Context,
    private val files : List<File>,
    private val onFileSelectedListener: onFileSelectedListener
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>(){

    inner class FileViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var fileName : TextView = itemView.findViewById(R.id.tvFileContainerName)
        var fileSize : TextView = itemView.findViewById(R.id.tvFileContainerFileSize)
        var fileImg : ImageView = itemView.findViewById(R.id.ivFileContainer)
        var container : CardView = itemView.findViewById(R.id.cardContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileAdapter.FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_container_layout,parent,false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileAdapter.FileViewHolder, position: Int) {
        val model = files[position]
        holder.fileName.text = model.name
        var items= 0
        if (model.isDirectory){
            val files1 = model.listFiles()

            for (singlefile in files1){
                if (!singlefile.isHidden){
                    items++
                }
            }

            holder.fileSize.text = "$items Files"
            holder.fileImg.setImageResource(R.drawable.ic_baseline_folder_24)

        }
        else{
            holder.fileSize.text = Formatter.formatShortFileSize(context,model.length())
            getImg(model,holder)
        }

        holder.container.setOnClickListener{
            onFileSelectedListener.onfileClick(model)
        }

        holder.container.setOnLongClickListener{
            onFileSelectedListener.onfileLongClick(model,position)
            true
        }

    }

    private fun getImg(model : File , holder: FileViewHolder)
    {
        if (model.name.lowercase(Locale.getDefault()).endsWith(".jpg") ||
            model.name.lowercase(Locale.getDefault()).endsWith(".jpeg") ||
            model.name.lowercase(Locale.getDefault()).endsWith(".png") ||
            model.name.lowercase(Locale.getDefault()).endsWith(".gif")){
            holder.fileImg.setImageResource(R.drawable.ic_baseline_image_24)
        }
        else if(model.name.lowercase(Locale.getDefault()).endsWith(".pdf") ||
            model.name.lowercase(Locale.getDefault()).endsWith("..txt") || !model.isDirectory){
            holder.fileImg.setImageResource(R.drawable.ic_baseline_insert_drive_file_24)
        }
        else if (model.name.lowercase(Locale.getDefault()).endsWith(".mp3") ||
            model.name.lowercase(Locale.getDefault()).endsWith("..wzv")){
            holder.fileImg.setImageResource(R.drawable.ic_baseline_audiotrack_24)
        }
        else if (model.name.lowercase(Locale.getDefault()).endsWith(".mp4")){
            holder.fileImg.setImageResource(R.drawable.ic_baseline_ondemand_video_24)
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }


}