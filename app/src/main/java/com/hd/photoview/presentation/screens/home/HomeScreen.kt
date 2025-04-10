package com.hd.photoview.presentation.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
        topBar = { if(type.intValue == 1){  TopBrSearch(onEvent = onEvent, query, type)} else { TopBrNormal(
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
                    GridListImages(photos = photos , type, query, onEvent = onEvent, toDetail = toDetail)
                }
            }
        }
    }
}

@Composable
fun GridListImages(
    photos: LazyPagingItems<Photo>,
    type: MutableIntState,
    query: MutableState<String>,
    onEvent: (HomeScreenEvents) -> Unit,
    toDetail: (photo: Photo) -> Unit
) {
    val lazyGridState = rememberLazyGridState()


    val isLoading = photos.loadState.append is LoadState.Loading

    LaunchedEffect(lazyGridState) {
        snapshotFlow { lazyGridState.firstVisibleItemIndex }
            .collect { index ->
                if (index >= photos.itemCount - 5) {
                    if (type.intValue == 2) {
                        onEvent(HomeScreenEvents.LoadPhoto)
                    } else if (type.intValue == 1) {
                        onEvent(HomeScreenEvents.SearchPhoto(query = query.value))
                    }
                }
            }
    }

    LazyVerticalGrid(
        state = lazyGridState,
        contentPadding = PaddingValues(4.dp),
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(photos.itemCount, key = { index -> photos[index]?.id ?: index }) { index ->
            val photo = photos[index]
            photo?.let {

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(300)) +
                            expandVertically(animationSpec = tween(300))
                ) {
                    ImageItem(photo.small, photo, toDetail = toDetail)
                }
            }
        }

        if (isLoading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
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
fun TopBrSearch(onEvent: (HomeScreenEvents) -> Unit , query: MutableState<String>, type: MutableIntState){
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }

    TopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable { type.intValue = 2 }
            )
        },
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp) // spacing from trailing edge
            ) {
                TextField(
                    value = query.value,
                    onValueChange = { newText -> query.value = newText },
                    placeholder = { Text("Search...") },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F0F0),
                        unfocusedContainerColor = Color(0xFFF0F0F0),
                        disabledContainerColor = Color(0xFFE0E0E0),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp) // match Material style
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onEvent.invoke(HomeScreenEvents.SearchPhoto(query.value))
                        }
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = MaterialTheme.colorScheme.surface)
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