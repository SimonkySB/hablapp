package com.example.hablapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


interface AuthManagerInterface {
    suspend fun loginWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?>
    suspend fun registerWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?>
    suspend fun recuperarClave(email: String): AuthRes<Void>
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
}

sealed class AuthRes<out T> {
    data class Success<T>(val data: T): AuthRes<T>()
    data class Error(val errorMessage: String): AuthRes<Nothing>()
}

class AuthManager(private val auth: FirebaseAuth) : AuthManagerInterface {


    override suspend fun loginWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?>{
        return try {
            val task = auth.signInWithEmailAndPassword(email, password)
            val result = task.await()
            AuthRes.Success(result.user)
        } catch(e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(result.user)
        } catch(e: Exception) {
            AuthRes.Error(e.message ?: "Error el registrar el usuario")
        }
    }

    override suspend fun recuperarClave(email: String) : AuthRes<Void> {
        return try {
            val result = auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(result)
        } catch(e: Exception) {
            AuthRes.Error(e.message ?: "Error el registrar el usuario")
        }

    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}