package com.example.hablapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hablapp.R
import com.example.hablapp.core.RouterManager
import com.example.hablapp.core.SnackbarController
import com.example.hablapp.core.fechaConHora
import com.example.hablapp.models.Nota
import com.example.hablapp.utils.AuthManager
import com.example.hablapp.utils.NotasDBManager
import java.util.Date

@Composable
fun NotaDetalleView(
    routerManager: RouterManager,
    snackController: SnackbarController,
    notasDbManager: NotasDBManager,
    authManager: AuthManager,
    nota: Nota?
) {
    val (titulo, setTitulo) = remember {
        mutableStateOf(nota?.titulo ?: "")
    }
    val (descripcion, setDescripcion) = remember {
        mutableStateOf(nota?.descripcion ?: "")
    }

    val (fecha, setFecha) = remember {
        mutableStateOf(nota?.fechaCreacion ?: Date())
    }

    LaunchedEffect(nota) {
        nota?.let {
            setTitulo(it.titulo)
            setDescripcion(it.descripcion)
            setFecha(it.fechaCreacion)
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
                .fillMaxSize()
                .padding(28.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            if (nota != null) {
                Button(
                    onClick = {

                        notasDbManager.eliminarNota(nota.key ?: "")
                        routerManager.onNavigateToNotas()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.eliminar_nota))
                }
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = fechaConHora(fecha), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(20.dp))
            }




            TextField(
                value = titulo,
                onValueChange = {newVal -> setTitulo(newVal.take(20))},
                label = { Text(text = stringResource(id = R.string.titulo_nota_lbl))},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            TextField(
                value = descripcion,
                onValueChange = {newVal -> setDescripcion(newVal)},
                label = { Text(text = stringResource(id = R.string.descripcion_nota_lbl))},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 16.dp),
                maxLines = Int.MAX_VALUE,
                singleLine = false
            )
            Button(
                onClick = {

                    val userId = authManager.getCurrentUser()?.uid ?: ""
                    val newNota = Nota(titulo = titulo, descripcion = descripcion, fechaCreacion = Date(), userId = userId);

                    if(nota != null) {
                        newNota.key = nota.key
                        newNota.fechaCreacion = nota.fechaCreacion
                        notasDbManager.actualizarNota(newNota.key?: "", newNota)
                    }
                    else {
                        notasDbManager.agregarNota(newNota)
                    }

                    routerManager.onNavigateToNotas()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.guardar_nota))
            }

        }
    }
}