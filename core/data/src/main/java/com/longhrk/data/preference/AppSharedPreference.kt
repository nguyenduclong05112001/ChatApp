package com.longhrk.data.preference

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPreference @Inject constructor(
    @ApplicationContext context: Context
) {
    private val pref = context.getSharedPreferences("preference_name", Context.MODE_PRIVATE)

    fun setBolPreference(key: String, value: Boolean) {
        pref.edit()
            .putBoolean(key, value)
            .apply()
    }

    fun getBolPreference(key: String): Boolean {
        return pref.getBoolean(key, false)
    }
}