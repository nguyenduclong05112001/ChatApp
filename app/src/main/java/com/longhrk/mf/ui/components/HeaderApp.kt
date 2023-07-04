package com.longhrk.mf.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.longhrk.dimension.R
import com.longhrk.mf.core.GetDimension
import com.longhrk.mf.ui.theme.itemColor

@Composable
fun HeaderApp(
    modifier: Modifier,
    isIcon: Boolean = false,
    contentHeader: String,
    onLogoClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isIcon) {
            Image(
                modifier = Modifier
                    .clickable {
                        onLogoClick()
                    }
                    .padding(
                        vertical = GetDimension.dimensionOf(res = R.dimen._10sdp),
                        horizontal = GetDimension.dimensionOf(res = R.dimen._5sdp)
                    )
                    .size(GetDimension.dimensionOf(res = R.dimen._30sdp)),
                painter = painterResource(id = com.longhrk.mf.R.drawable.circle_logo),
                contentDescription = "Logo"
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(
                    vertical = GetDimension.dimensionOf(res = R.dimen._10sdp),
                    horizontal = GetDimension.dimensionOf(res = R.dimen._5sdp)
                ),
            text = contentHeader,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                fontSize = GetDimension.dimensionOfText(res = R.dimen._16sdp),
                color = itemColor
            )
        )
    }
}