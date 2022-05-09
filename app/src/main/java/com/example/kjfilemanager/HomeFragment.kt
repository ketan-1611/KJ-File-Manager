package com.example.kjfilemanager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.DexterBuilder.*
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class HomeFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var otherFiles : TextView
    lateinit var gotoLocals : LinearLayout
    lateinit var createFolder : LinearLayout
    lateinit var modelArrayList: ArrayList<Model>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.rvHome)
        otherFiles = view.findViewById(R.id.tvothers)
        gotoLocals = view.findViewById(R.id.gotolabel)
        createFolder = view.findViewById(R.id.createFolder)

        gotoLocals.setOnClickListener {
            requireFragmentManager().beginTransaction().replace(R.id.frame,InternalFragment())
                .addToBackStack(null).commit()
        }

        createFolder.setOnClickListener {
            val intent = Intent(requireContext(),CreateFileItems :: class.java)
            startActivity(intent)
        }

        runTimePermission()

        return view
    }


    private fun setRecycler1(){
        modelArrayList = ArrayList()
        modelArrayList.add(Model("Images",R.drawable.ic_baseline_image_24))
        modelArrayList.add(Model("Video",R.drawable.ic_baseline_ondemand_video_24))
        modelArrayList.add(Model("Audio",R.drawable.ic_baseline_audiotrack_24))
        modelArrayList.add(Model("Document",R.drawable.folder))
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter = HomeArrayListAdapter(modelArrayList,requireContext())

    }

    private fun runTimePermission(){
        Dexter.withContext(context)
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()){
                        setRecycler1()
                        Toast.makeText(requireContext(),"Permission Granted",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),"Permission Denied: Please accept Permission",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }

            }).check()
    }


}