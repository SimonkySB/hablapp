package com.example.hablapp.utils

import java.text.SimpleDateFormat
import java.util.Date

fun fechaConHora(fecha: Date): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
    val formattedDate = dateFormat.format(fecha)
    return formattedDate
}