package com.longhrk.data.repo

import com.longhrk.data.Const
import com.longhrk.data.database.AppDatabase
import com.longhrk.data.database.entity.LocalUser
import com.longhrk.data.preference.AppSharedPreference
import javax.inject.Inject

class DataRepo @Inject constructor(
    private val preference: AppSharedPreference,
    private val db: AppDatabase
) {
    fun setIsLogin(isLogin: Boolean) {
        preference.setBolPreference(
            key = Const.IS_LOGIN_KEY,
            value = isLogin
        )
    }

    fun getIsLogin() = preference.getBolPreference(Const.IS_LOGIN_KEY)

    suspend fun getUser() = db.getLocalUserDatabase().getUser()

    suspend fun checkUserIsExist(email: String) = db.getLocalUserDatabase().isUserIsExist(email)

    suspend fun updateUser(user: LocalUser) = db.getLocalUserDatabase().updateUser(user)

    suspend fun addUser(user: LocalUser) = db.getLocalUserDatabase().addUser(user)
}