package com.vladusecho.xnews.presentation

import android.graphics.fonts.Font
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vladusecho.xnews.ui.theme.XNewsTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import coil3.compose.AsyncImage
import com.vladusecho.xnews.data.models.ArticleDto

@Composable
fun MainScreen() {

    val viewModel: MainViewModel = viewModel()

    val screenState = viewModel.state.collectAsState(MainState.Initial)
    val currentState = screenState.value

    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                label = {Text(text = "Поиск")}
            )
            OutlinedButton(
                onClick = {
                    viewModel.loadArticles(text)
                },
            ) {
                Text(
                    text = "Загрузить..."
                )
            }
            when(currentState) {
                is MainState.Content -> {
                    LazyColumn {
                        items(currentState.articles) {
                            Article(it)
                        }
                    }
                }
                MainState.Initial -> {

                }
                MainState.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun Article(
    article: ArticleDto
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(CircleShape.copy(CornerSize(7.dp)))
            .background(Color.Gray.copy(alpha = 0.3F))
            .padding(5.dp)
    ) {
        Column(
        ) {
            Text(
                text = article.getDate()
            )
            Text(
                text = article.title
            )
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = article.urlToImage,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Text(text = article.description)
        }

    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    XNewsTheme {
        MainScreen()
    }
}