package com.example.hablapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.hablapp.core.MyNavHost
import com.example.hablapp.core.SnackbarController
import com.example.hablapp.ui.theme.HablappTheme
import kotlinx.coroutines.CoroutineScope


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            val snackbarController = remember { SnackbarController(snackbarHostState, coroutineScope) }
            HablappTheme(darkTheme = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    MyNavHost(
                        modifier = Modifier.padding(innerPadding),
                        snackController = snackbarController
                    )
                }
            }
        }
    }
}


