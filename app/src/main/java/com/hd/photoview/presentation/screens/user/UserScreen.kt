package com.hd.photoview.presentation.screens.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.theme.PhotoViewTheme
import com.hd.photoview.presentation.utils.ImageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.UserScreen(photosData: Flow<PagingData<Photo>>,
                                     photo: Photo,
                                     animatedVisibilityScope: AnimatedVisibilityScope,
                                     toDetail: (photo: Photo) -> Unit){

    val photos = photosData.collectAsLazyPagingItems()

    val lazyGridState = rememberLazyGridState()

    val isLoading = photos.loadState.append is LoadState.Loading

    Scaffold(topBar = { TopBarUser(photo, animatedVisibilityScope) },
             modifier = Modifier.fillMaxSize()) { innerPadding ->

        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            LazyVerticalGrid(
                state = lazyGridState,
                contentPadding = PaddingValues(4.dp),
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    photos.itemCount,
                    key = { index -> photos[index]?.id ?: index }
                ) { index ->
                    val photo = photos[index]
                    photo?.let {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(300)) +
                                    expandVertically(animationSpec = tween(300))
                        ) {
                            ImageItem(photo.small, photo, toDetail = toDetail, animatedVisibilityScope)
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
   }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TopBarUser(photo: Photo, animatedVisibilityScope: AnimatedVisibilityScope) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${photo.username} photos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState("text/${photo.username}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 700)
                        }
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview()
@Composable
fun UserScreenPreview() {
    PhotoViewTheme {
        SharedTransitionLayout {
            val sharedTransitionScope = this

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                val emptyFlow = remember { flowOf(PagingData.empty<Photo>()) }

                val samplePhoto = Photo(
                    id = "abc123",
                    small = "https://example.com/photo_small.jpg",
                    full = "https://example.com/photo_full.jpg",
                    regular = "https://example.com/photo_regular.jpg",
                    description = "A scenic mountain view during sunset.",
                    username = "nature_lover42"
                )

                with(sharedTransitionScope) {
                    UserScreen(
                        photosData = emptyFlow,
                        photo = samplePhoto,
                        animatedVisibilityScope = this@AnimatedVisibility,
                        toDetail = {}
                    )
                }
            }
        }
    }
}