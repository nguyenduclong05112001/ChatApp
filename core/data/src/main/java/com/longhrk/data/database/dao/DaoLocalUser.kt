package com.longhrk.data.database.dao

import androidx.room.*
import com.longhrk.data.database.entity.LocalUser

@Dao
interface DaoLocalUser {

    @Query("SELECT EXISTS(SELECT * FROM users WHERE id = :email)")
    suspend fun isUserIsExist(email: String): Boolean

    @Query("Select * from users")
    suspend fun getUser(): LocalUser

    @Insert
    suspend fun addUser(user: LocalUser)

    @Update
    suspend fun updateUser(user: LocalUser)
}