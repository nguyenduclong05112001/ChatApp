package com.longhrk.mf.ui.extensions

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

fun Context.resourceUri(resourceId: Int): Uri = with(resources) {
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
}