package com.example.hablapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


sealed class AuthRes<out T> {
    data class Success<T>(val data: T): AuthRes<T>()
    data class Error(val errorMessage: String): AuthRes<Nothing>()
}

class AuthManager {
    private val auth: FirebaseAuth by lazy {  Firebase.auth }


    suspend fun loginWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?>{
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(result.user)
        } catch(e: Exception) {
            AuthRes.Error(e.message ?: "Error el registrar el usuario")
        }
    }

    suspend fun registerWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(result.user)
        } catch(e: Exception) {
            AuthRes.Error(e.message ?: "Error el registrar el usuario")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}