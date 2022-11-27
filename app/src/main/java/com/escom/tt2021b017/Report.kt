package com.escom.tt2021b017

import com.google.type.DateTime

data class Report(val id: String, val fecha: String, val frecuencia: String) {

    fun getFrecuenciaToString(): String{
        return "$frecuencia PPM"
    }
}
