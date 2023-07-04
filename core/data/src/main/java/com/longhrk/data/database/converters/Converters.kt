package com.longhrk.data.database.converters

import android.net.Uri
import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    fun fromUri(uri: Uri?): String {
        return try {
            uri.toString()
        } catch (e: Exception) {
            ""
        }
    }


    @TypeConverter
    fun fromString(data: String?): Uri? {
        return try {
            Uri.parse(data)
        } catch (e: Exception) {
            null
        }
    }
}