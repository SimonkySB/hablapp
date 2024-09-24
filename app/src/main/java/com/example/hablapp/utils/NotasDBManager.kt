package com.example.hablapp.utils

import com.example.hablapp.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await


class NotasDBManager(
    private val authManager: AuthManagerInterface,
    private val databaseRef: DatabaseReference
) {


    fun guardarNota(nota: Nota) {
        val userId = authManager.getCurrentUser()?.uid ?: return
        val databaseRef = getDbRef(userId);

        nota.userId = userId
        if(nota.key == null) {
            val keyRef = databaseRef.push()
            nota.key = keyRef.key;
            keyRef.setValue(nota)
        }
        else {
            databaseRef.child(nota.key!!).setValue(nota)
        }
    }


    fun obtenerNotas(): Flow<List<Nota>> {
        val userId = authManager.getCurrentUser()?.uid ?: return flowOf(emptyList())
        val databaseRef = getDbRef(userId);

        return callbackFlow {

            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notas = snapshot.children.mapNotNull { snap ->
                        val nota = snap.getValue(Nota::class.java)
                        snap.key?.let {  nota?.copy(key = it) }
                    }
                    trySend(notas).isSuccess
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            databaseRef.addValueEventListener(listener);
            awaitClose {databaseRef.removeEventListener(listener)}
        }
    }

    suspend fun obtenerNotaPorId(key: String): Nota?{
        val userId = authManager.getCurrentUser()?.uid ?: return null
        val databaseRef = getDbRef(userId);

        return try {
            val res = databaseRef.child(key).get().await()
            res.getValue(Nota::class.java)?.copy(key = key)
        } catch (ex: Exception) {
            null
        }
    }

    fun eliminarNota(key: String) {
        val userId = authManager.getCurrentUser()?.uid ?: return
        val databaseRef = getDbRef(userId);
        databaseRef.child(key).removeValue()
    }

    private fun getDbRef(userId: String): DatabaseReference {
        return databaseRef.child("notas/$userId")
    }
}