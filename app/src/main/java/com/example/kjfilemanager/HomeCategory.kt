package com.example.kjfilemanager

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class HomeCategory : AppCompatActivity() {

    lateinit var category: String
    lateinit var categoryName : TextView
    lateinit var fileLink : String
    lateinit var categoryRecyclerView: RecyclerView
    lateinit var categoryViewAdapter: CategoryViewAdapter
    lateinit var datas : MutableList<File>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_category)

        category = intent.getStringExtra("category").toString()
        categoryName = findViewById<View>(R.id.tvToolbar) as TextView
        categoryRecyclerView = findViewById<View>(R.id.rvCategory) as RecyclerView
        datas = ArrayList()
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryViewAdapter = CategoryViewAdapter(this@HomeCategory,datas as ArrayList<File>,category)
        categoryRecyclerView.adapter = categoryViewAdapter


        fileLink = System.getenv("EXTERNAL_STORAGE")


        categoryName.text = category

        gettingfile(fileLink)
    }

    private fun gettingfile(path: String){
        val file = File(path)
        if (file.isDirectory && file.canRead()){
            val file1 = file.listFiles()

            for (singlefile in file1)
            {
                if (singlefile.isDirectory && singlefile.canRead()){
                    gettingfile(singlefile.absolutePath)
                }
                else if (singlefile.isFile && singlefile.canRead()){
                    displayFile(singlefile)
                }
            }
        }
        else if(file.isFile && file.canRead()){
            displayFile(file)
        }
    }

    private fun displayFile(singlefile: File){
        when (category){
           "Image" -> if (singlefile.name.lowercase(Locale.getDefault()).endsWith(".png") ||
                   singlefile.name.lowercase(Locale.getDefault()).contains(".jpg") ||
                   singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpeg") ||
                       singlefile.name.lowercase(Locale.getDefault()).endsWith(".gif")){
               datas.add(singlefile)
           }
            "Video" -> if (singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp4") ||
                    singlefile.name.lowercase(Locale.getDefault()).contains(".mkv")){
                datas.add(singlefile)
            }

            "Audio" -> if (singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp3") ||
                singlefile.name.lowercase(Locale.getDefault()).contains(".wav")) {
                datas.add(singlefile)
            }

            "Document" -> if (singlefile.name.lowercase(Locale.getDefault()).endsWith(".txt")||
                    singlefile.name.lowercase(Locale.getDefault()).contains(".pdf")){
                datas.add(singlefile)
            }

        }
    }
}