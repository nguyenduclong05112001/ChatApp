package com.longhrk.mf.ui.components

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.Glide
import com.longhrk.mf.R
import com.longhrk.mf.core.GetDimension
import com.longhrk.mf.ui.theme.itemColor
import com.longhrk.mf.ui.theme.lovedColor
import com.longhrk.mf.ui.viewmodel.model.Favourite

@Composable
fun ItemFavourite(
    modifier: Modifier,
    favourite: Favourite,
    onLove: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (photo, name, love) = createRefs()

        AndroidView(
            modifier = Modifier
                .constrainAs(photo) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.matchParent
                    width = Dimension.matchParent
                },
            factory = { context ->
                ImageView(context).apply {
                    Glide
                        .with(context)
                        .load(favourite.photo)
                        .error(R.drawable.load_fail)
                        .centerCrop()
                        .into(this)
                }
            }
        )

        Text(
            modifier = Modifier
                .constrainAs(name) {
                    bottom.linkTo(love.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    height = Dimension.wrapContent
                    width = Dimension.fillToConstraints
                }
                .padding(
                    start = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._10sdp),
                    end = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._10sdp),
                    bottom = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._5sdp),
                ),
            text = favourite.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                fontSize = GetDimension.dimensionOfText(res = com.longhrk.dimension.R.dimen._12sdp),
                color = itemColor
            )
        )

        Icon(
            modifier = Modifier
                .constrainAs(love) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .clickable {
                    onLove()
                }
                .padding(
                    end = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._10sdp),
                    bottom = GetDimension.dimensionOf(res = com.longhrk.dimension.R.dimen._10sdp),
                ),
            painter = painterResource(id = R.drawable.ic_heart),
            contentDescription = "Love",
            tint = if (favourite.love) lovedColor else itemColor
        )
    }
}