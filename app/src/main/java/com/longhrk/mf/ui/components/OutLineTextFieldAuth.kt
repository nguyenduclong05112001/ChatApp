package com.longhrk.mf.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.longhrk.dimension.R
import com.longhrk.mf.core.GetDimension
import com.longhrk.mf.ui.theme.negativeBorderColor
import com.longhrk.mf.ui.theme.splashColor

@Composable
fun OutLineTextFieldAuth(
    modifier: Modifier,
    icon: Int = 0,
    isIcon: Boolean = false,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardAction: KeyboardActions,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    visualTransformation: String = "none",
    iconClick: () -> Unit
) {
    var inputValues by remember {
        mutableStateOf("")
    }

    ConstraintLayout(modifier = modifier) {
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

                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
            value = inputValues,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                textColor = splashColor,
                cursorColor = splashColor,
                placeholderColor = negativeBorderColor
            ),
            placeholder = { Text(text = placeholder) },
            textStyle = TextStyle(
                fontSize = GetDimension.dimensionOfText(res = R.dimen._14sdp),
                fontWeight = FontWeight.Light,
            ),
            singleLine = true,
            maxLines = 1,
            onValueChange = {
                inputValues = it
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardAction,
            visualTransformation = if (visualTransformation == "none") VisualTransformation.None else PasswordVisualTransformation()
        )

        if (isIcon && inputValues.isNotEmpty()) {
            IconButton(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(input.end)

                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                    },
                onClick = iconClick
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = splashColor
                )
            }
        }
    }
}