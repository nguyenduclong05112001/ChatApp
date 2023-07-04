package com.longhrk.data.database.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUser(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "photo")
    val photo: Uri?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String
)