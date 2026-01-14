package com.vladusecho.xnews.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter

sealed class ImgState {

    object LoadingImg : ImgState()

    object LoadedImg : ImgState()

    object Initial : ImgState()
    object Error : ImgState()
}