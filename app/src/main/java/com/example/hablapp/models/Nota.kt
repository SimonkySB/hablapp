package com.example.hablapp.models

import java.util.Date


data class Nota(
    var id: Int = 0,
    val titulo: String,
    val descripcion: String,
    var fechaCreacion: Date
)