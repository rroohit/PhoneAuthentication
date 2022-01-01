package com.phoneauthentication.domain.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
fun PhoneNumTextField(
    text: String = "",
    hint: String = "",
    maxLength: Int = 10,
    errorMsg: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle()
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                if (it.length <= maxLength) {
                    onTextChange(it.filter { str ->
                        !str.isWhitespace()
                    })
                }
            },
            placeholder = {
                Text(
                    text = if (hint.isEmpty()) "" else hint,
                    style = MaterialTheme.typography.body1
                )
            },
            textStyle = textStyle,
            isError = errorMsg != "",
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
        )

        if (errorMsg.isNotEmpty()) {
            Text(
                text = errorMsg,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End

            )
        }

    }


}