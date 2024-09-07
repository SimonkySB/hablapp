package com.example.hablapp.services

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hablapp.models.Nota

class NotasService : ViewModel() {

    private var id: Int = 1;
    var notas = mutableStateListOf<Nota>()
        private set


    fun agregarNota(nota: Nota) {
        val n = obtenerNotaPorId(nota.id)
        if (n == null) {
            nota.id = id
            notas.add(nota)
            id += 1
        }
        else {
            val index = notas.indexOf(n)
            notas[index] = nota
        }

    }


    fun obtenerNotaPorId(id: Int): Nota? {
        return notas.find { it.id == id }
    }

    fun eliminarNota(id: Int){
        notas.removeIf { it.id == id }
    }

    fun limpiar(){
        notas.clear()
    }
}