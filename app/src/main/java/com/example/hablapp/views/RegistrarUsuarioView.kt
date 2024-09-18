package com.example.hablapp.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hablapp.R
import com.example.hablapp.components.AppClickeableText
import com.example.hablapp.components.AppPasswordTextField
import com.example.hablapp.components.AppTextField
import com.example.hablapp.components.TitleTextComponent
import com.example.hablapp.core.RouterManager
import com.example.hablapp.core.SnackbarController
import com.example.hablapp.utils.AuthManager
import com.example.hablapp.utils.AuthRes
import kotlinx.coroutines.launch


@Composable
fun RegistrarUsuarioView(
    routerManager: RouterManager,
    snackController: SnackbarController,
    authManager: AuthManager
) {
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    val password = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(28.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TitleTextComponent(text = stringResource(id = R.string.app_register_title))
            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                text = stringResource(id = R.string.app_email_placeholder),
                value = email.value,
                onValuechange = { value -> email.value = value })
            Spacer(modifier = Modifier.height(10.dp))
            AppPasswordTextField(
                value = password.value,
                onValuechange = { value -> password.value = value })
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                scope.launch {
                    register(email.value, password.value, authManager, keyboard, context, snackController, routerManager)
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.app_register_button))
            }
            Spacer(modifier = Modifier.height(10.dp))
            AppClickeableText(
                initialText = stringResource(id = R.string.ya_tienes_cuenta),
                annotatedText = stringResource(id = R.string.ingresa_aqui),
                onTextSelected = { routerManager.onNavigateToLogin() }
            )
        }
    }
}

private suspend fun register(
    email: String,
    password: String,
    authManager: AuthManager,
    keyboard: SoftwareKeyboardController?,
    context: Context,
    snackController: SnackbarController,
    routerManager: RouterManager
) {
    if(email.isNotEmpty() && password.isNotEmpty()){
        keyboard?.hide()
        when(val result = authManager.registerWithEmailAndPassword(email, password)){
            is AuthRes.Success -> {
                snackController.show(context.resources.getString(R.string.cuenta_creada_exitosa))
                routerManager.onNavigateToLogin()
            }
            is AuthRes.Error -> {
                snackController.show("Error: ${result.errorMessage}")
            }
        }
    } else {
        snackController.show(context.resources.getString(R.string.campos_vacios))
    }




}
