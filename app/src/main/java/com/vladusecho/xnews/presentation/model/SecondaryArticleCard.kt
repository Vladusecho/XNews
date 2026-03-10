package com.vladusecho.xnews.presentation.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.Image
import coil3.compose.AsyncImage
import com.vladusecho.xnews.R
import com.vladusecho.xnews.data.mappers.toArticleDateFormat
import com.vladusecho.xnews.domain.models.Article
import com.vladusecho.xnews.presentation.state.ImgState
import com.vladusecho.xnews.ui.theme.XNewsTheme
import java.text.SimpleDateFormat

@Composable
fun SecondaryArticleCard(
    modifier: Modifier = Modifier,
    article: Article
) {
    var isLoading: ImgState by remember { mutableStateOf(ImgState.Initial) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = article.title,
                    fontFamily = HeroFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
                Spacer(modifier.height(8.dp))
                Text(
                    text = article.description,
                    fontFamily = HeroFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xff565E6E),
                    fontSize = 10.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 12.sp
                )
                Spacer(modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val formatter = SimpleDateFormat.getInstance()
                    Text(
                        text = formatter.format(article.publishedAt),
                        fontFamily = HeroFontFamily,
                        fontWeight = FontWeight.Light,
                        color = Color.Black,
                        fontSize = 10.sp,
                    )
                }
            }
            Spacer(modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.BottomEnd
            ) {
                LoadingImageStatus(isLoading)
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                    ,
                    model = article.urlToImage,
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
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
                        .background(Color.Black.copy(alpha = 0.6f))
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
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SecondaryArticleCardPreview() {
    XNewsTheme {
        SecondaryArticleCard(
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

