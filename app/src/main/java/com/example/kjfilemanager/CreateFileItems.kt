package com.example.kjfilemanager

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import java.io.*
import java.lang.StringBuilder

class CreateFileItems : AppCompatActivity() {

    lateinit var etName : EditText
    lateinit var btnCreateFolder : AppCompatButton
    lateinit var btnOpen : AppCompatButton
    lateinit var btnCreateFiles : AppCompatButton
    lateinit var sName : String
    lateinit var fileArray : ArrayList<String>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_file_items)

        etName = findViewById(R.id.etCreateFile)
        btnCreateFolder = findViewById(R.id.btnCreateFolder)
        btnOpen = findViewById(R.id.btnCreateOpen)
        fileArray = ArrayList()
        listView = findViewById(R.id.lvCreate)

        btnCreateFolder.setOnClickListener {
            sName = etName.text.toString(). trim(' ')
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
                createFolder()
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),101)
            }
        }

        btnOpen.setOnClickListener {
            sName = etName.text.toString().trim(' ')
            val uri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/" + sName + "/")
            startActivity(Intent(Intent.ACTION_GET_CONTENT).setDataAndType(uri,"*/*"))
        }


    }

    private fun createFolder() {
        val folder = File(Environment.getExternalStorageDirectory(),sName)
        if (folder.exists()){
            Toast.makeText(applicationContext,"Folder Already Exists",Toast.LENGTH_SHORT).show()
        }
        else
        {
            folder.mkdir()
            if (folder.isDirectory){
                Toast.makeText(applicationContext,"Folder is created successfully",Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(applicationContext,"Folder could not be created",Toast.LENGTH_SHORT).show()
            }
        }
    }

     fun createFile(view: View){
        val layout = layoutInflater.inflate(R.layout.create_file,null, false)
        AlertDialog.Builder(this)
            .setTitle("Create File")
            .setView(layout)
            .setPositiveButton("Create"){dialog, _ ->
                val myFileName = layout.findViewById<EditText>(R.id.etCreateFileNew)
                val myFileContent = layout.findViewById<EditText>(R.id.etCreateFileContent)
                addFile(myFileName.text.toString(),myFileContent.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Close"){dialog, _ -> dialog.dismiss()}
            .show()
    }

    private fun addFile(fileName : String, fileContent : String)
    {
        try {
            var reader = openFileOutput(fileName, MODE_PRIVATE)
            reader.write(fileContent.toByteArray())
            reader.close()
            loadFiles()
        }
        catch (e : FileNotFoundException){
            e.printStackTrace()
        }
        catch (e : IOException)
        {
            e.printStackTrace()
        }
    }

    private fun loadFiles()
    {
        val currentDir = filesDir
        fileArray.clear()
        fileArray.addAll(listOf(*currentDir.list()))
        val adapter = CreateFileAdapter(fileArray,this)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            showFileContent(fileArray[position])
        }

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener{parent,view, position,id ->
            AlertDialog.Builder(this)
                .setTitle("Do you want to delete this file ? ")
                .setPositiveButton("Yes"){dialog, _, ->
                    delete_file(fileArray[position])
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog, _, ->
                    dialog.dismiss()
                }
                .show()
            true
        }
    }

    private fun showFileContent(s: String){
        val sb = StringBuilder()
        val file = File(filesDir,s)
        try {
            var br = BufferedReader(FileReader(file))
            var line : String?
            while (br.readLine().also { line = it } != null){
                sb.append(line)
                sb.append('\n')
            }
        }catch (e : FileNotFoundException){
                e.printStackTrace()
        }
        catch (e : IOException)
        {
            e.printStackTrace()
        }

        val layout = layoutInflater.inflate(R.layout.create_file,null,false)
        val myFileName = layout.findViewById<EditText>(R.id.etCreateFileNew)
        val myFileContent = layout.findViewById<EditText>(R.id.etCreateFileContent)
        myFileName.setText(s)
        myFileContent.setText(sb)
        AlertDialog.Builder(this)
            .setTitle("Update / make change to file")
            .setView(layout)
            .setPositiveButton("Update"){dialog, _ ->
                updateFile(myFileName.text.toString(),myFileContent.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Close"){dialog, _ ->
                dialog.dismiss()
            }
            .show()


    }

    private fun updateFile(fileName: String, fileContent : String){
        try {
            var reader = openFileOutput(fileName, MODE_PRIVATE)
            reader.write(fileContent.toByteArray())
            reader.close()
            loadFiles()
        }
        catch (e : FileNotFoundException){
            e.printStackTrace()
        }
        catch (e : IOException)
        {
            e.printStackTrace()
        }
    }

    private fun delete_file(s: String){
        val file = File(filesDir,s)
        file.delete()
        loadFiles()
    }
    
}