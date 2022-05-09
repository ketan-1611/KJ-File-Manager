package com.example.kjfilemanager

import java.io.File

interface onFileSelectedListener {
    fun onfileClick(file : File)

    fun onfileLongClick(file: File, position : Int)
}