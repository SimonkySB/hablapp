package com.example.hablapp.core

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarController(private val snackbarHostState: SnackbarHostState, private val coroutineScope: CoroutineScope) {


    fun show(
        message: String,
        duration: SnackbarDuration = SnackbarDuration.Short,

    ) {
        coroutineScope.launch {
            val action = snackbarHostState.showSnackbar(
                message = message,
                duration = duration,
                withDismissAction = true
            )

            when (action) {
                SnackbarResult.Dismissed -> {

                }
                SnackbarResult.ActionPerformed -> {

                }
            }
        }
    }
}