package com.hd.photoview.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.hd.photoview.data.remote.dto.PhotoItem


@Composable
fun HomeScreen(state : HomeScreenState, onEvent: (HomeScreenEvents) -> Unit){
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()){
            Button(onClick = { onEvent(HomeScreenEvents.LoadPhoto)  }) {
                Text(text = "Click")
            }
            Box(modifier= Modifier
                .padding(it)
                .fillMaxSize(),
                contentAlignment = Alignment.Center){
                if(state.isLoading){
                    CircularProgressIndicator()
                }else if(state.listPhoto.isEmpty()){
                    Text(text = "No images found")
                }else{
                    GridListImages(list = state.listPhoto)
                }
            }
        }
    }
}

@Composable
fun GridListImages(list : List<PhotoItem>){
    LazyVerticalGrid(
        contentPadding = PaddingValues(4.dp),
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(list.size, {img -> list[img].id}) { img ->
            ImageItem(list[img].urls.small)
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

//        Image(
//            painter = rememberAsyncImagePainter(model = url),
//            contentDescription = "",
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(170.dp)
//        )
        AsyncImage(model = url, contentDescription = null,
            contentScale = ContentScale.FillWidth ,
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp))
    }

}


