package com.example.hablapp.views

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hablapp.R
import com.example.hablapp.components.AppClickeableText
import com.example.hablapp.components.AppPasswordTextField
import com.example.hablapp.components.AppTextField
import com.example.hablapp.components.TitleTextComponent
import com.example.hablapp.core.RouterManager
import com.example.hablapp.core.SnackbarController
import com.example.hablapp.services.UsuariosService


@Composable
fun LoginView(
    routerManager: RouterManager,
    snackController: SnackbarController,
    usuariosService: UsuariosService
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val username = remember {
        mutableStateOf("user1")
    }
    val password = remember {
        mutableStateOf("password1")
    }

    val onUsernameValueChange: (value: String) -> Unit = {
        value -> username.value = value
    }

    val onPasswordValueChange: (value: String) -> Unit = {
            value -> password.value = value
    }

    val onLoginClick: () -> Unit = {
        keyboard?.hide()

        if(usuariosService.login(username = username.value, password = password.value)) {
            snackController.show(context.resources.getString(R.string.login_exitoso))
            username.value = ""
            password.value = ""
            routerManager.onNavigateToNotas()
        }
        else {
            snackController.show(context.resources.getString(R.string.login_invalid))
        }
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
            TitleTextComponent(text = stringResource(id = R.string.app_login_title))
            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                text = stringResource(id = R.string.app_username_placeholder),
                value = username.value,
                onValuechange = { value -> onUsernameValueChange(value) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            AppPasswordTextField(
                value = password.value,
                onValuechange = { value -> onPasswordValueChange(value) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppClickeableText(
                initialText = stringResource(id = R.string.olvidaste_tu_clave),
                annotatedText = stringResource(id = R.string.recupera_tu_clave),
                onTextSelected = {routerManager.onNavigateToRecuperarClave()},
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onLoginClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.app_login_button))
            }


            Spacer(modifier = Modifier.height(10.dp))
            AppClickeableText(
                initialText =  stringResource(id = R.string.no_tienes_cuenta),
                annotatedText = stringResource(id = R.string.registrate_aqui),
                onTextSelected = { routerManager.onNavigateToRegistro() }
            )
        }
    }

}


