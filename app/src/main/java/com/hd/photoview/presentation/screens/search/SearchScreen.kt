package com.hd.photoview.presentation.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.hd.photoview.R
import com.hd.photoview.domain.model.Photo
import com.hd.photoview.presentation.utils.ImageItem
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchScreen(photosData: Flow<PagingData<Photo>>,
                                       toDetail: (photo: Photo) -> Unit,
                                       animatedVisibilityScope: AnimatedVisibilityScope,
                                       query: MutableState<String>,
                                       onSearch: () -> Unit,
                                       onBack: () -> Unit) {

    val photos = photosData.collectAsLazyPagingItems()

    val lazyGridState = rememberLazyGridState()

    val isLoading = photos.loadState.append is LoadState.Loading

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBrSearch(query, onSearch, onBack)
        }
    ) {  innerPadding ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBrSearch(query: MutableState<String>, onSearch: () -> Unit, onBack: () -> Unit){

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
                    .clickable {
                        onBack.invoke()
                    }
            )
        },
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp)
            ) {
                BasicTextField(
                    value = query.value,
                    onValueChange = { newText -> query.value = newText },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        baselineShift = BaselineShift.None
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch.invoke()
                        }
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (query.value.isEmpty()) {
                                Text(
                                    text = "Search...",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp, // Match the input text size
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}


