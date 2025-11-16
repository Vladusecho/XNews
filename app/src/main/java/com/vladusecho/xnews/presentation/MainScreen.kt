package com.vladusecho.xnews.presentation

import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.vladusecho.xnews.data.models.ArticleDto
import com.vladusecho.xnews.ui.theme.XNewsTheme

@Composable
fun MainScreen() {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    val viewModel: MainViewModel = viewModel()

    val screenState = viewModel.state.collectAsState(MainState.Initial)
    val currentState = screenState.value

    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = "Введите запрос") }
                )
                Button(
                    modifier = Modifier,
                    onClick = {
                        viewModel.loadArticles(text)
                    },
                    colors = ButtonDefaults.buttonColors().copy(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Magenta, Color.Gray)
                        )
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "Найти"
                    )
                }
            }
            when (currentState) {
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is MainState.Error -> {
                    showSnackbar = true
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
//                        Snackbar(
//                            modifier = Modifier
//                                .padding(horizontal = 10.dp, vertical = 30.dp),
//                            contentColor = Color.Black,
//                            containerColor = Color.LightGray
//                        ) {
//                            Text(text = currentState.error)
//                        }
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                            .padding(11.dp)
                        ) {
                            Snackbar(it,
                                contentColor = Color.Black, containerColor = Color.Cyan)
                        }
                        LaunchedEffect(showSnackbar) {
                            if (showSnackbar) {
                                snackbarHostState.showSnackbar(currentState.error)
                                showSnackbar = false
                            }
                        }
                    }

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