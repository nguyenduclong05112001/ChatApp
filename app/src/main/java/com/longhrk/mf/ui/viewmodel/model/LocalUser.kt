package com.longhrk.mf.ui.viewmodel.model

import android.net.Uri

data class LocalUser(
    val photo: Uri,
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String
)