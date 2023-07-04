package com.longhrk.mf.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.longhrk.mf.R
import com.longhrk.mf.core.GetDimension
import com.longhrk.mf.ui.theme.itemColor
import com.longhrk.mf.ui.viewmodel.model.Favourite

@Composable
fun FavouritesComponent(
    modifier: Modifier,
    favourites: List<Favourite>
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.favourites),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                fontSize = GetDimension.dimensionOfText(res = com.longhrk.dimension.R.dimen._18sdp),
                color = itemColor
            )
        )

        LazyRow(
            modifier = Modifier
                .padding(
                    vertical = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._10sdp)
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(favourites) {
                ItemFavourite(
                    modifier = Modifier
                        .shadow(
                            clip = true,
                            shape = RoundedCornerShape(40),
                            elevation = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._10sdp)
                        )
                        .clip(shape = RoundedCornerShape(40))
                        .padding(horizontal = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._10sdp))
                        .height(GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._150sdp))
                        .width(GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._100sdp)),
                    favourite = it
                ) {

                }
            }
        }
    }
}