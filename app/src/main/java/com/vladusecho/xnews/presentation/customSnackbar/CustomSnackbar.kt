package com.vladusecho.xnews.presentation.customSnackbar

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MySnackbarHost(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
    ) { snackbarData ->
        CustomSnackbar(snackbarData = snackbarData)
    }
}

@Composable
fun CustomSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData
) {
    Snackbar(
        modifier = modifier,
        action = {},
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(text = snackbarData.visuals.message)
    }
}