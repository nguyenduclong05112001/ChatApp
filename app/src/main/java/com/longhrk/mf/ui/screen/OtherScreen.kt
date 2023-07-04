package com.longhrk.mf.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun OtherScreen(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
            .clickable { onClick() }
    ) {

    }
}