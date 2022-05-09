package com.example.kjfilemanager

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CreateFileAdapter(var fileArray: ArrayList<String>, var myContext: Context): BaseAdapter() {
    override fun getCount(): Int {
        return fileArray.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var cv = p1
        if (cv == null){
            val textView = TextView(myContext)
            textView.setTextColor(Color.rgb(0,0,0))
            textView.textSize = 20F
            textView.text = fileArray[p0]
            textView.height = 125
            cv = textView
        }

        return cv

    }

}