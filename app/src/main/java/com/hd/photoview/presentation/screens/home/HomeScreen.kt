package com.hd.photoview.presentation.screens.home

import Urls
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.hd.photoview.data.remote.dto.PhotoItem
import com.hd.photoview.R


@Composable
fun HomeScreen(
    photos: LazyPagingItems<PhotoItem>,
    onEvent: (HomeScreenEvents) -> Unit
) {
    val photoState = remember{ mutableStateOf(PhotoItem(urls = Urls("", " ", ""), null , "12345" )) }
    val showDialog = remember { mutableStateOf(false) }
    val isInSearch = remember { mutableStateOf(false ) }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { if(isInSearch.value){  TopBrSearch(onEvent = onEvent, isInSearch = isInSearch)} else { TopBrNormal(
            isInSearch = isInSearch
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
                    GridListImages(photos = photos, photoState, showDialog , onEvent = onEvent)
                }
            }
        }
        if(showDialog.value){
            PhotoDialog(photoItem = photoState, onDismissRequest = { showDialog.value = false }, onEvent = onEvent)
        }
    }
}

@Composable
fun GridListImages(
    photos: LazyPagingItems<PhotoItem>,
    photoState: MutableState<PhotoItem>,
    showDialog: MutableState<Boolean>,
    onEvent: (HomeScreenEvents) -> Unit,
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
                ImageItem(photo.urls.small  ){
                     photoState.value = photo
                    showDialog.value = true
                }
            }
        }
    }
}


@Composable
fun ImageItem(url: String ,  showDialog:() -> Unit ) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
    ) {

        AsyncImage(model = url, contentDescription = null,
            contentScale = ContentScale.FillWidth ,
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clickable { showDialog.invoke() })


      }
  }



@Composable
fun PhotoDialog(
   photoItem: MutableState<PhotoItem> ,
    onDismissRequest: () -> Unit,
    onEvent: (HomeScreenEvents) -> Unit
) {
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
                Row(horizontalArrangement = Arrangement.End){
                    Icon(painterResource(id = R.drawable.baseline_download_24) , null)
                    Icon(painterResource(id = R.drawable.public_24px) ,
                        null, tint = Color.Black,
                        modifier = Modifier.
                          clickable { onEvent.invoke(HomeScreenEvents.Download(photoItem.value.urls.full)) })
                }
                AsyncImage(
                    model= photoItem.value.urls.full,
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
fun TopBrSearch(onEvent: (HomeScreenEvents) -> Unit , isInSearch: MutableState<Boolean>){
    val focusRequester = remember { FocusRequester() }
    var searchText by remember{ mutableStateOf("") }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }

    TopAppBar(
        title = {
            TextField(
                value = searchText,
                onValueChange = { newText -> searchText = newText },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions ( onSearch = {
                               onEvent.invoke(HomeScreenEvents.SearchPhoto(searchText))
                                   isInSearch.value = false
                       }
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBrNormal(isInSearch: MutableState<Boolean>){
    TopAppBar(title = { Text( text = "Unsplash")},
        actions = {Icon(Icons.Default.Search , 
            null,
            modifier = Modifier.clickable { isInSearch.value = true  })})
}