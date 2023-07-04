package com.longhrk.mf.ui.screen

import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.longhrk.mf.core.GetDimension
import com.longhrk.mf.ui.components.CheckBoxBase
import com.longhrk.mf.ui.components.LoadingView
import com.longhrk.mf.ui.components.OutLineTextFieldAuth
import com.longhrk.mf.ui.theme.*
import com.longhrk.mf.ui.viewmodel.AuthViewModel
import com.longhrk.mf.ui.viewmodel.model.ResultFireBase
import com.longhrk.mf.ui.viewmodel.model.UserAuth
import kotlinx.coroutines.launch
import com.longhrk.dimension.R as resDimension
import com.longhrk.mf.R as resApp

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNextScreen: () -> Unit
) {
    val tokenGoogle = stringResource(resApp.string.default_web_client_id)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isLoading by authViewModel.isShowProgressBar.collectAsState()
    val titleCurrent by authViewModel.titleCurrent.collectAsState()
    val result by authViewModel.statusResponse.collectAsState()

    val errorNotify = stringResource(id = resApp.string.error_network)

    val failLogin = stringResource(id = resApp.string.fail_login)

    val launcherGoogle = rememberFirebaseAuthLauncherForGoogle(
        authViewModel = authViewModel,
        onAuthComplete = {
            if (it.user != null) {
                authViewModel.setCurrentUser(it.user)
                authViewModel.setLogin()
                onNextScreen()
            }
        },
        onAuthError = {
            Toast.makeText(context, failLogin, Toast.LENGTH_SHORT).show()
        }
    )

    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }

    val launcherFacebook = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Toast.makeText(context, failLogin, Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    val token = result.accessToken.token
                    val credential = FacebookAuthProvider.getCredential(token)
                    val authResult = authViewModel.signInWithGoogleOrFacebook(credential)
                    if (authResult.user != null) {
                        authViewModel.setCurrentUser(authResult.user)
                        authViewModel.setLogin()
                        onNextScreen()
                    } else {
                        Toast.makeText(context, failLogin, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }

    val onClickFacebook = {
        launcherFacebook.launch(listOf("email", "public_profile"))
    }

    val onClickGoogle = {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(tokenGoogle)
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        launcherGoogle.launch(googleSignInClient.signInIntent)
    }

    LaunchedEffect(isLoading) {
        when (result) {
            ResultFireBase.SUCCESS -> {
                onNextScreen()
            }
            ResultFireBase.ERROR -> {
                Toast.makeText(context, errorNotify, Toast.LENGTH_SHORT).show()
            }
            ResultFireBase.FAIL -> {
                Toast.makeText(context, failLogin, Toast.LENGTH_SHORT).show()
            }
            ResultFireBase.NONE -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { contextImage ->
                ImageView(contextImage).apply {
                    Glide.with(contextImage)
                        .load(resApp.drawable.bg_for_login)
                        .centerCrop()
                        .into(this)
                }
            })

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = GetDimension.dimensionOf(
                        res = resDimension.dimen._10sdp
                    )
                )
        ) {
            val (title, options, inputs) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)

                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                    }
                    .padding(
                        top = GetDimension.dimensionOf(res = resDimension.dimen._70sdp),
                        start = GetDimension.dimensionOf(res = resDimension.dimen._20sdp),
                    ),
                text = titleCurrent,
                style = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._32sdp),
                    fontWeight = FontWeight.Bold,
                    color = backgroundSearchColor,
                    textAlign = TextAlign.Start
                )
            )

            OptionAuth(
                modifier = Modifier
                    .padding(
                        top = GetDimension.dimensionOf(res = resDimension.dimen._15sdp)
                    )
                    .constrainAs(options) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                    },
                authViewModel = authViewModel
            )

            ContentAuth(
                modifier = Modifier
                    .padding(
                        top = GetDimension.dimensionOf(res = resDimension.dimen._15sdp)
                    )
                    .constrainAs(inputs) {
                        top.linkTo(options.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        height = Dimension.fillToConstraints
                        width = Dimension.matchParent
                    },
                authViewModel = authViewModel,
                onClickGoogle = onClickGoogle,
                onClickFacebook = onClickFacebook
            )
        }

        if (isLoading) {
            LoadingView()
        }
    }
}

@Composable
fun OptionAuth(
    modifier: Modifier,
    authViewModel: AuthViewModel,
) {
    val currentTab by authViewModel.titleCurrent.collectAsState()

    val listAuth = mutableListOf(
        "Login", "Register"
    )

    Row(modifier = modifier) {
        listAuth.forEach {
            Box(modifier = Modifier
                .padding(horizontal = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                .clickable {
                    authViewModel.setTitleCurrent(it)
                }
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            all = GetDimension.dimensionOf(res = resDimension.dimen._10sdp),
                        )
                        .align(Alignment.Center),
                    text = it,
                    style = TextStyle(
                        fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._14sdp),
                        fontWeight = if (currentTab == it) FontWeight.Bold else FontWeight.Light,
                        color = if (currentTab == it) possibleButtonColor else negativeBorderColor,
                        textAlign = TextAlign.Center
                    )
                )

                if (currentTab == it) {
                    Divider(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(GetDimension.dimensionOf(res = resDimension.dimen._1sdp))
                            .width(GetDimension.dimensionOf(res = resDimension.dimen._28sdp)),
                        color = backgroundColor
                    )
                }
            }
        }
    }
}

