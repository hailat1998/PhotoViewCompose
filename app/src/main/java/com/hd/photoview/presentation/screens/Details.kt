package com.hd.photoview.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hd.photoview.R
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.screens.home.HomeScreenEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetail(photo: Photo, onEvent: (HomeScreenEvents) -> Unit){
    var selected by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val menuItems = listOf("full", "regular", "small")
Surface {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Text(text = "Quality")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selected,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor(),
                    textStyle = MaterialTheme.typography.titleMedium
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    menuItems.forEach { q ->
                        DropdownMenuItem(
                            text = { Text(text = q) },
                            onClick = {
                                selected = q
                                expanded = false
                            }
                        )
                    }
                }
            }

            Icon(
                painterResource(id = R.drawable.baseline_download_24) , null,
                modifier = Modifier
                    .clickable { onEvent.invoke(HomeScreenEvents.Download(photo, selected)) }
                    .padding(12.dp))
            Icon(
                painterResource(id = R.drawable.public_24px) ,
                null, tint = Color.Black, modifier = Modifier.padding(12.dp)
            )
        }

        AsyncImage(
            model= photo.small,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
        )
    }
  }
}