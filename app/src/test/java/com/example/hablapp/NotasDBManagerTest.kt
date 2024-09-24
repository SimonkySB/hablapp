package com.example.hablapp

import com.example.hablapp.models.Nota
import com.example.hablapp.utils.AuthManagerInterface
import com.example.hablapp.utils.NotasDBManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever



class NotasDBManagerTest {

    private lateinit var notasDBManager: NotasDBManager
    private lateinit var mockAuthManager: AuthManagerInterface
    private lateinit var mockUser: FirebaseUser
    private lateinit var mockDatabaseRef: DatabaseReference
    private lateinit var mockUserDbRef: DatabaseReference
    private lateinit var mockKeyRef: DatabaseReference

    @Before
    fun setUp() {
        mockAuthManager = mock()
        mockUser = mock()
        mockDatabaseRef = mock()
        mockUserDbRef = mock()
        mockKeyRef = mock()

        whenever(mockAuthManager.getCurrentUser()).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn("testUserId")

        whenever(mockDatabaseRef.child("notas/testUserId")).thenReturn(mockUserDbRef)
        whenever(mockUserDbRef.push()).thenReturn(mockKeyRef)
        whenever(mockKeyRef.key).thenReturn("mockKey")

        notasDBManager = NotasDBManager(mockAuthManager, mockDatabaseRef)
    }




    @Test
    fun `guardarNota should push new note when key is null`() {
        val nota = Nota(key = null)


        notasDBManager.guardarNota(nota)

        verify(mockKeyRef).setValue(nota)
        assertEquals("mockKey", nota.key)
    }


    @Test
    fun `guardarNota should do nothing if there is no current user`() {
        whenever(mockAuthManager.getCurrentUser()).thenReturn(null)

        val nota = Nota(key = null)

        notasDBManager.guardarNota(nota)

        verify(mockDatabaseRef, never()).push()
        verify(mockUserDbRef, never()).setValue(any())
    }

    @Test
    fun `guardarNota should set userId for the note`() {
        val nota = Nota(key = null)

        notasDBManager.guardarNota(nota)

        assertEquals("testUserId", nota.userId)
    }
}