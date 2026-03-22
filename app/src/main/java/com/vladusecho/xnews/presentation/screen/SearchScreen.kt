package com.vladusecho.xnews.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.model.SecondaryArticleCard
import com.vladusecho.xnews.presentation.viewModel.SearchCommand
import com.vladusecho.xnews.presentation.viewModel.SearchState
import com.vladusecho.xnews.presentation.viewModel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current

    val viewModel: SearchViewModel = hiltViewModel()

    val state = viewModel.state.collectAsState()
    val currentState = state.value

    val query = viewModel.query.collectAsState()
    val currentQuery = query.value


    Scaffold(
        topBar = {
            Column() {
                TopAppBar(
                    title = {
                        OutlinedTextField(
                            value = currentQuery,
                            onValueChange = { viewModel.processCommand(SearchCommand.InputQuery(it)) },
                            placeholder = {
                                Text(
                                    text = "Начните поиск здесь",
                                    color = Color.Gray,
                                    fontFamily = HeroFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    lineHeight = 10.sp
                                )
                            },
                            maxLines = 1,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            ),
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontFamily = HeroFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                lineHeight = 10.sp
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton({
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back",
                                tint = Color.Black
                            )
                        }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier,
                    thickness = 0.5.dp,
                    color = Color.Gray.copy(alpha = 0.5f)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .background(color = Color.White)
        ) {

            when (currentState) {
                is SearchState.Content -> {
                    LazyColumn(
                    ) {
                        items(
                            items = currentState.articles,
                            key = { it.id }
                        ) { article ->
                            Box(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                SecondaryArticleCard(
                                    article = article,
                                    onArticleClick = {
                                        showArticleInBrowser(article, context)
                                    }
                                )
                            }
                        }
//                        item {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier.padding(16.dp),
//                                    color = Color(0xffFF0606)
//                                )
//                            }
//                        }
                    }
                }
                is SearchState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentState.error,
                            color = Color.Black,
                            fontFamily = HeroFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            lineHeight = 10.sp
                        )
                    }
                }
                SearchState.Initial -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Пусто...",
                            color = Color.Black,
                            fontFamily = HeroFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                            lineHeight = 10.sp
                        )
                    }
                }
                SearchState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xffFF0606)
                        )
                    }
                }
            }
        }
    }
}