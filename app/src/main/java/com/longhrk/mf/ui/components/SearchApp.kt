package com.longhrk.mf.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.longhrk.dimension.R
import com.longhrk.mf.core.GetDimension
import com.longhrk.mf.ui.theme.backgroundSearchColor
import com.longhrk.mf.ui.theme.itemColor
import com.longhrk.mf.ui.theme.negativeBorderColor
import com.longhrk.mf.ui.theme.possibleButtonColor

@Composable
fun SearchApp(
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    onButtonClickSearch: () -> Unit,
    onButtonClick: () -> Unit
) {
    var searchValues by remember {
        mutableStateOf("")
    }

    val localDensity = LocalDensity.current
    var heightcomponent by remember {
        mutableStateOf(0.dp)
    }

    ConstraintLayout(
        modifier = modifier,
    ) {
        val (componentSearch, buttonAdd) = createRefs()

        ConstraintLayout(
            modifier = Modifier
                .constrainAs(componentSearch) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(buttonAdd.start)

                    height = Dimension.wrapContent
                    width = Dimension.fillToConstraints
                }
                .clip(shape = RoundedCornerShape(20))
                .background(backgroundSearchColor)
        ) {
            val (input, button) = createRefs()
            OutlinedTextField(
                modifier = Modifier
                    .padding(
                        horizontal = GetDimension.dimensionOf(res = R.dimen._7sdp),
                    )
                    .constrainAs(input) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(button.start)

                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
                    .onGloballyPositioned {
                        heightcomponent = with(localDensity) {
                            it.size.height.toDp()
                        }
                    },
                value = searchValues,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    textColor = itemColor,
                    cursorColor = itemColor,
                    placeholderColor = negativeBorderColor
                ),
                placeholder = { Text(text = "Search...") },
                textStyle = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = R.dimen._12sdp),
                    fontWeight = FontWeight.Light,
                ),
                singleLine = true,
                maxLines = 1,
                onValueChange = {
                    searchValues = it
                    onValueChange(it)
                },
            )

            IconButton(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)

                        height = Dimension.fillToConstraints
                        width = Dimension.ratio("1:1")
                    }
                    .background(negativeBorderColor),
                onClick = onButtonClickSearch
            ) {
                Icon(
                    painter = painterResource(id = com.longhrk.mf.R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = itemColor
                )
            }
        }

        IconButton(
            modifier = Modifier
                .padding(start = GetDimension.dimensionOf(res = R.dimen._10sdp))
                .constrainAs(buttonAdd) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.value(heightcomponent)
                    width = Dimension.ratio("1:1")
                }
                .clip(shape = RoundedCornerShape(20))
                .background(possibleButtonColor),
            onClick = onButtonClick
        ) {
            Icon(
                painter = painterResource(id = com.longhrk.mf.R.drawable.ic_add),
                contentDescription = "add",
                tint = itemColor
            )
        }
    }
}