package com.vladusecho.xnews.presentation.model

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.presentation.state.ImgState

@Composable
fun Article(
    modifier: Modifier = Modifier,
    article: Article
) {

    var isLoading: ImgState by remember { mutableStateOf(ImgState.Initial) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .padding(16.dp)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                clip = true
            )
            .clip(CircleShape.copy(CornerSize(10.dp)))
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                context.startActivity(intent)
            },
    ) {
        Column {
            LoadingImageStatus(isLoading)
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = article.urlToImage ?: "",
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                onLoading = {
                    isLoading = ImgState.LoadingImg
                },
                onSuccess = {
                    isLoading = ImgState.LoadedImg
                },
                onError = {
                    isLoading = ImgState.Error
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = article.getDateWithAuthor(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraLight
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = article.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = article.description,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun LoadingImageStatus(imgState: ImgState) {
    when (imgState) {
        ImgState.Initial -> {}
        ImgState.LoadedImg -> {}
        ImgState.LoadingImg -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ImgState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Error,
                    contentDescription = "Error icon"
                )
            }
        }
    }
}