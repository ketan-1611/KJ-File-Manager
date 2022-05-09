package com.example.kjfilemanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.format.Formatter
import android.text.format.Formatter.formatShortFileSize
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class InternalFragment : Fragment() , onFileSelectedListener{

    lateinit var recyclerView: RecyclerView
    lateinit var showFilePath: TextView
    lateinit var back : ImageView
    lateinit var fileList: MutableList<File>
    lateinit var storage: File
    lateinit var data : String
    lateinit var fileAdapter : FileAdapter
    var items = arrayOf("Details","Rename","Delete","Share")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_internal, container, false)

        showFilePath = view.findViewById(R.id.tvInternalText)
        back = view.findViewById(R.id.ivInternalBack)
        recyclerView = view.findViewById(R.id.rvInternal)

        val internalStorage = System.getenv("EXTERNAL_STORAGE")
        storage = File(internalStorage)
        showFilePath.text = storage.absolutePath

        try {
            data = requireArguments().getString("path").toString()
            val file = File(data)
            storage = file
            showFilePath.text = storage.absolutePath
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        displayFiles()

        return view
    }

    private fun displayFiles(){
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        fileList = ArrayList()
        fileList.addAll(searchfile(storage))
        fileAdapter = FileAdapter(requireContext(),fileList,this)
        recyclerView.adapter = fileAdapter
    }

    private fun searchfile(file: File) : ArrayList<File>{
        val fileArrayList = ArrayList<File>()
        val files = file.listFiles()

        for (singleFile in files){
            if (singleFile.isDirectory && !singleFile.isHidden){
                fileArrayList.add(singleFile)
            }
        }

        for (singlefile in files){
            if (singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpeg") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".jpg") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".png") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".gif") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp3") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".wav") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".mp4") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".txt") ||
                singlefile.name.lowercase(Locale.getDefault()).endsWith(".pdf")){
                fileArrayList.add(singlefile)
            }
        }
        return fileArrayList
    }

    override fun onfileClick(file: File) {
        if (file.isDirectory){
            val bundle = Bundle()
            bundle.putString("path",file.absolutePath)
            val internalFragment = InternalFragment()
            internalFragment.arguments = bundle
            requireFragmentManager().beginTransaction().replace(R.id.frame,internalFragment)
                .addToBackStack(null).commit()
        }
        else
        {
            try{
                FileOpener.openFile(requireContext(),file)
            }
            catch(e:IOException){
                e.printStackTrace()
            }
        }
    }

    override fun onfileLongClick(file: File, position: Int) {
        val optionDialogBox = Dialog(requireContext())
        optionDialogBox.setContentView(R.layout.option_dialog)
        optionDialogBox.setTitle("select a option")
        val optiondialogues = optionDialogBox.findViewById<View>(R.id.list) as ListView
        val customAdapter = CustomAdapter()
        optiondialogues.adapter = customAdapter
        optionDialogBox.show()
        optiondialogues.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (parent.getItemAtPosition(position).toString()){
                "Details" -> {
                    val alertDialog = AlertDialog.Builder(context)
                    alertDialog.setTitle("Details")
                    val details = TextView(context)
                    alertDialog.setView(details)

                    val modifiedDate = Date(file.lastModified())
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                    val dateFormatted = simpleDateFormat.format(modifiedDate)
                    val size = Formatter.formatShortFileSize(context,file.length())
                    details.text = """
                        File Name : ${file.name}
                        Path : ${file.absolutePath}
                        File Size : $size
                        Last modified date : $dateFormatted
                    """.trimIndent()
                    alertDialog.setPositiveButton("ok"){dialog, _ -> dialog.dismiss()}
                    alertDialog.create()
                    alertDialog.show()

                }

                "Rename" -> {
                    val renameAlertDialog = AlertDialog.Builder(context)
                    renameAlertDialog.setTitle("Rename File")
                    val name = EditText(context)
                    renameAlertDialog.setView(name)
                    renameAlertDialog.setPositiveButton("ok"){dialog, _ ->
                        val newFileName = name.editableText.toString()
                        val extension = file.absolutePath.substring(file.absolutePath.lastIndexOf(".")+1)
                        val current = File(file.absolutePath)
                        val destination = File(file.absolutePath.replace(file.name,newFileName) + extension)
                        if (current.renameTo(destination)){
                           fileList[position] = destination
                            fileAdapter.notifyDataSetChanged()
                            Toast.makeText(context,"File name changed",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(context,"File name could not be changed",Toast.LENGTH_LONG).show()
                        }
                        dialog.dismiss()
                    }
                    renameAlertDialog.setNegativeButton("Cancel"){dialog, _ ->
                        dialog.dismiss()
                    }
                    renameAlertDialog.create()
                    renameAlertDialog.show()
                }

                "Delete" -> {
                    deleteFile(file,position)
                }

                "Share" -> {
                    shareFile(file)
                }


            }
        }
    }

    fun deleteFile(file: File, position: Int){
        val deleteFile = AlertDialog.Builder(context)
        deleteFile.setTitle("Delelte "+ file.name + " ?")
        deleteFile.setPositiveButton("ok"){dialog, _ ->
            file.delete()
            fileList.removeAt(position)
            fileAdapter.notifyDataSetChanged()
            dialog.dismiss()
            Toast.makeText(context,"File Deleted successfully ", Toast.LENGTH_LONG).show()
        }
        deleteFile.setNegativeButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }
        deleteFile.create()
        deleteFile.show()
    }

    fun shareFile(file: File){
        val fileName = file.name
        var share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "*/*"
        val shareURI = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            file
        )
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        share.putExtra(Intent.EXTRA_STREAM,shareURI)
        startActivity(Intent.createChooser(share,"Send $fileName ? "))
    }


    internal inner class CustomAdapter : BaseAdapter(){
        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(p0: Int): Any {
            return items[p0]
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.option_layout,null)
            val textOption = view.findViewById<TextView>(R.id.tvOption)
            val img = view.findViewById<ImageView>(R.id.ivOption)

            textOption.text = items[p0]

            if (items[p0]=="Details") {
                img.setImageResource(R.drawable.details)
            }
           if (items[p0]=="Rename"){
                img.setImageResource(R.drawable.rename)
            }
            if(items[p0]=="Delete"){
                img.setImageResource(R.drawable.delete)
            }
            if (items[p0]=="Share"){
                img.setImageResource(R.drawable.share)
            }

            return view
        }

    }

}