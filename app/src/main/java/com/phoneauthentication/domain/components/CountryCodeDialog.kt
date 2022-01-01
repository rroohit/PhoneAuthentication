package com.phoneauthentication.domain.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.phoneauthentication.domain.models.CCPCountry
import com.phoneauthentication.domain.models.DEFAULT_FLAG_RES
import com.phoneauthentication.domain.models.getFlagMasterResID

@Composable
fun CountryCodeDialog(
    modifier: Modifier = Modifier,
    flagOnly: Boolean = false,
    initialCountry: CCPCountry = CCPCountry("in", "+91", "IN", DEFAULT_FLAG_RES),
    //pickedCountry: (CCPCountry) -> Unit
) {
    val picked = remember { mutableStateOf(initialCountry) }

    MaterialTheme {
        Column(modifier = modifier) {
            val openDialog = remember { mutableStateOf(false) }
            val dialogWidth = 250.dp
            val dialogHeight = 400.dp

            Row(
                Modifier.clickable {
                    openDialog.value = false
                },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id = getFlagMasterResID()
                    ), contentDescription = null
                )
                if (!flagOnly) {
                    Text(
                        picked.value.name,
                        Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)
                    )
                    Text(
                        picked.value.phoneCode,
                        Modifier.padding(0.dp, 0.dp, 24.dp, 0.dp)
                    )
                }

                //Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }

            if (openDialog.value) {
                Dialog(onDismissRequest = { openDialog.value = false }) {
                    Box(
                        Modifier
                            .size(dialogWidth, dialogHeight)
                            .background(Color.White)
                    ) {
                        LazyColumn {
                            items(0) { _ ->
                                Row(
                                    Modifier
                                        .padding(
                                            horizontal = 18.dp,
                                            vertical = 18.dp
                                        )
                                        .clickable {
                                            //pickedCountry()
                                            //picked.value = countryList[index]
                                            openDialog.value = false
                                        }) {
                                    Image(
                                        painter = painterResource(
                                            id = getFlagMasterResID()
                                        ), contentDescription = null
                                    )
                                    Text(
                                        "IN",
                                        Modifier.padding(horizontal = 18.dp)
                                    )

                                }

                            }

                        }

                    }

                }

            }

        }

    }

}
