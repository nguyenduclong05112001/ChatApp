package com.longhrk.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.longhrk.data.database.converters.Converters
import com.longhrk.data.database.dao.DaoLocalUser
import com.longhrk.data.database.entity.LocalUser

@Database(
    entities = [
        LocalUser::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getLocalUserDatabase(): DaoLocalUser
}