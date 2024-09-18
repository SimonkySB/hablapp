package com.example.hablapp.models

import java.util.Date


data class Nota(
    var key: String? = null,
    var titulo: String = "",
    var descripcion: String = "",
    var fechaCreacion: Date = Date(),
    var userId: String = ""
)