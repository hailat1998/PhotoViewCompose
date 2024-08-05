package com.hd.photoview.presentation.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.hd.photoview.R
import com.hd.photoview.domain.model.Photo


@Composable
fun HomeScreen(
    photos: LazyPagingItems<Photo>,
    type: MutableIntState,
    query: MutableState<String>,
    onEvent: (HomeScreenEvents) -> Unit,
    toDetail: (photo: Photo) -> Unit
) {

    val context = LocalContext.current as Activity

   BackHandler {
       if(type.intValue == 1){
           type.intValue = 2
       }else{
          context.finish()
       }
   }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { if(type.intValue == 1){  TopBrSearch(onEvent = onEvent, query)} else { TopBrNormal(
            type = type
        ) } }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if ( photos.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator()
                } else if (photos.itemCount == 0) {
                    Text(text = "No images found")
                } else {
                    GridListImages(photos = photos , onEvent = onEvent, toDetail = toDetail)
                }
            }
        }

    }
}

@Composable
fun GridListImages(
    photos: LazyPagingItems<Photo>,
    onEvent: (HomeScreenEvents) -> Unit,
    toDetail: (photo: Photo) -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(lazyGridState) {
        if (!lazyGridState.canScrollForward) {
            onEvent(HomeScreenEvents.LoadPhoto)
        }
    }

    LazyVerticalGrid(
        state = lazyGridState,
        contentPadding = PaddingValues(4.dp),
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(photos.itemCount) { index ->
            val photo = photos[index]
            photo?.let {
                ImageItem(photo.small , photo ,toDetail = toDetail)
            }
        }
    }
}


@Composable
fun ImageItem(url: String , photo: Photo ,  toDetail: (photo: Photo) -> Unit) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
            .clickable { toDetail.invoke(photo) }            ,
    ) {

        AsyncImage(model = url, contentDescription = null,
            contentScale = ContentScale.FillWidth ,
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                )
          }
  }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDialog(
    photo: MutableState<Photo>,
    onDismissRequest: () -> Unit,
    onEvent: (HomeScreenEvents) -> Unit
) {
    var selected by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val menuItems = listOf("full", "regular", "small")

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Photo")
        },
        text = {
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

                    Icon(painterResource(id = R.drawable.baseline_download_24) , null,
                        modifier = Modifier
                            .clickable {
                                onEvent.invoke(
                                    HomeScreenEvents.Download(
                                        photo.value,
                                        selected
                                    )
                                )
                            }
                            .padding(12.dp))
                    Icon(painterResource(id = R.drawable.public_24px) ,
                        null, tint = Color.Black, modifier = Modifier.padding(12.dp)
                       )
                }

                AsyncImage(
                    model= photo.value.small,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                )
            }
        },
        confirmButton = {
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBrSearch(onEvent: (HomeScreenEvents) -> Unit , query: MutableState<String>){
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }

    TopAppBar(
        title = {
            TextField(
                value = query.value,
                onValueChange = { newText -> query.value = newText },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions ( onSearch = {
                               onEvent.invoke(HomeScreenEvents.SearchPhoto(query.value))
                           }
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBrNormal(type: MutableIntState){
    TopAppBar(title = { Text( text = "Unsplash") },
        actions = {Icon(Icons.Default.Search , 
            null,
            modifier = Modifier.clickable { type.value = 1  }
            )
        }
    )
}