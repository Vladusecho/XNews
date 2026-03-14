package com.vladusecho.xnews.presentation.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vladusecho.xnews.data.mappers.toArticleDateFormat
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.presentation.state.ImgState
import com.vladusecho.xnews.ui.theme.XNewsTheme

@Composable
fun HotArticleCard(
    modifier: Modifier = Modifier,
    article: Article
) {

    var isLoading: ImgState by remember { mutableStateOf(ImgState.Initial) }

    Box(
        modifier = modifier
            .width(300.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(10))
    ) {
        LoadingImageStatus(isLoading)
        AsyncImage(
            model = article.urlToImage,
            contentDescription = "",
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
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(20))
                .background(Color.Black.copy(alpha = 0.4f))
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = article.source ?: "",
                fontFamily = HeroFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 12.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.1f to Color.Transparent,
                        0.7f to Color.Black
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = article.title,
                    fontFamily = HeroFontFamily,
                    fontWeight = FontWeight.W600,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
@Preview
private fun HotArticleCardPreview() {
    XNewsTheme {
        HotArticleCard(
            article = Article(
                "0",
                "Антон Похиляк",
                "Украина возмутилась из-за возвращения России на престижную выставку",
                "Россия впервые с 2019 года появится на Венецианской биеннале современного искусства — с проектом «Дерево уходит корнями в небо». Об этом сообщает The Art Newspaper Russia",
                "https://lenta.ru/news/2026/03/08/ukraina-vozmutilas-iz-za-vozvrascheniya-rossii-na-prestizhnuyu-mezhdunarodnuyu-vystavku/",
                "https://icdn.lenta.ru/images/2026/03/08/18/20260308182707822/share_c71d67a04bbed4fc20fa304e0741f7f1.jpg",
                "2026-03-08T15:27:07Z".toArticleDateFormat(),
                source = "Lenta"
            )
        )
    }
}