package com.example.hablapp.services

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hablapp.models.Usuario



class UsuariosService : ViewModel()  {
    private val _items = mutableStateListOf<Usuario>()
    val items: List<Usuario> get() = _items

    init {
        llenarUsuarios()
    }

    fun registrar(item: Usuario): String? {
        if(_items.find { u -> u.username == item.username } != null) {
            return "El nombre de usuario se encuentra en uso"
        }
        if(_items.find { u -> u.email == item.email } != null) {
            return "El email se encuentra en uso"
        }
        _items.add(item)
        return null
    }


    fun login(username: String, password: String): Boolean{
        val user = _items.find { u -> u.username == username && u.password == password }
        return user != null
    }

    private fun llenarUsuarios() {
        _items.add(Usuario("user1", "password1", "user1@example.com"))
        _items.add(Usuario("user2", "password2", "user2@example.com"))
        _items.add(Usuario("user3", "password3", "user3@example.com"))
        _items.add(Usuario("user4", "password4", "user4@example.com"))
        _items.add(Usuario("user5", "password5", "user5@example.com"))
    }

}