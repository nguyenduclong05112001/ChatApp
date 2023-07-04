package com.longhrk.mf.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

object GetDimension {

    @Composable
    fun dimensionOf(res: Int): Dp{
        return dimensionResource(id = res)
    }

    @Composable
    fun dimensionOfText(res: Int): TextUnit{
        return with(LocalDensity.current) {
            dimensionResource(id = res).toSp()
        }
    }
}