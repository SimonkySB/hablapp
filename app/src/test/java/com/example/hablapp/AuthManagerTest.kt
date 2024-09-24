package com.example.hablapp


import android.app.Activity
import com.example.hablapp.utils.AuthManager
import com.example.hablapp.utils.AuthRes
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.concurrent.Executor

class AuthManagerTest {

    private lateinit var authManager: AuthManager
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockUser: FirebaseUser

    @Before
    fun setup() {
        mockAuth = mock()
        mockUser = mock()
        authManager = AuthManager(mockAuth)
    }

    @Test
    fun loginWithEmailAndPassword() = runTest  {
        val email = "test@example.com"
        val password = "password"

        val mockAuthResult = mock<AuthResult> {
            on { user } doReturn mockUser
        }
        val mockTask = MockTask(mockAuthResult)

        Mockito.`when`(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)


        val result = authManager.loginWithEmailAndPassword(email, password)


        assert(result is AuthRes.Success)
        assert((result as AuthRes.Success).data == mockUser)
    }

    @Test
    fun `loginWithEmailAndPassword should return Error when login fails`() = runTest {

        val email = "test@example.com"
        val password = "wrongPassword"
        val errorMessage = "Login failed"


        val mockTask = MockTask<AuthResult>(isSuccessful = false, exception = Exception(errorMessage))
        Mockito.`when`(mockAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)


        val result = authManager.loginWithEmailAndPassword(email, password)


        assert(result is AuthRes.Error)
        assert((result as AuthRes.Error).errorMessage == errorMessage)
    }

    @Test
    fun `registerWithEmailAndPassword should return Success when registration is successful`() = runTest {

        val email = "test@example.com"
        val password = "password"
        val mockAuthResult = mock<AuthResult> {
            on { user } doReturn mockUser
        }
        val mockTask = MockTask(mockAuthResult)

        Mockito.`when`(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)


        val result = authManager.registerWithEmailAndPassword(email, password)


        assert(result is AuthRes.Success)
        assert((result as AuthRes.Success).data == mockUser)
    }

    @Test
    fun `registerWithEmailAndPassword should return Error when registration fails`() = runTest {

        val email = "test@example.com"
        val password = "password"
        val errorMessage = "Registration failed"


        val mockTask = MockTask<AuthResult>(isSuccessful = false, exception = Exception(errorMessage))
        Mockito.`when`(mockAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)


        val result = authManager.registerWithEmailAndPassword(email, password)


        assert(result is AuthRes.Error)
        assert((result as AuthRes.Error).errorMessage == errorMessage)
    }

    @Test
    fun `recuperarClave should return Success when email is sent successfully`() = runTest {

        val email = "test@example.com"
        val mockTask = MockTask<Void>(null)


        Mockito.`when`(mockAuth.sendPasswordResetEmail(email)).thenReturn(mockTask)


        val result = authManager.recuperarClave(email)

        assert(result is AuthRes.Success)
    }
}




class MockTask<T>(
    private val result: T? = null,
    private val isSuccessful: Boolean = true,
    private val exception: Exception? = null
) : Task<T>() {

    private val successListeners = mutableListOf<OnSuccessListener<in T>>()
    private val failureListeners = mutableListOf<OnFailureListener>()

    override fun isComplete(): Boolean = true

    override fun isSuccessful(): Boolean = isSuccessful

    override fun getResult(): T? {
        if (isSuccessful) {
            return result
        } else {
            throw exception ?: Exception("Task not successful")
        }
    }

    override fun <X : Throwable?> getResult(exceptionType: Class<X>): T {
        if (isSuccessful) {
            return result ?: throw Exception("Result is null")
        } else {
            throw exception ?: Exception("Task not successful")
        }
    }

    override fun isCanceled(): Boolean = false // Assuming it can't be canceled

    override fun getException(): Exception? {
        return if (!isSuccessful) exception else null
    }

    override fun addOnSuccessListener(listener: OnSuccessListener<in T>): Task<T> {
        successListeners.add(listener)
        if (isSuccessful) {
            listener.onSuccess(result)
        }
        return this
    }

    override fun addOnFailureListener(listener: OnFailureListener): Task<T> {
        failureListeners.add(listener)
        if (!isSuccessful) {
            listener.onFailure(exception ?: Exception("Error en Task"))
        }
        return this
    }

    override fun addOnFailureListener(executor: Executor, listener: OnFailureListener): Task<T> {
        return addOnFailureListener(listener)
    }

    override fun addOnSuccessListener(executor: Executor, listener: OnSuccessListener<in T>): Task<T> {
        return addOnSuccessListener(listener)
    }

    override fun addOnSuccessListener(activity: Activity, listener: OnSuccessListener<in T>): Task<T> {
        return addOnSuccessListener(listener)
    }

    override fun addOnFailureListener(activity: Activity, listener: OnFailureListener): Task<T> {
        return addOnFailureListener(listener)
    }
}