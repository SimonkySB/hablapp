package com.example.hablapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hablapp.R
import com.example.hablapp.components.TitleTextComponent
import com.example.hablapp.core.RouterManager
import com.example.hablapp.core.SnackbarController
import com.example.hablapp.utils.fechaConHora
import com.example.hablapp.models.Nota
import com.example.hablapp.utils.AuthManager
import com.example.hablapp.utils.NotasDBManager

@Composable
fun NotasView(
    routerManager: RouterManager,
    snackController: SnackbarController,
    notasDbManager: NotasDBManager,
    authManager: AuthManager
) {

    val notas by notasDbManager.obtenerNotas().collectAsState(emptyList())

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
        ){

            Spacer(modifier = Modifier.height(20.dp))
            TitleTextComponent(
                text = stringResource(id = R.string.notas_title),
                rightIcon = {
                    IconButton(
                        onClick = {
                            authManager.signOut()
                            routerManager.onNavigateToLogin()
                        },
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp)
                            .align(Alignment.Start)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color.Red
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    routerManager.onNavigateToNotaDetalle("0")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.agrega_notas))
            }
            Spacer(modifier = Modifier.height(20.dp))



            notas.forEach {nota ->
                NotaItem(nota = nota, onClick = {
                    routerManager.onNavigateToNotaDetalle(nota.key?:"")
                })
                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}

@Composable
fun NotaItem(nota: Nota, onClick: () -> Unit){

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = nota.titulo, style = MaterialTheme.typography.titleMedium)
            Text(text = fechaConHora(nota.fechaCreacion), style = MaterialTheme.typography.titleSmall)
        }

    }


}