@Composable
fun ContentAuth(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    onClickGoogle: () -> Unit,
    onClickFacebook: () -> Unit
) {
    val currentTab by authViewModel.titleCurrent.collectAsState()

    when (currentTab) {
        "Login" -> {
            LoginContent(
                modifier = modifier,
                authViewModel = authViewModel,
                onClickGoogle = onClickGoogle,
                onClickFacebook = onClickFacebook
            )
        }
        "Register" -> {
            RegisterContent(
                modifier = modifier,
                authViewModel = authViewModel,
                onClickGoogle = onClickGoogle,
                onClickFacebook = onClickFacebook
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterContent(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    onClickGoogle: () -> Unit,
    onClickFacebook: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val keyBoardController = LocalSoftwareKeyboardController.current
    val localFocus = LocalFocusManager.current

    var visualTransformationPassword by remember {
        mutableStateOf("password")
    }

    var visualTransformationConfirm by remember {
        mutableStateOf("password")
    }

    var emailValue by remember {
        mutableStateOf("")
    }

    var passwordValue by remember {
        mutableStateOf("")
    }

    var confirmValue by remember {
        mutableStateOf("")
    }

    Column(modifier = modifier) {
        OutLineTextFieldAuth(
            modifier = Modifier
                .padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                .fillMaxWidth()
                .background(bgOfInputLoginColor)
                .border(
                    width = GetDimension.dimensionOf(res = resDimension.dimen._1sdp),
                    color = splashColor,
                    shape = RoundedCornerShape(20)
                ),
            onValueChange = {
                emailValue = it
            },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            keyboardAction = KeyboardActions(
                onNext = { localFocus.moveFocus(FocusDirection.Next) }
            ),
            placeholder = "Email"
        ) {}

        OutLineTextFieldAuth(
            modifier = Modifier
                .padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                .fillMaxWidth()
                .background(bgOfInputLoginColor)
                .border(
                    width = GetDimension.dimensionOf(res = resDimension.dimen._1sdp),
                    color = splashColor,
                    shape = RoundedCornerShape(20)
                ),
            icon = resApp.drawable.ic_eye,
            isIcon = true,
            onValueChange = {
                passwordValue = it
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next,
            keyboardAction = KeyboardActions(
                onNext = { localFocus.moveFocus(FocusDirection.Next) }
            ),
            visualTransformation = visualTransformationPassword,
            placeholder = "Password"
        ) {
            visualTransformationPassword = if (visualTransformationPassword == "password") {
                "none"
            } else {
                "password"
            }
        }

        OutLineTextFieldAuth(
            modifier = Modifier
                .padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                .fillMaxWidth()
                .background(bgOfInputLoginColor)
                .border(
                    width = GetDimension.dimensionOf(res = resDimension.dimen._1sdp),
                    color = splashColor,
                    shape = RoundedCornerShape(20)
                ),
            icon = resApp.drawable.ic_eye,
            isIcon = true,
            onValueChange = {
                confirmValue = it
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            keyboardAction = KeyboardActions(
                onDone = { keyBoardController?.hide() }
            ),
            visualTransformation = visualTransformationConfirm,
            placeholder = "Confirm"
        ) {
            visualTransformationConfirm = if (visualTransformationConfirm == "password") {
                "none"
            } else {
                "password"
            }
        }

        Button(
            modifier = Modifier
                .padding(
                    horizontal = GetDimension.dimensionOf(res = resDimension.dimen._20sdp),
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._15sdp)
                )
                .clip(shape = RoundedCornerShape(20))
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = possibleButtonColor,
                contentColor = itemColor
            ),
            onClick = {
                scope.launch {
                    authViewModel.signUp(
                        userAuth = UserAuth(
                            email = emailValue.trim(),
                            password = passwordValue.trim()
                        )
                    )
                }
            })
        {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = GetDimension.dimensionOf(res = resDimension.dimen._5sdp),
                    ),
                text = "Register",
                style = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._16sdp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }

        ComponentMoreOption(
            modifier = Modifier.padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)),
            onClickGoogle = onClickGoogle,
            onClickFacebook = onClickFacebook,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginContent(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    onClickGoogle: () -> Unit,
    onClickFacebook: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val keyBoardController = LocalSoftwareKeyboardController.current
    val localFocus = LocalFocusManager.current

    var visualTransformation by remember {
        mutableStateOf("password")
    }

    var checkBoxStatus by remember {
        mutableStateOf(false)
    }

    var emailValue by remember {
        mutableStateOf("")
    }

    var passwordValue by remember {
        mutableStateOf("")
    }

    Column(modifier = modifier) {
        OutLineTextFieldAuth(
            modifier = Modifier
                .padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                .fillMaxWidth()
                .background(bgOfInputLoginColor)
                .border(
                    width = GetDimension.dimensionOf(res = resDimension.dimen._1sdp),
                    color = splashColor,
                    shape = RoundedCornerShape(20)
                ),
            onValueChange = {
                emailValue = it
            },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            keyboardAction = KeyboardActions(
                onNext = { localFocus.moveFocus(FocusDirection.Next) }
            ),
            placeholder = "Email"
        ) {}

        OutLineTextFieldAuth(
            modifier = Modifier
                .padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                .fillMaxWidth()
                .background(bgOfInputLoginColor)
                .border(
                    width = GetDimension.dimensionOf(res = resDimension.dimen._1sdp),
                    color = splashColor,
                    shape = RoundedCornerShape(20)
                ),
            icon = resApp.drawable.ic_eye,
            isIcon = true,
            onValueChange = {
                passwordValue = it
            },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            keyboardAction = KeyboardActions(
                onDone = { keyBoardController?.hide() }
            ),
            visualTransformation = visualTransformation,
            placeholder = "Password"
        ) {
            visualTransformation = if (visualTransformation == "password") {
                "none"
            } else {
                "password"
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._15sdp))
                .fillMaxWidth()
        ) {
            val (checkBox, contentCheckBox, forgetPassword) = createRefs()

            CheckBoxBase(
                modifier = Modifier
                    .constrainAs(checkBox) {
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        top.linkTo(parent.top)

                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                    },
                values = checkBoxStatus,
                onCheckedChange = {
                    checkBoxStatus = it
                }
            )

            Text(
                modifier = Modifier
                    .constrainAs(contentCheckBox) {
                        top.linkTo(parent.top)
                        start.linkTo(checkBox.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
                    .padding(
                        horizontal = GetDimension.dimensionOf(res = resDimension.dimen._5sdp),
                    ),
                text = "Remember password",
                style = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp),
                    fontWeight = FontWeight.Light,
                    color = backgroundColor,
                    textAlign = TextAlign.Start
                )
            )

            Text(
                modifier = Modifier
                    .constrainAs(forgetPassword) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)

                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                text = "Forget password",
                style = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._12sdp),
                    fontWeight = FontWeight.Bold,
                    color = possibleButtonColor,
                    textAlign = TextAlign.Center
                )
            )
        }

        Button(
            modifier = Modifier
                .padding(
                    horizontal = GetDimension.dimensionOf(res = resDimension.dimen._20sdp),
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._15sdp)
                )
                .clip(shape = RoundedCornerShape(20))
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = possibleButtonColor,
                contentColor = itemColor
            ),
            onClick = {
                scope.launch {
                    authViewModel.signIn(
                        userAuth = UserAuth(
                            email = emailValue.trim(),
                            password = passwordValue.trim()
                        )
                    )
                }
            })
        {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = GetDimension.dimensionOf(res = resDimension.dimen._5sdp),
                    ),
                text = "Login",
                style = TextStyle(
                    fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._16sdp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }

        ComponentMoreOption(
            modifier = Modifier.padding(vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)),
            onClickGoogle = onClickGoogle,
            onClickFacebook = onClickFacebook,
        )
    }
}

