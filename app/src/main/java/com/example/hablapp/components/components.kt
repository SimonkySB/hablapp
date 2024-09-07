package com.example.hablapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hablapp.R
import com.example.hablapp.ui.theme.Purple40

@Composable
fun TitleTextComponent(text: String, fontSize: TextUnit = 32.sp, leftIcon: @Composable (() -> Unit)? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ){

        Box(
            modifier = Modifier
                .width(50.dp)
                .align(Alignment.CenterVertically)
        ){
            if (leftIcon != null) {
                leftIcon()
            }

        }

        Box(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)

        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Normal,
                lineHeight = 40.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .width(50.dp)
                .align(Alignment.CenterVertically)
        ){

        }

    }

}

@Composable
fun AppTextField(text: String, value: String,  onValuechange: (String) -> Unit){

    OutlinedTextField(
        label = {Text(text = text)},
        value = value,
        onValueChange = { onValuechange(it) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AppPasswordTextField(value: String, onValuechange: (String) -> Unit){

    val passwordVisibility = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        label = {Text(text = stringResource(id = R.string.app_password_placeholder))},
        value = value,
        onValueChange = { onValuechange(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            val iconImage = if(passwordVisibility.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(imageVector = iconImage, contentDescription = "")
            }
        },
        visualTransformation = if(passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}


@Composable
fun AppClickeableText(
    initialText: String,
    annotatedText: String,
    onTextSelected: (String) -> Unit,
    textAlign: TextAlign = TextAlign.Center
) {

    val str = buildAnnotatedString {
        append(initialText)
        append("  ")
        withStyle(style = SpanStyle(color = Purple40)) {
            pushStringAnnotation(tag = annotatedText, annotation = annotatedText)
            append(annotatedText)
        }
    }
    ClickableText(
        text = str,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            textAlign = textAlign
        ),
        onClick = { offset ->
            str.getStringAnnotations(offset, offset).firstOrNull()?.also { span ->
                if(span.item == annotatedText) {
                    onTextSelected(span.item)
                }
            }
        })
}