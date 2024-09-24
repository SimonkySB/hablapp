package com.example.hablapp.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hablapp.R
import com.example.hablapp.core.RouterManager
import com.example.hablapp.core.SnackbarController
import com.example.hablapp.utils.fechaConHora
import com.example.hablapp.models.Nota
import com.example.hablapp.utils.AuthManager
import com.example.hablapp.utils.NotasDBManager
import org.w3c.dom.Text
import java.util.Date
import java.util.Locale

@Composable
fun NotaDetalleView(
    routerManager: RouterManager,
    notasDbManager: NotasDBManager,
    nota: Nota?
) {

    val context = LocalContext.current

    val (titulo, setTitulo) = remember {
        mutableStateOf(nota?.titulo ?: "")
    }
    val (descripcion, setDescripcion) = remember {
        mutableStateOf(nota?.descripcion ?: "")
    }

    val (fecha, setFecha) = remember {
        mutableStateOf(nota?.fechaCreacion ?: Date())
    }

    val textToSpeech = remember {
        mutableStateOf<TextToSpeech?>(null)
    }

    val tituloLaucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result -> setTitulo(getResult(result)) }
    val descLaucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result -> setDescripcion(getResult(result)) }



    fun iniciarGrabacionTitulo() {
        tituloLaucher.launch(crearIntent())
    }

    fun iniciarGrabacionDescripcion() {
        descLaucher.launch(crearIntent())
    }



    LaunchedEffect(Unit) {
        textToSpeech.value = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.value?.setLanguage(Locale.getDefault())
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech.value?.stop()
            textToSpeech.value?.shutdown()
        }
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
                IconButton(
                    onClick = {
                        routerManager.onNavigateToNotas()
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                if (nota != null) {
                    IconButton(
                        onClick = {
                            notasDbManager.eliminarNota(nota.key ?: "")
                            routerManager.onNavigateToNotas()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }

            }



            if (nota != null) {
                Text(text = fechaConHora(fecha), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(20.dp))
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = titulo,
                    onValueChange = {newVal -> setTitulo(newVal.take(20))},
                    label = { Text(text = stringResource(id = R.string.titulo_nota_lbl))},
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp)
                )
                PlayButton(titulo, textToSpeech.value)
                RecordButton { iniciarGrabacionTitulo() }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                TextField(
                    value = descripcion,
                    onValueChange = {newVal -> setDescripcion(newVal)},
                    label = { Text(text = stringResource(id = R.string.descripcion_nota_lbl))},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 16.dp)
                        .heightIn(min = 100.dp, max = 200.dp),
                    maxLines = Int.MAX_VALUE,

                    singleLine = false
                )
                PlayButton(descripcion, textToSpeech.value)
                RecordButton { iniciarGrabacionDescripcion() }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {

                    val newNota = Nota(titulo = titulo, descripcion = descripcion, fechaCreacion = fecha);
                    if(nota != null) { newNota.key = nota.key }
                    notasDbManager.guardarNota(newNota)

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

@Composable
fun PlayButton(value: String, textToSpeech: TextToSpeech?) {
    IconButton(
        onClick = {
            if(value.trim().isNotEmpty()) {
                textToSpeech?.speak(value, TextToSpeech.QUEUE_FLUSH, null, null)
            }

        },
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "Play"
        )
    }
}

@Composable
fun RecordButton(action: () -> Unit) {
    IconButton(
        onClick = { action() },
    ) {
        Icon(
            imageVector = Icons.Filled.RecordVoiceOver,
            contentDescription = "Record"
        )
    }
}


fun getResult(result: ActivityResult): String {
    if (result.resultCode == Activity.RESULT_OK) {
        val data = result.data
        val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        if (!results.isNullOrEmpty()) {
            return results[0]
        }
    }
    return ""
}

fun crearIntent(): Intent {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")
    }
    return intent;
}