@Composable
fun ComponentMoreOption(
    modifier: Modifier,
    onClickFacebook: () -> Unit,
    onClickGoogle: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp),
                ),
            text = "OR CONNECT WITH",
            style = TextStyle(
                fontSize = GetDimension.dimensionOfText(res = resDimension.dimen._14sdp),
                fontWeight = FontWeight.Normal,
                color = backgroundColor,
                textAlign = TextAlign.Center
            )
        )

        Row(
            modifier = Modifier
                .padding(
                    vertical = GetDimension.dimensionOf(res = resDimension.dimen._10sdp)
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemMoreOption(
                modifier = Modifier
                    .clickable {
                        onClickFacebook()
                    }
                    .padding(horizontal = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                    .clip(RoundedCornerShape(50))
                    .size(GetDimension.dimensionOf(res = resDimension.dimen._30sdp)),
                icon = resApp.drawable.ic_facebook
            )

            ItemMoreOption(
                modifier = Modifier
                    .clickable {
                        onClickGoogle()
                    }
                    .padding(horizontal = GetDimension.dimensionOf(res = resDimension.dimen._10sdp))
                    .clip(RoundedCornerShape(50))
                    .size(GetDimension.dimensionOf(res = resDimension.dimen._30sdp)),
                icon = resApp.drawable.ic_google
            )
        }
    }
}

@Composable
fun ItemMoreOption(
    modifier: Modifier,
    icon: Int,
) {
    Box(modifier = modifier) {
        AndroidView(
            factory = {
                ImageView(it).apply {
                    Glide.with(it)
                        .load(icon)
                        .centerCrop()
                        .into(this)
                }
            }
        )
    }
}

@Composable
fun rememberFirebaseAuthLauncherForGoogle(
    authViewModel: AuthViewModel,
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = authViewModel.signInWithGoogleOrFacebook(credential)
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}