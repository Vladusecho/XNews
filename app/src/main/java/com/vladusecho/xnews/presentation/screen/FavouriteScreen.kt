package com.vladusecho.xnews.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.viewModel.FavouriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: FavouriteViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            Column() {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Избранное ()",
                            fontFamily = HeroFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            fontSize = 19.sp,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    ),
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 15.dp,
                                        bottomEnd = 0.dp,
                                        bottomStart = 15.dp
                                    )
                                )
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(
                                            Color.Red,
                                            Color(0xff61070A)
                                        )
                                    )
                                )
                        ) {
                            Text(
                                text = "XNews",
                                fontFamily = HeroFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(
                                    vertical = 8.dp,
                                    horizontal = 4.dp
                                )
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
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = paddingValues.calculateTopPadding())
        ) {

        }
    }
}

