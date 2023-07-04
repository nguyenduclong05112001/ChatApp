package com.longhrk.mf.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.longhrk.mf.ui.theme.splashColor
import  com.longhrk.mf.R as resApp

@Composable
fun CheckBoxBase(
    modifier: Modifier,
    values: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    var valueChecked by remember {
        mutableStateOf(values)
    }

    Box(modifier = modifier.clickable {
        valueChecked = !valueChecked
        onCheckedChange(valueChecked)
    }) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = resApp.drawable.ic_check_box),
            tint = splashColor,
            contentDescription = "check box"
        )

        if (valueChecked) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = resApp.drawable.ic_checked),
                tint = splashColor,
                contentDescription = "checked"
            )
        }
    }
}