package com.vladusecho.xnews.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.presentation.MainViewModel

@Composable
fun FavouriteScreen(
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {

    val favouriteArticlesState = viewModel.getFavouriteArticles().collectAsState(listOf())
    val favouriteList = favouriteArticlesState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(paddingValues)
            .border(BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSecondary))
    ) {
        LazyColumn {
            items(items = favouriteList,
                key = { it.id })
            {
                Box(
                    modifier = Modifier.animateItem()
                ) {
                    MySwipeToDismiss(
                        article = it
                    ) {
                        viewModel.deleteFromFavourite(it.id)
                    }
                }
            }
        }
    }
}

@Composable
fun MySwipeToDismiss(
    article: Article,
    onDelete: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var itemWidth by remember { mutableFloatStateOf(0f) }
    val deleteThreshold = remember(itemWidth) { itemWidth * 0.17f }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { itemWidth = it.width.toFloat() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
                .clip(CircleShape.copy(CornerSize(10.dp)))
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                Icons.Default.Delete,
                "Удалить",
                modifier = Modifier.padding(end = 20.dp)
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(-itemWidth + itemWidth * 0.8f, 0F)
                            change.consume()
                        },
                        onDragEnd = {
                            if (-offsetX > deleteThreshold) {
                                onDelete()
                            } else {
                                offsetX = 0f
                            }
                        }
                    )
                }
        ) {
            Article(article = article)
        }
    }
}