package com.vladusecho.xnews.presentation.mySwiper

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.presentation.model.Article

enum class SwipeActionType {
    ADD_TO_FAVOURITE, DELETE_FROM_FAVOURITE
}


@Composable
fun MySwiper(
    article: Article,
    swipeActionType: SwipeActionType,
    onAction: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var itemWidth by remember { mutableFloatStateOf(0f) }
    val threshold = remember(itemWidth) { itemWidth * 0.17f }

    val (icon, contentDescription) = when (swipeActionType) {
        SwipeActionType.ADD_TO_FAVOURITE -> Pair(
            Icons.Default.Favorite,
            "Добавить в избранное"
        )
        SwipeActionType.DELETE_FROM_FAVOURITE -> Pair(
            Icons.Default.Delete,
            "Удалить"
        )
    }

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
                icon,
                contentDescription,
                modifier = Modifier.padding(end = 20.dp)
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            offsetX =
                                (offsetX + dragAmount).coerceIn(-itemWidth + itemWidth * 0.8f, 0F)
                            change.consume()
                        },
                        onDragEnd = {
                            if (-offsetX > threshold) {
                                onAction()
                                if(swipeActionType == SwipeActionType.ADD_TO_FAVOURITE) {
                                    offsetX = 0F
                                }
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