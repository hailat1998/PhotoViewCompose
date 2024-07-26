package com.hd.photoview.presentation.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    Scaffold(modifier = Modifier.fillMaxSize()) {
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
                    GridListImages(photos = photos, onEvent = onEvent)
                }
            }
        }
    }
}

@Composable
fun GridListImages(
    photos: LazyPagingItems<PhotoItem>,
    onEvent: (HomeScreenEvents) -> Unit
) {
    val lazyGridState = rememberLazyGridState()
    // Use LaunchedEffect to call onEvent when the grid can't scroll forward
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
                ImageItem(photo.urls.small)
            }
        }
    }
}


@Composable
fun ImageItem(url: String) {
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
                .height(170.dp))
    }
  }



@Composable
fun PhotoDialog(
    photoUrl: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
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
                    Icon(painterResource(id = R.drawable.baseline_download_24) , null)
                }
                AsyncImage(
                   model= photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Close")
            }
        }
    )
}

