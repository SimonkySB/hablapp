package com.example.hablapp.utils

import com.example.hablapp.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.jetbrains.annotations.Async.Execute

class NotasDBManager {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("notas")
    private val authManager: AuthManager = AuthManager()

    fun agregarNota(nota: Nota) {
        val key = databaseRef.push().key
        if(key != null) {
            databaseRef.child(key).setValue(nota)
        }
    }

    fun actualizarNota(key: String, nota: Nota) {
        databaseRef.child(key).setValue(nota)
    }

    fun obtenerNotas(): Flow<List<Nota>> {
        val idFilter = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            val listener = databaseRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notas = snapshot.children.mapNotNull { snap ->
                        val nota = snap.getValue(Nota::class.java)
                        snap.key?.let {  nota?.copy(key = it) }
                    }
                    trySend(notas.filter { it.userId == idFilter }).isSuccess
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose {databaseRef.removeEventListener(listener)}
        }
        return flow
    }

    suspend fun obtenerNotaPorId(key: String): Nota?{
        return try {
            val res = databaseRef.child(key).get().await()
            res.getValue(Nota::class.java)?.copy(key = key)
        } catch (ex: Exception) {
            null
        }
    }

    fun eliminarNota(key: String) {
        databaseRef.child(key).removeValue()
    }
}