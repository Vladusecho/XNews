package com.vladusecho.xnews.presentation.state

sealed class ImgState {

    object LoadingImg : ImgState()

    object LoadedImg : ImgState()

    object Initial : ImgState()
    object Error : ImgState()
}